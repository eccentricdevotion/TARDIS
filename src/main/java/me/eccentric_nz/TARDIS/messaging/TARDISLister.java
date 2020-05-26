/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.messaging;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionInventory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.WORLD_MANAGER;
import me.eccentric_nz.TARDIS.messaging.TableGenerator.Alignment;
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
            TableGenerator tg = new TableGenerator(Alignment.LEFT, Alignment.LEFT, Alignment.RIGHT, Alignment.RIGHT, Alignment.RIGHT);
            tg.addRow(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "TARDIS Rechargers", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "World", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "X", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Y", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Z");
            tg.addRow();
            for (String s : therechargers) {
                // only list public rechargers
                if (!s.startsWith("rift")) {
                    String w;
                    if (plugin.getWorldManager().equals(WORLD_MANAGER.MULTIVERSE)) {
                        w = plugin.getMVHelper().getAlias(TARDIS.plugin.getConfig().getString("rechargers." + s + ".world"));
                    } else {
                        w = TARDIS.plugin.getConfig().getString("rechargers." + s + ".world");
                    }
                    String x = TARDIS.plugin.getConfig().getString("rechargers." + s + ".x");
                    String y = TARDIS.plugin.getConfig().getString("rechargers." + s + ".y");
                    String z = TARDIS.plugin.getConfig().getString("rechargers." + s + ".z");
                    tg.addRow(ChatColor.GREEN + s + ChatColor.RESET, w, x, y, z);
                }
            }
            for (String line : tg.generate(TableGenerator.Receiver.CLIENT, true, true)) {
                p.sendMessage(line);
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
                    TableGenerator tg = new TableGenerator(Alignment.LEFT, Alignment.LEFT, Alignment.RIGHT, Alignment.RIGHT, Alignment.RIGHT);
                    tg.addRow(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "TARDIS " + plugin.getLanguage().getString("SAVES"), ChatColor.GOLD + "" + ChatColor.UNDERLINE + "World", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "X", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Y", ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Z");
                    tg.addRow();
                    // get home
                    HashMap<String, Object> wherehl = new HashMap<>();
                    wherehl.put("tardis_id", id);
                    ResultSetHomeLocation rsh = new ResultSetHomeLocation(TARDIS.plugin, wherehl);
                    rsh.resultSet();
                    tg.addRow(ChatColor.GREEN + plugin.getLanguage().getString("HOME") + ChatColor.RESET, rsh.getWorld().getName(), "" + rsh.getX(), "" + rsh.getY(), "" + rsh.getZ());
                    tg.addRow();
                    // list other saved destinations
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("tardis_id", id);
                    ResultSetDestinations rsd = new ResultSetDestinations(TARDIS.plugin, whered, true);
                    if (rsd.resultSet()) {
                        ArrayList<HashMap<String, String>> data = rsd.getData();
                        for (HashMap<String, String> map : data) {
                            if (map.get("type").equals("0")) {
                                tg.addRow(ChatColor.GREEN + map.get("dest_name") + ChatColor.RESET, map.get("world"), "" + map.get("x"), "" + map.get("y"), "" + map.get("z"));
                            }
                        }
                    }
                    for (String line : tg.generate(TableGenerator.Receiver.CLIENT, true, true)) {
                        p.sendMessage(line);
                    }
                }
                if (l.equalsIgnoreCase("companions")) {
                    // list companions
                    String comps = tardis.getCompanions();
                    if (comps != null && !comps.isEmpty()) {
                        String[] companionData = comps.split(":");
                        ItemStack[] heads = new TARDISCompanionInventory(plugin, companionData).getSkulls();
                        // open the GUI
                        Inventory inv = plugin.getServer().createInventory(p, 54, ChatColor.DARK_RED + "Companions");
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
