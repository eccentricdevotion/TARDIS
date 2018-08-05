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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionInventory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.WORLD_MANAGER;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * The Zygons are a race of metamorphic humanoids. They originated from the planet Zygor, but often tried to migrate
 * away from it.
 *
 * @author eccentric_nz
 */
public class TARDISLister {

    private final TARDIS plugin;

    public TARDISLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Retrieves various lists from the database.
     *
     * @param p an instance of a player.
     * @param l is the String name of the list type to retrieve. Possible values are areas, saves, rechargers and
     *          companions.
     */
    public void list(Player p, String l) {
        if (l.equals("rechargers")) {
            Set<String> therechargers = TARDIS.plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
            if (therechargers.size() < 1) {
                TARDISMessage.send(p, "CHARGER_NONE");
            }
            int a = 1;

            for (String s : therechargers) {
                // only list public rechargers
                if (!s.startsWith("rift")) {
                    if (a == 1) {
                        TARDISMessage.send(p, "CHARGERS");
                    }
                    String w;
                    if (plugin.getWorldManager().equals(WORLD_MANAGER.MULTIVERSE)) {
                        w = plugin.getMVHelper().getAlias(TARDIS.plugin.getConfig().getString("rechargers." + s + ".world"));
                    } else {
                        w = TARDIS.plugin.getConfig().getString("rechargers." + s + ".world");
                    }
                    int x = TARDIS.plugin.getConfig().getInt("rechargers." + s + ".x");
                    int y = TARDIS.plugin.getConfig().getInt("rechargers." + s + ".y");
                    int z = TARDIS.plugin.getConfig().getInt("rechargers." + s + ".z");
                    p.sendMessage(a + ". [" + s + "] in world: " + w + ", at " + x + ":" + y + ":" + z);
                    a++;
                }
            }
        }
        if (l.equals("areas")) {
            ResultSetAreas rsa = new ResultSetAreas(TARDIS.plugin, null, true, false);
            int n = 1;
            if (!rsa.resultSet()) {
                TARDISMessage.send(p, "AREA_NONE");
            }
            for (Area a : rsa.getData()) {
                if (n == 1) {
                    TARDISMessage.send(p, "AREAS");
                }
                p.sendMessage(n + ". [" + a.getAreaName() + "] in world: " + a.getWorld());
                n++;
            }
        } else {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", p.getUniqueId().toString());
            ResultSetTardis rst = new ResultSetTardis(TARDIS.plugin, where, "", false, 0);
            if (rst.resultSet()) {
                Tardis tardis = rst.getTardis();
                int id = tardis.getTardis_id();
                // list TARDIS saves
                if (l.equalsIgnoreCase("saves")) {
                    // get home
                    HashMap<String, Object> wherehl = new HashMap<>();
                    wherehl.put("tardis_id", id);
                    ResultSetHomeLocation rsh = new ResultSetHomeLocation(TARDIS.plugin, wherehl);
                    rsh.resultSet();
                    p.sendMessage(ChatColor.GRAY + plugin.getLanguage().getString("SAVES"));
                    p.sendMessage(ChatColor.GREEN + plugin.getLanguage().getString("HOME") + ": " + rsh.getWorld().getName() + " at x:" + rsh.getX() + " y:" + rsh.getY() + " z:" + rsh.getZ());
                    // list other saved destinations
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("tardis_id", id);
                    ResultSetDestinations rsd = new ResultSetDestinations(TARDIS.plugin, whered, true);
                    int i = 1;
                    if (rsd.resultSet()) {
                        ArrayList<HashMap<String, String>> data = rsd.getData();
                        for (HashMap<String, String> map : data) {
                            if (i == 1) {
                                p.sendMessage(ChatColor.GRAY + "----------------");
                            }
                            String type = map.get("type");
                            if (type.equals("0")) {
                                String dn = map.get("dest_name");
                                p.sendMessage(ChatColor.GREEN + "" + i + ". [" + dn + "]: " + map.get("world") + " at x:" + map.get("x") + " y:" + map.get("y") + " z:" + map.get("z"));
                                i++;
                            }
                        }
                    }
                }
                if (l.equalsIgnoreCase("companions")) {
                    // list companions
                    String comps = tardis.getCompanions();
                    if (comps != null && !comps.isEmpty()) {
                        String[] companionData = comps.split(":");
                        ItemStack[] heads = new TARDISCompanionInventory(plugin, companionData).getSkulls();
                        // open the GUI
                        Inventory inv = plugin.getServer().createInventory(p, 54, "§4Companions");
                        inv.setContents(heads);
                        p.openInventory(inv);
                    } else {
                        TARDISMessage.send(p, "COMPANIONS_NONE");
                    }
                }
            }
        }
    }
}
