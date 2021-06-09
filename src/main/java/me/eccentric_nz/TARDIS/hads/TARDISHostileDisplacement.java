/*
 * Copyright (C) 2021 eccentric_nz
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
 * along with this program. If ChatColor.RESET, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.hads;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISHADSEvent;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISBiome;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * The Hostile Action Displacement System, or HADS, was one of the defence mechanisms of the Doctor's TARDIS. When the
 * outer shell of the vessel came under attack, the unit dematerialised the TARDIS and re-materialised it a short
 * distance away after the attacker had gone, in a safer locale. The HADS had to be manually set, and the Doctor often
 * forgot to do so.
 *
 * @author eccentric_nz
 */
class TARDISHostileDisplacement {

    private final TARDIS plugin;
    private final List<Integer> angles;
    private int count = 0;

    TARDISHostileDisplacement(TARDIS plugin) {
        angles = Arrays.asList(0, 45, 90, 135, 180, 225, 270, 315);
        this.plugin = plugin;
    }

    void moveTARDIS(int id, UUID uuid, Player hostile, PRESET preset) {

        TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
        int r = plugin.getConfig().getInt("preferences.hads_distance");
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            plugin.debug("Could not get current TARDIS location for HADS!");
        }
        boolean underwater = rsc.isSubmarine();
        Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        // displace
        COMPASS d = rsc.getDirection();
        Location l = current.clone();
        // randomise the direction
        Collections.shuffle(angles);
        for (Integer a : angles) {
            count++;
            int wx = (int) (l.getX() + r * Math.cos(a)); // x = cx + r * cos(a)
            int wz = (int) (l.getZ() + r * Math.sin(a)); // z = cz + r * sin(a)
            l.setX(wx);
            l.setZ(wz);
            boolean bool = true;
            int y;
            if (l.getWorld().getEnvironment().equals(Environment.NETHER)) {
                y = plugin.getUtils().getHighestNetherBlock(l.getWorld(), wx, wz);
            } else {
                y = TARDISStaticLocationGetters.getHighestYin3x3(l.getWorld(), wx, wz);
            }
            l.setY(y);
            if (l.getBlock().getRelative(BlockFace.DOWN).isLiquid() && !plugin.getConfig().getBoolean("travel.land_on_water") && !rsc.isSubmarine()) {
                bool = false;
            }
            Player player = plugin.getServer().getPlayer(uuid);
            if (bool) {
                Location sub = null;
                boolean safe;
                if (rsc.isSubmarine()) {
                    sub = tt.submarine(l.getBlock(), d);
                    safe = (sub != null);
                } else {
                    int[] start = TARDISTimeTravel.getStartLocation(l, d);
                    safe = (TARDISTimeTravel.safeLocation(start[0], y, start[2], start[1], start[3], l.getWorld(), d) < 1);
                }
                if (safe) {
                    Location fl = (rsc.isSubmarine()) ? sub : l;
                    if (plugin.getPluginRespect().getRespect(fl, new Parameters(player, Flag.getNoMessageFlags()))) {
                        // sound the cloister bell at current location for dematerialisation
                        TARDISCloisterBell bell = new TARDISCloisterBell(plugin, 5, id, current, plugin.getServer().getPlayer(uuid), true, "HADS displacement", false);
                        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
                        bell.setTask(taskID);
                        plugin.getTrackerKeeper().getCloisterBells().put(id, taskID);
                        // sound the cloister bell at HADS location for materialisation
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            TARDISCloisterBell end = new TARDISCloisterBell(plugin, 6, id, fl, plugin.getServer().getPlayer(uuid), false, "", true);
                            int endTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, end, 2L, 70L);
                            bell.setTask(endTask);
                            plugin.getTrackerKeeper().getCloisterBells().put(id, endTask);
                        }, 420L);
                        // set current
                        HashMap<String, Object> tid = new HashMap<>();
                        tid.put("tardis_id", id);
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("world", fl.getWorld().getName());
                        set.put("x", fl.getBlockX());
                        set.put("y", fl.getBlockY());
                        set.put("z", fl.getBlockZ());
                        set.put("submarine", (rsc.isSubmarine()) ? 1 : 0);
                        plugin.getQueryFactory().doUpdate("current", set, tid);
                        long delay = 1L;
                        // move TARDIS
                        plugin.getTrackerKeeper().getInVortex().add(id);
                        DestroyData dd = new DestroyData();
                        dd.setDirection(d);
                        dd.setLocation(current);
                        dd.setPlayer(player);
                        dd.setHide(false);
                        dd.setOutside(true);
                        dd.setSubmarine(rsc.isSubmarine());
                        dd.setTardisID(id);
                        TARDISBiome biome = TARDISStaticUtils.getBiomeAt(current);
                        dd.setThrottle(SpaceTimeThrottle.NORMAL);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            plugin.getTrackerKeeper().getDematerialising().add(id);
                            plugin.getPresetDestroyer().destroyPreset(dd);
                        }, delay);
                        BuildData bd = new BuildData(uuid.toString());
                        bd.setDirection(d);
                        bd.setLocation(fl);
                        bd.setMalfunction(false);
                        bd.setOutside(true);
                        bd.setPlayer(player);
                        bd.setRebuild(false);
                        bd.setSubmarine(rsc.isSubmarine());
                        bd.setTardisID(id);
                        bd.setThrottle(SpaceTimeThrottle.NORMAL);
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), delay * 2);
                        // message time lord
                        String message = plugin.getPluginName() + ChatColor.RED + "H" + ChatColor.RESET + "ostile " + ChatColor.RED + "A" + ChatColor.RESET + "ction " + ChatColor.RED + "D" + ChatColor.RESET + "isplacement " + ChatColor.RED + "S" + ChatColor.RESET + "ystem " + plugin.getLanguage().getString("HADS_ENGAGED");
                        player.sendMessage(message);
                        String hads = fl.getWorld().getName() + ":" + fl.getBlockX() + ":" + fl.getBlockY() + ":" + fl.getBlockZ();
                        TARDISMessage.send(player, "HADS_LOC", hads);
                        if (player != hostile) {
                            hostile.sendMessage(message);
                        }
                        plugin.getPM().callEvent(new TARDISHADSEvent(hostile, id, fl, HADS.DISPLACEMENT));
                        break;
                    } else {
                        TARDISMessage.send(player, "HADS_PROTECTED");
                        if (player != hostile) {
                            TARDISMessage.send(hostile, "HADS_PROTECTED");
                        }
                    }
                } else if (underwater) {
                    TARDISMessage.send(player, "HADS_NOT_SAFE");
                } else if (count > 7) {
                    // only if count is 8 or more
                    // use dispersal instead...
                    new TARDISHostileDispersal(plugin).disperseTARDIS(id, uuid, hostile, preset);
                }
            } else {
                plugin.getTrackerKeeper().getDamage().remove(id);
                TARDISMessage.send(player, "HADS_NO_WATER");
            }
        }
    }
}
