/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetNextLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
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

/**
 *
 * @author eccentric_nz
 */
public class TARDISMaterialseFromVortex implements Runnable {

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
        final UUID uuid = player.getUniqueId();
        HashMap<String, Object> wherenl = new HashMap<String, Object>();
        wherenl.put("tardis_id", id);
        ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, wherenl);
        if (!rsn.resultSet()) {
            TARDISMessage.send(player, "DEST_NO_LOAD");
            return;
        }
        Location exit = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
        final QueryFactory qf = new QueryFactory(plugin);
        boolean is_next_sub = rsn.isSubmarine();
        boolean malfunction = false;
        HashMap<String, Object> wherei = new HashMap<String, Object>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            boolean cham = tardis.isChamele_on();
            // get current location for back
            HashMap<String, Object> wherecu = new HashMap<String, Object>();
            wherecu.put("tardis_id", id);
            final ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecu);
            if (rscl.resultSet()) {
                if (plugin.getConfig().getInt("preferences.malfunction") > 0) {
                    // check for a malfunction
                    TARDISMalfunction m = new TARDISMalfunction(plugin, id, player, rscl.getDirection(), handbrake, tardis.getEps(), tardis.getCreeper());
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
                            // set beacon colour to red
                            if (!tardis.getBeacon().isEmpty()) {
                                setBeaconUpBlock(tardis.getBeacon(), id);
                            }
                            // play tardis crash sound
                            TARDISSounds.playTARDISSound(handbrake, "tardis_malfunction");
                            // add a potion effect to the player
                            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150, 5));
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    TARDISSounds.playTARDISSound(handbrake, "tardis_cloister_bell");
                                }
                            }, 300L);
                        } else {
                            malfunction = false;
                        }
                    }
                }
                if (exit != null) {
                    if (!exit.getWorld().isChunkLoaded(exit.getChunk())) {
                        exit.getWorld().loadChunk(exit.getChunk());
                    }
                    final PRESET preset = tardis.getPreset();
                    COMPASS sd = rsn.getDirection();
                    HashMap<String, Object> wherek = new HashMap<String, Object>();
                    wherek.put("uuid", uuid.toString());
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherek);
                    boolean minecart = false;
                    boolean set_biome = true;
                    int flight_mode = 1;
                    if (rsp.resultSet()) {
                        minecart = rsp.isMinecartOn();
                        set_biome = rsp.isPoliceboxTexturesOn();
                        flight_mode = rsp.getFlightMode();
                    }
                    // set destination flight data
                    final BuildData bd = new BuildData(plugin, uuid.toString());
                    bd.setChameleon(cham);
                    bd.setDirection(sd);
                    bd.setLocation(exit);
                    bd.setMalfunction(false);
                    bd.setOutside(false);
                    bd.setPlayer(player);
                    bd.setRebuild(false);
                    bd.setSubmarine(is_next_sub);
                    bd.setTardisID(id);
                    bd.setTexture(set_biome);

                    //plugin.getPresetBuilder().buildPreset(pbd);
                    // remember flight data
                    plugin.getTrackerKeeper().getFlightData().put(uuid, bd);
                    long delay = (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) ? 0L : 500L;
                    // flight mode
                    if (flight_mode == 2 || flight_mode == 3) {
                        delay += 650L;
                        Runnable runner = (flight_mode == 2) ? new TARDISRegulatorStarter(plugin, player) : new TARDISManualFlightStarter(plugin, player, id);
                        // start the flying mode when demat has finished
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runner, 500L);
                    }
                    final boolean mine_sound = minecart;
                    final Location sound_loc = (preset.equals(PRESET.JUNK_MODE)) ? exit : handbrake;
                    final Location external_sound_loc = exit;
                    final boolean malchk = malfunction;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            BuildData b_data = plugin.getTrackerKeeper().getFlightData().get(uuid);
                            Location final_location = b_data.getLocation();
                            final Location l = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                            plugin.getPresetBuilder().buildPreset(b_data);
                            if (!mine_sound) {
                                if (!preset.equals(PRESET.JUNK_MODE)) {
                                    if (!malchk) {
                                        TARDISSounds.playTARDISSound(sound_loc, "tardis_land");
                                        TARDISSounds.playTARDISSound(external_sound_loc, "tardis_land");
                                    }
                                } else {
                                    TARDISSounds.playTARDISSound(sound_loc, "junk_land");
                                }
                            } else {
                                handbrake.getWorld().playSound(handbrake, Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                            }
                            final HashMap<String, Object> setcurrent = new HashMap<String, Object>();
                            final HashMap<String, Object> wherecurrent = new HashMap<String, Object>();
                            final HashMap<String, Object> setback = new HashMap<String, Object>();
                            final HashMap<String, Object> whereback = new HashMap<String, Object>();
                            final HashMap<String, Object> setdoor = new HashMap<String, Object>();
                            final HashMap<String, Object> wheredoor = new HashMap<String, Object>();
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
                                        TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, "travel", 1);
                                        taf.doAchievement(distance);
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
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("lastuse", now);
                    HashMap<String, Object> whereh = new HashMap<String, Object>();
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
        while (!b.getType().equals(Material.BEACON)) {
            b = b.getRelative(BlockFace.DOWN);
        }
        TARDISBlockSetters.setBlockAndRemember(b.getRelative(BlockFace.UP), Material.STAINED_GLASS, (byte) 14, id, 2);
    }
}
