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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.messaging;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.companionGUI.TARDISCompanionInventory;
import me.eccentric_nz.tardis.database.data.Area;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetAreas;
import me.eccentric_nz.tardis.database.resultset.ResultSetDestinations;
import me.eccentric_nz.tardis.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.WorldManager;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * The Zygons are a race of metamorphic humanoids. They originated from the planet Zygor, but often tried to migrate
 * away from it.
 *
 * @author eccentric_nz
 */
public class TARDISLister {

	private final TARDISPlugin plugin;

	public TARDISLister(TARDISPlugin plugin) {
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
			Set<String> therechargers = Objects.requireNonNull(TARDISPlugin.plugin.getConfig().getConfigurationSection("rechargers")).getKeys(false);
			if (therechargers.size() < 1) {
				TARDISMessage.send(player, "CHARGER_NONE");
			}
			TARDISMessage.message(player, ChatColor.GOLD + "" + ChatColor.UNDERLINE + "TARDIS Rechargers");
			TARDISMessage.message(player, "Hover to see location (world x, y, z)");
			if (TARDISPermission.hasPermission(player, "tardis.admin")) {
				TARDISMessage.message(player, "Click to /tardisteleport");
			}
			TARDISMessage.message(player, "");
			int n = 1;
			for (String s : therechargers) {
				// only list public rechargers
				if (!s.startsWith("rift")) {
					String w;
					if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
						w = plugin.getMVHelper().getAlias(TARDISPlugin.plugin.getConfig().getString("rechargers." + s + ".world"));
					} else {
						w = TARDISAliasResolver.getWorldAlias(TARDISPlugin.plugin.getConfig().getString("rechargers." + s + ".world"));
					}
					String x = TARDISPlugin.plugin.getConfig().getString("rechargers." + s + ".x");
					String y = TARDISPlugin.plugin.getConfig().getString("rechargers." + s + ".y");
					String z = TARDISPlugin.plugin.getConfig().getString("rechargers." + s + ".z");
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
			ResultSetAreas rsa = new ResultSetAreas(TARDISPlugin.plugin, null, true, false);
			int n = 1;
			if (!rsa.resultSet()) {
				TARDISMessage.send(player, "AREA_NONE");
			}
			for (Area a : rsa.getData()) {
				if (n == 1) {
					TARDISMessage.send(player, "AREAS");
					TARDISMessage.message(player, "");
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
			ResultSetTardis rst = new ResultSetTardis(TARDISPlugin.plugin, where, "", false, 0);
			if (rst.resultSet()) {
				TARDIS tardis = rst.getTardis();
				int id = tardis.getTardisId();
				// list TARDIS saves
				if (list.equalsIgnoreCase("saves")) {
					TARDISMessage.message(player, ChatColor.GOLD + "" + ChatColor.UNDERLINE + "TARDIS " + plugin.getLanguage().getString("SAVES"));
					TARDISMessage.message(player, "Hover to see location (world x, y, z)");
					TARDISMessage.message(player, "Click to /tardistravel");
					TARDISMessage.message(player, "");
					// get home
					HashMap<String, Object> wherehl = new HashMap<>();
					wherehl.put("tardis_id", id);
					ResultSetHomeLocation rsh = new ResultSetHomeLocation(TARDISPlugin.plugin, wherehl);
					rsh.resultSet();
					String homeWorld = (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(rsh.getWorld()) : TARDISAliasResolver.getWorldAlias(rsh.getWorld());
					TextComponent tch = new TextComponent(plugin.getLanguage().getString("HOME"));
					tch.setColor(ChatColor.GREEN);
					tch.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.format("%s %s, %s, %s", homeWorld, rsh.getX(), rsh.getY(), rsh.getZ()))));
					tch.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardistravel home"));
					player.spigot().sendMessage(tch);
					// list other saved destinations
					HashMap<String, Object> whered = new HashMap<>();
					whered.put("tardis_id", id);
					ResultSetDestinations rsd = new ResultSetDestinations(TARDISPlugin.plugin, whered, true);
					if (rsd.resultSet()) {
						ArrayList<HashMap<String, String>> data = rsd.getData();
						for (HashMap<String, String> map : data) {
							if (map.get("type").equals("0")) {
								String world = (plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(map.get("world")) : TARDISAliasResolver.getWorldAlias(map.get("world"));
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
						TARDISMessage.send(player, "COMPANIONS_NONE");
					}
				}
			}
		}
	}
}
