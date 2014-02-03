/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * The Hostile Action Displacement System, or HADS, was one of the defence
 * mechanisms of the Doctor's TARDIS. When the outer shell of the vessel came
 * under attack, the unit dematerialised the TARDIS and re-materialised it a
 * short distance away after the attacker had gone, in a safer locale. The HADS
 * had to be manually set, and the Doctor often forgot to do so.
 *
 * @author eccentric_nz
 */
public class TARDISHostileDisplacement {

    private final TARDIS plugin;
    private final List<Integer> angles;

    public TARDISHostileDisplacement(TARDIS plugin) {
        this.angles = Arrays.asList(new Integer[]{0, 45, 90, 135, 180, 225, 270, 315});
        this.plugin = plugin;
    }

    public void moveTARDIS(final int id, Player hostile) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            //String current = rs.getCurrent();
            String owner = rs.getOwner();
            final boolean cham = rs.isChamele_on();
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player", owner);
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
            if (rsp.resultSet()) {
                if (rsp.isHadsOn()) {
                    TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                    int r = plugin.getConfig().getInt("preferences.hads_distance");
                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                    wherecl.put("tardis_id", id);
                    final ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (!rsc.resultSet()) {
                        plugin.debug("Could not get current TARDIS location for HADS!");
                    }
                    final Location loc = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                    final COMPASS d = rsc.getDirection();
                    Location l = loc.clone();
                    // randomise the direction
                    Collections.shuffle(angles);
                    for (Integer a : angles) {
                        int wx = (int) (l.getX() + r * Math.cos(a)); // x = cx + r * cos(a)
                        int wz = (int) (l.getZ() + r * Math.sin(a)); // z = cz + r * sin(a)
                        l.setX(wx);
                        l.setZ(wz);
                        boolean bool = true;
                        int y;
                        if (l.getWorld().getEnvironment().equals(Environment.NETHER)) {
                            y = getHighestNetherBlock(l.getWorld(), wx, wz);
                        } else {
                            y = l.getWorld().getHighestBlockAt(l).getY();
                        }
                        l.setY(y);
                        if (l.getBlock().getRelative(BlockFace.DOWN).isLiquid() && !plugin.getConfig().getBoolean("travel.land_on_water") && !rsc.isSubmarine()) {
                            bool = false;
                        }
                        final Player player = plugin.getServer().getPlayer(owner);
                        if (bool) {
                            Location sub = null;
                            boolean safe;
                            if (rsc.isSubmarine()) {
                                sub = tt.submarine(l.getBlock(), d);
                                safe = (sub != null);
                            } else {
                                int[] start = tt.getStartLocation(l, d);
                                safe = (tt.safeLocation(start[0], y, start[2], start[1], start[3], l.getWorld(), d) < 1);
                            }
                            if (safe) {
                                final Location fl = (rsc.isSubmarine()) ? sub : l;
                                if (plugin.getPluginRespect().getRespect(player, fl, false)) {
                                    // set current
                                    QueryFactory qf = new QueryFactory(plugin);
                                    HashMap<String, Object> tid = new HashMap<String, Object>();
                                    tid.put("tardis_id", id);
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    set.put("world", fl.getWorld().getName());
                                    set.put("x", fl.getBlockX());
                                    set.put("y", fl.getBlockY());
                                    set.put("z", fl.getBlockZ());
                                    set.put("submarine", (rsc.isSubmarine()) ? 1 : 0);
                                    qf.doUpdate("current", set, tid);
                                    plugin.getTrackerKeeper().getTrackDamage().remove(Integer.valueOf(id));
                                    final boolean mat = plugin.getConfig().getBoolean("police_box.materialise");
                                    long delay = (mat) ? 1L : 180L;
                                    // move TARDIS
                                    plugin.getTrackerKeeper().getTrackInVortex().add(Integer.valueOf(id));
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            plugin.getTrackerKeeper().getTrackDematerialising().add(Integer.valueOf(id));
                                            plugin.getPresetDestroyer().destroyPreset(loc, d, id, false, mat, cham, player, rsc.isSubmarine());
                                        }
                                    }, delay);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            plugin.getPresetBuilder().buildPreset(id, fl, d, cham, player, false, false, rsc.isSubmarine());
                                        }
                                    }, delay * 2);
                                    // message time lord
                                    String message = plugin.getPluginName() + ChatColor.RED + "H" + ChatColor.RESET + "ostile " + ChatColor.RED + "A" + ChatColor.RESET + "ction " + ChatColor.RED + "D" + ChatColor.RESET + "isplacement " + ChatColor.RED + "S" + ChatColor.RESET + "ystem engaged, moving TARDIS!";
                                    player.sendMessage(message);
                                    String hads = fl.getWorld().getName() + ":" + fl.getBlockX() + ":" + fl.getBlockY() + ":" + fl.getBlockZ();
                                    player.sendMessage(plugin.getPluginName() + "TARDIS moved to " + hads);
                                    if (player != hostile) {
                                        hostile.sendMessage(message);
                                    }
                                    break;
                                } else {
                                    player.sendMessage(plugin.getPluginName() + "HADS could not be engaged because the area is protected!");
                                    if (player != hostile) {
                                        hostile.sendMessage(plugin.getPluginName() + "HADS could not be engaged because the area is protected!");
                                    }
                                }
                            } else {
                                player.sendMessage(plugin.getPluginName() + "HADS could not be engaged because the it couldn't find a safe area!");
                            }
                        } else {
                            plugin.getTrackerKeeper().getTrackDamage().remove(Integer.valueOf(id));
                            player.sendMessage(plugin.getPluginName() + "HADS could not be engaged because the TARDIS cannot land on water!");
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private int getHighestNetherBlock(World w, int wherex, int wherez) {
        int y = 100;
        Block startBlock = w.getBlockAt(wherex, y, wherez);
        while (startBlock.getTypeId() != 0) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
        }
        int air = 0;
        while (startBlock.getTypeId() == 0 && startBlock.getLocation().getBlockY() > 30) {
            startBlock = startBlock.getRelative(BlockFace.DOWN);
            air++;
        }
        int id = startBlock.getTypeId();
        if ((id == 87 || id == 88 || id == 89 || id == 112 || id == 113 || id == 114) && air >= 4) {
            y = startBlock.getLocation().getBlockY() + 1;
        }
        return y;
    }
}
