/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.api.event.TARDISMalfunctionEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISMaterialisationEvent;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.ADVANCEMENT;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.travel.TARDISMalfunction;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISMaterialseFromVortex implements Runnable {

    private final TARDIS plugin;
    private final int id;
    private final Player player;
    private final Location handbrake;

    public TARDISMaterialseFromVortex(TARDIS plugin, int id, Player player, Location handbrake) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
        this.handbrake = handbrake;
    }

    @Override
    public void run() {
        UUID uuid = player.getUniqueId();
        HashMap<String, Object> wherenl = new HashMap<>();
        wherenl.put("tardis_id", id);
        ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, wherenl);
        if (!rsn.resultSet()) {
            TARDISMessage.send(player, "DEST_NO_LOAD");
            return;
        }
        Location exit = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
        QueryFactory qf = new QueryFactory(plugin);
        boolean is_next_sub = rsn.isSubmarine();
        boolean malfunction = (plugin.getTrackerKeeper().getMalfunction().containsKey(id) && plugin.getTrackerKeeper().getMalfunction().get(id));
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            // get current location for back
            HashMap<String, Object> wherecu = new HashMap<>();
            wherecu.put("tardis_id", id);
            ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecu);
            if (rscl.resultSet()) {
                BukkitScheduler scheduler = plugin.getServer().getScheduler();
                if (malfunction) {
                    // check for a malfunction
                    TARDISMalfunction m = new TARDISMalfunction(plugin);
                    exit = m.getMalfunction(id, player, rscl.getDirection(), handbrake, tardis.getEps(), tardis.getCreeper());
                    if (exit != null) {
                        plugin.getTrackerKeeper().getRescue().remove(id);
                        HashMap<String, Object> wheress = new HashMap<>();
                        wheress.put("tardis_id", id);
                        HashMap<String, Object> setsave = new HashMap<>();
                        setsave.put("world", exit.getWorld().getName());
                        setsave.put("x", exit.getBlockX());
                        setsave.put("y", exit.getBlockY());
                        setsave.put("z", exit.getBlockZ());
                        setsave.put("submarine", 0);
                        qf.doSyncUpdate("next", setsave, wheress);
                        if (plugin.getTrackerKeeper().getHasDestination().containsKey(id)) {
                            int amount = plugin.getTrackerKeeper().getHasDestination().get(id) * -1;
                            HashMap<String, Object> wheret = new HashMap<>();
                            wheret.put("tardis_id", id);
                            qf.alterEnergyLevel("tardis", amount, wheret, player);
                            TARDISMessage.send(player, "Q_FLY");
                            plugin.getTrackerKeeper().getHasDestination().remove(id);
                        }
                        plugin.getPM().callEvent(new TARDISMalfunctionEvent(player, tardis, exit));
                        // set beacon colour to red
                        if (!tardis.getBeacon().isEmpty()) {
                            setBeaconUpBlock(tardis.getBeacon(), id);
                        }
                        // play tardis crash sound
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            TARDISSounds.playTARDISSound(handbrake, "tardis_malfunction");
                        }
                        // add a potion effect to the player
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 5));
                        long cloister_delay = (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) ? 262L : 360L;
                        scheduler.scheduleSyncDelayedTask(plugin, () -> TARDISSounds.playTARDISSound(handbrake, "tardis_cloister_bell"), cloister_delay);
                    } else {
                        malfunction = false;
                    }
                }
                if (exit != null) {
                    if (!exit.getWorld().isChunkLoaded(exit.getChunk())) {
                        exit.getWorld().loadChunk(exit.getChunk());
                    }
                    PRESET preset = tardis.getPreset();
                    COMPASS sd = rsn.getDirection();
                    HashMap<String, Object> wherek = new HashMap<>();
                    wherek.put("uuid", uuid.toString());
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherek);
                    boolean minecart = false;
                    boolean set_biome = true;
                    boolean bar = false;
                    int flight_mode = 1;
                    if (rsp.resultSet()) {
                        minecart = rsp.isMinecartOn();
                        set_biome = rsp.isPoliceboxTexturesOn();
                        bar = rsp.isTravelbarOn();
                        flight_mode = rsp.getFlightMode();
                    }
                    // set destination flight data
                    BuildData bd = new BuildData(plugin, uuid.toString());
                    bd.setDirection(sd);
                    bd.setLocation(exit);
                    bd.setMalfunction(false);
                    bd.setOutside(false);
                    bd.setPlayer(player);
                    bd.setRebuild(false);
                    bd.setSubmarine(is_next_sub);
                    bd.setTardisID(id);
                    bd.setTexture(set_biome);

                    // remember flight data
                    plugin.getTrackerKeeper().getFlightData().put(uuid, bd);
                    long flight_mode_delay = ((plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) ? 0L : 518L);
                    long materialisation_delay = flight_mode_delay;
                    long travel_time = (malfunction) ? 400L : 375L;
                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id) && malfunction) {
                        materialisation_delay += 262L;
                        travel_time += 262L;
                    }
                    // flight mode
                    if (flight_mode == 2 || flight_mode == 3) {
                        materialisation_delay += 650L;
                        travel_time += 650L;
                        Runnable runner = (flight_mode == 2) ? new TARDISRegulatorStarter(plugin, player, id) : new TARDISManualFlightStarter(plugin, player, id);
                        // start the flying mode (after demat if not in vortex already)
                        scheduler.scheduleSyncDelayedTask(plugin, runner, flight_mode_delay);
                    }
                    if (bar) {
                        long tt = travel_time;
                        // start travel bar
                        scheduler.scheduleSyncDelayedTask(plugin, () -> new TARDISTravelBar(plugin).showTravelRemaining(player, tt, false), flight_mode_delay);
                    }
                    // cancel repeating sfx task
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                            scheduler.cancelTask(plugin.getTrackerKeeper().getDestinationVortex().get(id));
                        }
                    }, materialisation_delay - 140L);
                    boolean mine_sound = minecart;
                    Location sound_loc = (preset.equals(PRESET.JUNK_MODE)) ? exit : handbrake;
                    Location external_sound_loc = exit;
                    boolean malchk = malfunction;
                    scheduler.scheduleSyncDelayedTask(plugin, () -> {
                        BuildData b_data = plugin.getTrackerKeeper().getFlightData().get(uuid);
                        Location final_location = b_data.getLocation();
                        Location l = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                        plugin.getPM().callEvent(new TARDISMaterialisationEvent(player, tardis, final_location));
                        plugin.getPresetBuilder().buildPreset(b_data);
                        if (!mine_sound) {
                            if (!preset.equals(PRESET.JUNK_MODE)) {
                                if (!malchk) {
                                    TARDISSounds.playTARDISSound(sound_loc, "tardis_land");
                                    TARDISSounds.playTARDISSound(external_sound_loc, "tardis_land");
                                } else {
                                    TARDISSounds.playTARDISSound(sound_loc, "tardis_emergency_land");
                                    TARDISSounds.playTARDISSound(external_sound_loc, "tardis_emergency_land");
                                }
                            } else {
                                TARDISSounds.playTARDISSound(sound_loc, "junk_land");
                            }
                        } else {
                            handbrake.getWorld().playSound(handbrake, Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                        }
                        HashMap<String, Object> setcurrent = new HashMap<>();
                        HashMap<String, Object> wherecurrent = new HashMap<>();
                        HashMap<String, Object> setback = new HashMap<>();
                        HashMap<String, Object> whereback = new HashMap<>();
                        HashMap<String, Object> setdoor = new HashMap<>();
                        HashMap<String, Object> wheredoor = new HashMap<>();
                        // current
                        setcurrent.put("world", final_location.getWorld().getName());
                        setcurrent.put("x", final_location.getBlockX());
                        setcurrent.put("y", final_location.getBlockY());
                        setcurrent.put("z", final_location.getBlockZ());
                        setcurrent.put("direction", b_data.getDirection().toString());
                        setcurrent.put("submarine", (b_data.isSubmarine()) ? 1 : 0);
                        wherecurrent.put("tardis_id", id);
                        // back
                        setback.put("world", rscl.getWorld().getName());
                        setback.put("x", rscl.getX());
                        setback.put("y", rscl.getY());
                        setback.put("z", rscl.getZ());
                        setback.put("direction", rscl.getDirection().toString());
                        setback.put("submarine", (rscl.isSubmarine()) ? 1 : 0);
                        whereback.put("tardis_id", id);
                        // update Police Box door direction
                        setdoor.put("door_direction", b_data.getDirection().toString());
                        wheredoor.put("tardis_id", id);
                        wheredoor.put("door_type", 0);
                        if (setcurrent.size() > 0) {
                            qf.doUpdate("current", setcurrent, wherecurrent);
                            qf.doUpdate("back", setback, whereback);
                            qf.doUpdate("doors", setdoor, wheredoor);
                        }
                        if (plugin.getAchievementConfig().getBoolean("travel.enabled") && !plugin.getTrackerKeeper().getReset().contains(rscl.getWorld().getName())) {
                            if (l.getWorld().equals(final_location.getWorld())) {
                                int distance = (int) l.distance(final_location);
                                if (distance > 0 && plugin.getAchievementConfig().getBoolean("travel.enabled")) {
                                    TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, ADVANCEMENT.TRAVEL, 1);
                                    taf.doAchievement(distance);
                                }
                            }
                        }
                        if (!malchk) {
                            // forget flight data
                            plugin.getTrackerKeeper().getFlightData().remove(uuid);
                        }
                    }, materialisation_delay);
                    plugin.getTrackerKeeper().getDamage().remove(id);
                    // set last use
                    long now;
                    if (player.hasPermission("tardis.prune.bypass")) {
                        now = Long.MAX_VALUE;
                    } else {
                        now = System.currentTimeMillis();
                    }
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("lastuse", now);
                    HashMap<String, Object> whereh = new HashMap<>();
                    whereh.put("tardis_id", id);
                    qf.doUpdate("tardis", set, whereh);
                }
            }
        }
    }

    private void setBeaconUpBlock(String str, int id) {
        String[] beaconData = str.split(":");
        World w = plugin.getServer().getWorld(beaconData[0]);
        int bx = TARDISNumberParsers.parseInt(beaconData[1]);
        int by = TARDISNumberParsers.parseInt(beaconData[2]) + 1;
        int bz = TARDISNumberParsers.parseInt(beaconData[3]);
        Location bl = new Location(w, bx, by, bz);
        Block b = bl.getBlock();
        while (!b.getType().equals(Material.BEACON) && b.getLocation().getBlockY() > 0) {
            b = b.getRelative(BlockFace.DOWN);
        }
        TARDISBlockSetters.setBlockAndRemember(b.getRelative(BlockFace.UP), Material.RED_STAINED_GLASS, id, 2);
    }
}
