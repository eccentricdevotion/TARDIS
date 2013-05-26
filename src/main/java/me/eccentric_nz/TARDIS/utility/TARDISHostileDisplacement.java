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
            final TARDISConstants.COMPASS d = rs.getDirection();
            final boolean cham = rs.isChamele_on();
            HashMap<String, Object> wherep = new HashMap<String, Object>();
            wherep.put("player", rs.getOwner());
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
                        l.setX(l.getX() + r * Math.cos(a)); // x = cx + r * cos(a)
                        l.setZ(l.getZ() + r * Math.sin(a)); // z = cz + r * sin(a)
                        int y = l.getWorld().getHighestBlockAt(l).getY();
                        l.setY(y);
                        int[] start = tt.getStartLocation(l, d);
                        if (tt.safeLocation(start[0], start[1], start[2], start[3], start[4], l.getWorld(), d) < 1) {
                            final Location fl = l;
                            final Player player = plugin.getServer().getPlayer(rsp.getPlayer());
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
                                long delay = (plugin.getConfig().getBoolean("materialise")) ? 1L : 180L;
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        plugin.tardisDematerialising.add(id);
                                        plugin.destroyPB.destroyPoliceBox(loc, d, id, false, plugin.getConfig().getBoolean("materialise"), cham, player);
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
                    }
                }
            }
        }
    }
}
