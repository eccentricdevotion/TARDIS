/*
 * Copyright (C) 2014 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.flight;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.artron.TARDISArtronIndicator;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetNextLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.DISK_CIRCUIT;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.travel.TARDISMalfunction;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Lever;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * The handbrake was a utensil on the TARDIS used for quick stops. River song
 * once claimed that the TARDIS made it's "whoosh" noise because the Doctor had
 * left the handbrake on.
 *
 * @author eccentric_nz
 */
public class TARDISHandbrakeListener implements Listener {

    private final TARDIS plugin;

    public TARDISHandbrakeListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with the handbrake (lever) on the TARDIS
     * console. If the button is right-clicked the handbrake is set off, if
     * right-clicked while sneaking it is set on.
     *
     * @param event the player clicking the handbrake
     */
    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            if (blockType == Material.LEVER) {
                // Checks handbrake location against the database.
                final Location handbrake_loc = block.getLocation();
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("type", 0);
                where.put("location", handbrake_loc.toString());
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (rsc.resultSet()) {
                    event.setCancelled(true);
                    final int id = rsc.getTardis_id();
                    TARDISCircuitChecker tcc = null;
                    if (plugin.getConfig().getString("preferences.difficulty").equals("hard") && !plugin.getUtils().inGracePeriod(player, event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
                        tcc = new TARDISCircuitChecker(plugin, id);
                        tcc.getCircuits();
                    }
                    if (tcc != null && !tcc.hasMaterialisation()) {
                        TARDISMessage.send(player, "NO_MAT_CIRCUIT");
                        return;
                    }
                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                        return;
                    }
                    HashMap<String, Object> wherei = new HashMap<String, Object>();
                    wherei.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    final HashMap<String, Object> setcurrent = new HashMap<String, Object>();
                    final HashMap<String, Object> wherecurrent = new HashMap<String, Object>();
                    final HashMap<String, Object> setback = new HashMap<String, Object>();
                    final HashMap<String, Object> whereback = new HashMap<String, Object>();
                    final HashMap<String, Object> setdoor = new HashMap<String, Object>();
                    final HashMap<String, Object> wheredoor = new HashMap<String, Object>();
                    if (rs.resultSet()) {
                        if (rs.getPreset().equals(PRESET.JUNK)) {
                            return;
                        }
                        UUID ownerUUID = rs.getUuid();
                        final UUID uuid = player.getUniqueId();
                        if ((rs.isIso_on() && !uuid.equals(ownerUUID) && event.isCancelled() && !player.hasPermission("tardis.skeletonkey")) || plugin.getTrackerKeeper().getJohnSmith().containsKey(uuid)) {
                            // check if cancelled so we don't get double messages from the bind listener
                            TARDISMessage.send(player, "ISO_HANDS_OFF");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered_on()) {
                            TARDISMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        boolean cham = rs.isChamele_on();
                        boolean hidden = rs.isHidden();
                        String beacon = rs.getBeacon();
                        String eps = rs.getEps();
                        String creeper = rs.getCreeper();
                        Location exit = null;
                        boolean error = false;
                        if (plugin.getTrackerKeeper().getInVortex().contains(id)) {
                            TARDISMessage.send(player, "HANDBRAKE_IN_VORTEX");
                        } else {
                            Action action = event.getAction();
                            BlockState state = block.getState();
                            Lever lever = (Lever) state.getData();
                            int dist = 0;
                            // should the beacon turn on
                            HashMap<String, Object> wherek = new HashMap<String, Object>();
                            wherek.put("uuid", uuid.toString());
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherek);
                            boolean beac_on = true;
                            boolean minecart = false;
                            boolean bar = false;
                            int flight_mode = 1;
                            if (rsp.resultSet()) {
                                beac_on = rsp.isBeaconOn();
                                minecart = rsp.isMinecartOn();
                                flight_mode = rsp.getFlightMode();
                                bar = rsp.isTravelbarOn();
                            }
                            final QueryFactory qf = new QueryFactory(plugin);
                            if (action == Action.RIGHT_CLICK_BLOCK) {
                                if (rs.isHandbrake_on()) {
                                    if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                                        // check if door is open
                                        if (isDoorOpen(id)) {
                                            TARDISMessage.send(player, "DOOR_CLOSE");
                                            return;
                                        }
                                        TARDISSounds.playTARDISSound(handbrake_loc, player, "tardis_handbrake_release");
                                        if (!beac_on && !beacon.isEmpty()) {
                                            toggleBeacon(beacon, true);
                                        }
                                        HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                        wherecl.put("tardis_id", id);
                                        ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
                                        String resetw = "";
                                        Location l = null;
                                        if (!rscl.resultSet()) {
                                            hidden = true;
                                        } else {
                                            resetw = rscl.getWorld().getName();
                                            l = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                                        }
                                        COMPASS cd = rscl.getDirection();
                                        boolean sub = rscl.isSubmarine();
                                        Biome biome = rscl.getBiome();
                                        COMPASS sd = cd;
                                        boolean malfunction = false;
                                        boolean is_next_sub = false;
                                        if (plugin.getConfig().getInt("preferences.malfunction") > 0) {
                                            // check for a malfunction
                                            TARDISMalfunction m = new TARDISMalfunction(plugin, id, player, cd, handbrake_loc, eps, creeper);
                                            malfunction = m.isMalfunction();
                                            if (malfunction) {
                                                exit = m.getMalfunction();
                                                if (exit != null) {
                                                    HashMap<String, Object> wheress = new HashMap<String, Object>();
                                                    wheress.put("tardis_id", id);
                                                    HashMap<String, Object> setsave = new HashMap<String, Object>();
                                                    setsave.put("world", exit.getWorld().getName());
                                                    setsave.put("x", exit.getBlockX());
                                                    setsave.put("y", exit.getBlockY());
                                                    setsave.put("z", exit.getBlockZ());
                                                    setsave.put("submarine", 0);
                                                    qf.doUpdate("next", setsave, wheress);
                                                    if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                                                        int amount = plugin.getTrackerKeeper().getHasDestination().get(id) * -1;
                                                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                                                        wheret.put("tardis_id", id);
                                                        qf.alterEnergyLevel("tardis", amount, wheret, player);
                                                        TARDISMessage.send(player, "Q_FLY");
                                                        plugin.getTrackerKeeper().getHasDestination().remove(id);
                                                    }
                                                    // play tardis crash sound
                                                    TARDISSounds.playTARDISSound(handbrake_loc, player, "tardis_malfunction");
                                                    // add a potion effect to the player
                                                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 5));
                                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            TARDISSounds.playTARDISSound(handbrake_loc, player, "tardis_cloister_bell");
                                                        }
                                                    }, 300L);
                                                } else {
                                                    malfunction = false;
                                                }
                                            }
                                        }
                                        if (!malfunction) {
                                            HashMap<String, Object> wherenl = new HashMap<String, Object>();
                                            wherenl.put("tardis_id", id);
                                            ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, wherenl);
                                            if (!rsn.resultSet()) {
                                                TARDISMessage.send(player, "DEST_NO_LOAD");
                                                return;
                                            }
                                            is_next_sub = rsn.isSubmarine();
                                            exit = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
                                            sd = rsn.getDirection();
                                            // Changes the lever to off
                                            lever.setPowered(false);
                                            state.setData(lever);
                                            state.update();
                                            // Sets database and sends the player/world message/sounds
                                            set.put("handbrake_on", 0);
                                            TARDISMessage.send(player, "HANDBRAKE_OFF");
                                            if (!minecart) {
                                                TARDISSounds.playTARDISSound(handbrake_loc, player, "tardis_takeoff");
                                                // play sound at current TARDIS location
                                                if (l != null) {
                                                    TARDISSounds.playTARDISSoundNearby(l, "tardis_takeoff");
                                                }
                                            } else {
                                                handbrake_loc.getWorld().playSound(handbrake_loc, Sound.MINECART_INSIDE, 1.0F, 0.0F);
                                            }
                                        }
                                        if (exit != null) {
                                            // Removes Blue Box and loads chunk if it unloaded somehow
                                            if (!exit.getWorld().isChunkLoaded(exit.getChunk())) {
                                                exit.getWorld().loadChunk(exit.getChunk());
                                            }
                                            boolean mat = plugin.getConfig().getBoolean("police_box.materialise");
                                            if (mat && bar && plugin.isBarAPIOnServer()) {
                                                long bar_time = (flight_mode == 2 || flight_mode == 3) ? 1500L : 880L;
                                                new TARDISTravelBar(plugin).showTravelRemaining(player, bar_time);
                                            }
                                            plugin.getTrackerKeeper().getInVortex().add(id);
                                            final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
                                            pdd.setChameleon(cham);
                                            pdd.setDirection(cd);
                                            pdd.setLocation(l);
                                            pdd.setDematerialise(mat);
                                            pdd.setPlayer(player);
                                            pdd.setHide(false);
                                            pdd.setOutside(false);
                                            pdd.setSubmarine(sub);
                                            pdd.setTardisID(id);
                                            pdd.setBiome(biome);
                                            if (!hidden && !plugin.getTrackerKeeper().getReset().contains(resetw)) {
                                                plugin.getTrackerKeeper().getDematerialising().add(id);
                                                plugin.getPresetDestroyer().destroyPreset(pdd);
                                            } else {
                                                // set hidden false!
                                                set.put("hidden", 0);
                                                plugin.getPresetDestroyer().removeBlockProtection(id, new QueryFactory(plugin));
                                            }
                                            // get destination flight data
                                            final TARDISMaterialisationData pbd = new TARDISMaterialisationData();
                                            pbd.setChameleon(cham);
                                            pbd.setDirection(sd);
                                            pbd.setLocation(exit);
                                            pbd.setMalfunction(malfunction);
                                            pbd.setOutside(false);
                                            pbd.setPlayer(player);
                                            pbd.setRebuild(false);
                                            pbd.setSubmarine(is_next_sub);
                                            pbd.setTardisID(id);
                                            pbd.setDistance(dist);
                                            pbd.setResetWorld(resetw);
                                            pbd.setFromLocation(l);
                                            // remember flight data
                                            plugin.getTrackerKeeper().getFlightData().put(uuid, pbd);
                                            long delay = (mat) ? 500L : 10L;
                                            // flight mode
                                            if (mat && (flight_mode == 2 || flight_mode == 3)) {
                                                delay += 650L;
                                                Runnable runner = (flight_mode == 2) ? new TARDISRegulatorStarter(plugin, player) : new TARDISManualFlightStarter(plugin, player, id);
                                                // start the flying mode when demat has finished
                                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runner, 500L);
                                            }
                                            final boolean mine_sound = minecart;
                                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                                @Override
                                                public void run() {
                                                    TARDISMaterialisationData m_data = plugin.getTrackerKeeper().getFlightData().get(uuid);
                                                    Location final_location = m_data.getLocation();
                                                    plugin.getPresetBuilder().buildPreset(m_data);
                                                    if (!mine_sound) {
                                                        TARDISSounds.playTARDISSound(handbrake_loc, player, "tardis_land");
                                                        TARDISSounds.playTARDISSoundNearby(final_location, "tardis_land");
                                                    } else {
                                                        handbrake_loc.getWorld().playSound(handbrake_loc, Sound.MINECART_INSIDE, 1.0F, 0.0F);
                                                    }
                                                    // current
                                                    setcurrent.put("world", final_location.getWorld().getName());
                                                    setcurrent.put("x", final_location.getBlockX());
                                                    setcurrent.put("y", final_location.getBlockY());
                                                    setcurrent.put("z", final_location.getBlockZ());
                                                    setcurrent.put("direction", m_data.getDirection().toString());
                                                    setcurrent.put("submarine", (m_data.isSubmarine()) ? 1 : 0);
                                                    wherecurrent.put("tardis_id", id);
                                                    // get current location for back
                                                    HashMap<String, Object> wherecu = new HashMap<String, Object>();
                                                    wherecu.put("tardis_id", id);
                                                    ResultSetCurrentLocation rscu = new ResultSetCurrentLocation(plugin, wherecu);
                                                    if (!rscu.resultSet()) {
                                                        // back
                                                        setback.put("world", final_location.getWorld().getName());
                                                        setback.put("x", final_location.getX());
                                                        setback.put("y", final_location.getY());
                                                        setback.put("z", final_location.getZ());
                                                        setback.put("direction", final_location.getDirection().toString());
                                                        setback.put("submarine", (m_data.isSubmarine()) ? 1 : 0);
                                                    } else {
                                                        // back
                                                        setback.put("world", rscu.getWorld().getName());
                                                        setback.put("x", rscu.getX());
                                                        setback.put("y", rscu.getY());
                                                        setback.put("z", rscu.getZ());
                                                        setback.put("direction", rscu.getDirection().toString());
                                                        setback.put("submarine", (rscu.isSubmarine()) ? 1 : 0);
                                                    }
                                                    whereback.put("tardis_id", id);
                                                    // update Police Box door direction
                                                    setdoor.put("door_direction", m_data.getDirection().toString());
                                                    wheredoor.put("tardis_id", id);
                                                    wheredoor.put("door_type", 0);
                                                    if (setcurrent.size() > 0) {
                                                        qf.doUpdate("current", setcurrent, wherecurrent);
                                                        qf.doUpdate("back", setback, whereback);
                                                        qf.doUpdate("doors", setdoor, wheredoor);
                                                    }
                                                    if (plugin.getAchievementConfig().getBoolean("travel.enabled") && !plugin.getTrackerKeeper().getReset().contains(m_data.getResetWorld())) {
                                                        if (m_data.getFromLocation() != null && m_data.getFromLocation().getWorld().equals(final_location.getWorld())) {
                                                            m_data.setDistance((int) m_data.getFromLocation().distance(final_location));
                                                            if (m_data.getDistance() > 0 && plugin.getAchievementConfig().getBoolean("travel.enabled")) {
                                                                TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, "travel", 1);
                                                                taf.doAchievement(m_data.getDistance());
                                                            }
                                                        }
                                                    }
                                                    // forget flight data
                                                    plugin.getTrackerKeeper().getFlightData().remove(uuid);
                                                }
                                            }, delay);
                                            if (plugin.getTrackerKeeper().getDamage().containsKey(id)) {
                                                plugin.getTrackerKeeper().getDamage().remove(id);
                                            }
                                            // set last use
                                            long now;
                                            if (player.hasPermission("tardis.prune.bypass")) {
                                                now = Long.MAX_VALUE;
                                            } else {
                                                now = System.currentTimeMillis();
                                            }
                                            set.put("lastuse", now);
                                        } else {
                                            TARDISMessage.send(player, "TRAVEL_NO_EXIT");
                                            error = true;
                                        }
                                    } else {
                                        TARDISMessage.send(player, "TRAVEL_NEED_DEST");
                                        error = true;
                                    }
                                } else {
                                    TARDISMessage.send(player, "HANDBRAKE_OFF_ERR");
                                    error = true;
                                }
                            }
                            if (action == Action.LEFT_CLICK_BLOCK) {
                                if (!rs.isHandbrake_on()) {
                                    TARDISSounds.playTARDISSound(handbrake_loc, player, "tardis_handbrake_engage");
                                    // Changes the lever to on
                                    lever.setPowered(true);
                                    state.setData(lever);
                                    state.update();
                                    // Check if its at a recharge point
                                    TARDISArtronLevels tal = new TARDISArtronLevels(plugin);
                                    tal.recharge(id);
                                    if (!beac_on && !beacon.isEmpty()) {
                                        toggleBeacon(beacon, false);
                                    }
                                    // Remove energy from TARDIS and sets database
                                    set.put("handbrake_on", 1);
                                    TARDISMessage.send(player, "HANDBRAKE_ON");
                                    if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                                        int amount = plugin.getTrackerKeeper().getHasDestination().get(id) * -1;
                                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                                        wheret.put("tardis_id", id);
                                        qf.alterEnergyLevel("tardis", amount, wheret, player);
                                        if (!uuid.equals(ownerUUID)) {
                                            Player ptl = plugin.getServer().getPlayer(ownerUUID);
                                            if (ptl != null) {
                                                new TARDISArtronIndicator(plugin).showArtronLevel(ptl, id, Math.abs(amount));
                                            }
                                        }
                                    }
                                    plugin.getTrackerKeeper().getHasDestination().remove(id);
                                    if (plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                        plugin.getTrackerKeeper().getHasRandomised().remove(Integer.valueOf(id));
                                    }
                                    // damage the circuit if configured
                                    if (tcc != null && plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                                        // decrement uses
                                        int uses_left = tcc.getMaterialisationUses();
                                        new TARDISCircuitDamager(plugin, DISK_CIRCUIT.MATERIALISATION, uses_left, id, player).damage();
                                    }
                                } else {
                                    TARDISMessage.send(player, "HANDBRAKE_ON_ERR");
                                    error = true;
                                }
                            }
                            if (!error) {
                                HashMap<String, Object> whereh = new HashMap<String, Object>();
                                whereh.put("tardis_id", id);
                                qf.doUpdate("tardis", set, whereh);
                            }
                        }
                    }
                }
            }
        }
    }

    private void toggleBeacon(String str, boolean on) {
        String[] beaconData = str.split(":");
        World w = plugin.getServer().getWorld(beaconData[0]);
        int bx = TARDISNumberParsers.parseInt(beaconData[1]);
        int by = TARDISNumberParsers.parseInt(beaconData[2]);
        int bz = TARDISNumberParsers.parseInt(beaconData[3]);
        Location bl = new Location(w, bx, by, bz);
        Block b = bl.getBlock();
        b.setType((on) ? Material.GLASS : Material.REDSTONE_BLOCK);
    }

    @SuppressWarnings("deprecation")
    private boolean isDoorOpen(int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
        if (rs.resultSet()) {
            byte data = TARDISLocationGetters.getLocationFromDB(rs.getDoor_location(), 0.0f, 0.0f).getBlock().getData();
            return plugin.getGeneralKeeper().getDoorListener().isDoorOpen(data, rs.getDoor_direction());
        }
        return false;
    }
}
