/*
 * Copyright (C) 2012 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.utility;

import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISLister {

    private static TARDISDatabase service = TARDISDatabase.getInstance();

    /**
     * Retrieves various lists from the database.
     *
     * @param p an instance of a player.
     * @param l is the String name of the list type to retrieve. Possible values
     * are areas, saves and companions.
     */
    public static void list(Player p, String l) {
        String playerNameStr = p.getName();
        if (l.equals("areas")) {
            ResultSetAreas rsa = new ResultSetAreas(TARDIS.plugin, null, true);
            int a = 1;
            if (!rsa.resultSet()) {
                p.sendMessage(TARDIS.plugin.pluginName + " No areas were found!");
            }
            ArrayList<HashMap<String, String>> data = rsa.getData();
            for (HashMap<String, String> map : data) {
                String name = map.get("area_name");
                String world = map.get("world");
                if (a == 1) {
                    p.sendMessage(TARDIS.plugin.pluginName + " Areas");
                }
                p.sendMessage(a + ". [" + name + "] in world: " + world);
                a++;
            }
        } else {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", playerNameStr);
            ResultSetTardis rst = new ResultSetTardis(TARDIS.plugin, where, "", false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                // list TARDIS saves
                if (l.equalsIgnoreCase("saves")) {
                    // construct home string
                    String h = rst.getHome();
                    String[] h_data = h.split(":");
                    p.sendMessage(ChatColor.GRAY + "Saves");
                    p.sendMessage(ChatColor.GREEN + "HOME: " + h_data[0] + " at x:" + h_data[1] + " y:" + h_data[2] + " z:" + h_data[3]);
                    // list other saved destinations
                    HashMap<String, Object> whered = new HashMap<String, Object>();
                    whered.put("tardis_id", id);
                    ResultSetDestinations rsd = new ResultSetDestinations(TARDIS.plugin, where, true);
                    int i = 1;
                    if (rsd.resultSet()) {
                        ArrayList<HashMap<String, String>> data = rsd.getData();
                        for (HashMap<String, String> map : data) {
                            if (i == 1) {
                                p.sendMessage(ChatColor.GRAY + "----------------");
                            }
                            p.sendMessage(ChatColor.GREEN + "" + i + ". [" + map.get("dest_name") + "]: " + map.get("world") + " at x:" + map.get("x") + " y:" + map.get("y") + " z:" + map.get("z"));
                            i++;
                        }
                    }
                }
                if (l.equalsIgnoreCase("companions")) {
                    // list companions
                    String comps = rst.getCompanions();
                    if (!comps.equals("")) {
                        String[] companionData = comps.split(":");
                        p.sendMessage(ChatColor.AQUA + "Your TARDIS companions are:");
                        for (String c : companionData) {
                            p.sendMessage(ChatColor.AQUA + c);
                        }
                    } else {
                        p.sendMessage(ChatColor.DARK_BLUE + "You don't have any TARDIS companions yet." + ChatColor.RESET + " Use " + ChatColor.GREEN + "/tardis add [player]" + ChatColor.RESET + " to add some");
                    }
                }
            }
        }
    }
}