/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionInventory;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
     * @param player an instance of a player.
     * @param list   is the String name of the list type to retrieve. Possible values are areas, saves, rechargers and
     *               companions.
     */
    public void list(Player player, String list) {
        if (list.equals("rechargers")) {
            Set<String> therechargers = TARDIS.plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
            if (therechargers.size() < 1) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CHARGER_NONE");
            }
            plugin.getMessenger().messageWithColour(player, "TARDIS Rechargers", "#FFAA00");
            plugin.getMessenger().message(player, "Hover to see location (world x, y, z)");
            if (TARDISPermission.hasPermission(player, "tardis.admin")) {
                plugin.getMessenger().message(player, "Click to /tardisteleport");
            }
            plugin.getMessenger().message(player, "");
            int n = 1;
            for (String s : therechargers) {
                // only list public rechargers
                if (!s.startsWith("rift")) {
                    String world = TARDIS.plugin.getConfig().getString("rechargers." + s + ".world");
                    String w;
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                        w = plugin.getMVHelper().getAlias(world);
                    } else {
                        w = TARDISAliasResolver.getWorldAlias(world);
                    }
                    String x = TARDIS.plugin.getConfig().getString("rechargers." + s + ".x");
                    String y = TARDIS.plugin.getConfig().getString("rechargers." + s + ".y");
                    String z = TARDIS.plugin.getConfig().getString("rechargers." + s + ".z");
                    // TODO
                    TextComponent tcr = new TextComponent(n + ". " + s);
                    tcr.setColor(ChatColor.GREEN);
                    tcr.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("%s %s, %s, %s", w, x, y, z))));
                    if (TARDISPermission.hasPermission(player, "tardis.admin")) {
                        tcr.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tardisteleport %s %s %s not_for_players", w, x, z)));
                    }
                    player.spigot().sendMessage(tcr);
                    n++;
                }
            }
        }
        if (list.equals("areas")) {
            ResultSetAreas rsa = new ResultSetAreas(TARDIS.plugin, null, true, false);
            int n = 1;
            if (!rsa.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NONE");
            }
            for (Area a : rsa.getData()) {
                if (n == 1) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "AREAS");
                    plugin.getMessenger().message(player, "");
                }
                TextComponent tca = new TextComponent(n + ". [" + a.getAreaName() + "] in world: " + a.getWorld());
                if (TARDISPermission.hasPermission(player, "tardis.area." + a.getAreaName()) || TARDISPermission.hasPermission(player, "tardis.area.*")) {
                    tca.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to /tardistravel here")));
                    tca.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tardistravel area %s", a.getAreaName())));
                }
                player.spigot().sendMessage(tca);
                n++;
            }
        } else {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rst = new ResultSetTardis(TARDIS.plugin, where, "", false, 0);
            if (rst.resultSet()) {
                Tardis tardis = rst.getTardis();
                int id = tardis.getTardis_id();
                // list TARDIS saves
                if (list.equalsIgnoreCase("saves")) {
                    plugin.getMessenger().messageWithColour(player, "TARDIS " + plugin.getLanguage().getString("SAVES"), "#FFAA00");
                    plugin.getMessenger().message(player, "Hover to see location (world x, y, z)");
                    plugin.getMessenger().message(player, "Click to /tardistravel");
                    plugin.getMessenger().message(player, "");
                    // get home
                    HashMap<String, Object> wherehl = new HashMap<>();
                    wherehl.put("tardis_id", id);
                    ResultSetHomeLocation rsh = new ResultSetHomeLocation(TARDIS.plugin, wherehl);
                    rsh.resultSet();
                    String homeWorld = (!plugin.getPlanetsConfig().getBoolean("planets." + rsh.getWorld().getName() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(rsh.getWorld()) : TARDISAliasResolver.getWorldAlias(rsh.getWorld());
                    // TODO
                    TextComponent tch = new TextComponent(plugin.getLanguage().getString("HOME"));
                    tch.setColor(ChatColor.GREEN);
                    tch.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("%s %s, %s, %s", homeWorld, rsh.getX(), rsh.getY(), rsh.getZ()))));
                    tch.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardistravel home"));
                    player.spigot().sendMessage(tch);
                    // list other saved destinations
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("tardis_id", id);
                    ResultSetDestinations rsd = new ResultSetDestinations(TARDIS.plugin, whered, true);
                    if (rsd.resultSet()) {
                        ArrayList<HashMap<String, String>> data = rsd.getData();
                        for (HashMap<String, String> map : data) {
                            if (map.get("type").equals("0")) {
                                String world = (!plugin.getPlanetsConfig().getBoolean("planets." + map.get("world") + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(map.get("world")) : TARDISAliasResolver.getWorldAlias(map.get("world"));
                                TextComponent tcd = new TextComponent(map.get("dest_name"));
                                tcd.setColor(ChatColor.GREEN);
                                tcd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("%s %s, %s, %s", world, map.get("x"), map.get("y"), map.get("z")))));
                                tcd.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardistravel save " + map.get("dest_name")));
                                player.spigot().sendMessage(tcd);
                            }
                        }
                    }
                }
                if (list.equalsIgnoreCase("companions")) {
                    // list companions
                    String comps = tardis.getCompanions();
                    if (comps != null && !comps.isEmpty()) {
                        String[] companionData = comps.split(":");
                        ItemStack[] heads = new TARDISCompanionInventory(plugin, companionData).getSkulls();
                        // open the GUI
                        Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Companions");
                        inv.setContents(heads);
                        player.openInventory(inv);
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "COMPANIONS_NONE");
                    }
                }
            }
        }
    }
}
