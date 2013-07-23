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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
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
    private List<Integer> angles = Arrays.asList(new Integer[]{0, 45, 90, 135, 180, 225, 270, 315});

    public TARDISHostileDisplacement(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void moveTARDIS(final int id, Player hostile) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            String current = rs.getCurrent();
            String owner = rs.getOwner();
            final TARDISConstants.COMPASS d = rs.getDirection();
            final boolean cham = rs.isChamele_on();
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player", owner);
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
            if (rsp.resultSet()) {
                if (rsp.isHads_on()) {
                    TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                    int r = plugin.getConfig().getInt("hads_distance");
                    final Location loc = plugin.utils.getLocationFromDB(current, 0, 0);
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
                        if (l.getBlock().getRelative(BlockFace.DOWN).isLiquid() && !plugin.getConfig().getBoolean("land_on_water") && !plugin.trackSubmarine.contains(id)) {
                            bool = false;
                        }
                        final Player player = plugin.getServer().getPlayer(owner);
                        if (bool) {
                            boolean safe;
                            if (plugin.trackSubmarine.contains(id)) {
                                Location sub = tt.submarine(l.getBlock(), d);
                                safe = tt.isSafeSubmarine(sub, d);
                            } else {
                                int[] start = tt.getStartLocation(l, d);
                                safe = (tt.safeLocation(start[0], y, start[2], start[1], start[3], l.getWorld(), d) < 1);
                            }
                            if (safe) {
                                final Location fl = l;
                                TARDISPluginRespect pr = new TARDISPluginRespect(plugin);
                                if (pr.getRespect(player, l, false)) {
                                    // move TARDIS
                                    String hads = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                                    QueryFactory qf = new QueryFactory(plugin);
                                    HashMap<String, Object> tid = new HashMap<String, Object>();
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    tid.put("tardis_id", id);
                                    set.put("save", hads);
                                    set.put("current", hads);
                                    qf.doUpdate("tardis", set, tid);
                                    plugin.trackDamage.remove(Integer.valueOf(id));
                                    final boolean mat = plugin.getConfig().getBoolean("materialise");
                                    long delay = (mat) ? 1L : 180L;
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            plugin.tardisDematerialising.add(id);
                                            plugin.destroyPB.destroyPoliceBox(loc, d, id, false, mat, cham, player);
                                        }
                                    }, delay);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            plugin.buildPB.buildPoliceBox(id, fl, d, cham, player, false, false);
                                        }
                                    }, delay * 2);
                                    // message time lord
                                    String message = plugin.pluginName + ChatColor.RED + "H" + ChatColor.RESET + "ostile " + ChatColor.RED + "A" + ChatColor.RESET + "ction " + ChatColor.RED + "D" + ChatColor.RESET + "isplacement " + ChatColor.RED + "S" + ChatColor.RESET + "ystem engaged, moving TARDIS!";
                                    player.sendMessage(message);
                                    player.sendMessage(plugin.pluginName + "TARDIS moved to " + hads);
                                    if (player != hostile) {
                                        hostile.sendMessage(message);
                                    }
                                    break;
                                } else {
                                    player.sendMessage(plugin.pluginName + "HADS could not be engaged because the area is protected!");
                                    if (player != hostile) {
                                        hostile.sendMessage(plugin.pluginName + "HADS could not be engaged because the area is protected!");
                                    }
                                }
                            }
                        } else {
                            plugin.trackDamage.remove(Integer.valueOf(id));
                            player.sendMessage(plugin.pluginName + "HADS could not be engaged because the TARDIS cannot land on water!");
                        }
                    }
                }
            }
        }
    }

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
