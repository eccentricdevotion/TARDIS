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
package me.eccentric_nz.tardis.commands.admin;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author eccentric_nz
 */
class TARDISConfigCommand {

	private final TARDISPlugin plugin;
	private final List<String> sections = new ArrayList<>();

	TARDISConfigCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
		sections.add("storage");
		sections.add("creation");
		sections.add("police_box");
		sections.add("travel");
		sections.add("preferences");
		sections.add("allow");
		sections.add("growth");
	}

	boolean showConfigOptions(CommandSender sender, String[] args) {
		String section = args[1].toLowerCase(Locale.ENGLISH);
		TARDISMessage.send(sender, "CONFIG");
		if (sections.contains(section)) {
			sender.sendMessage(ChatColor.AQUA + section + ":" + ChatColor.RESET);
			Set<String> options = plugin.getConfig().getConfigurationSection(section).getKeys(false);
			options.forEach((o) -> {
				if (o.equals("mysql")) {
					sender.sendMessage("    " + ChatColor.GREEN + o + "mysql:");
					sender.sendMessage("        " + ChatColor.GREEN + o + "url: " + plugin.getConfig().getString(section + ".mysql.url"));
					sender.sendMessage("        " + ChatColor.GREEN + o + "user: " + plugin.getConfig().getString(section + ".mysql.user"));
					sender.sendMessage("        " + ChatColor.GREEN + o + "password: " + plugin.getConfig().getString(section + ".mysql.password"));
				} else {
					sender.sendMessage("    " + ChatColor.GREEN + o + ": " + plugin.getConfig().getString(section + "." + o));
				}
			});
			return true;
		}
		if (section.equals("worlds")) {
			sender.sendMessage(ChatColor.AQUA + section + ":" + ChatColor.RESET);
			Set<String> worldNames = plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false);
			worldNames.forEach((wname) -> {
				String enabled = plugin.getPlanetsConfig().getString("planets." + wname + ".time_travel");
				sender.sendMessage("    " + ChatColor.GREEN + wname + ": " + ChatColor.RESET + enabled);
			});
			return true;
		}
		if (section.equals("rechargers")) {
			sender.sendMessage(ChatColor.AQUA + section + ":" + ChatColor.RESET);
			Set<String> chargerNames = plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
			chargerNames.forEach((charname) -> {
				sender.sendMessage("    " + ChatColor.GREEN + charname + ":");
				sender.sendMessage("        world: " + plugin.getConfig().getString("rechargers." + charname + ".world"));
				sender.sendMessage("        x: " + plugin.getConfig().getString("rechargers." + charname + ".x"));
				sender.sendMessage("        y: " + plugin.getConfig().getString("rechargers." + charname + ".y"));
				sender.sendMessage("        z: " + plugin.getConfig().getString("rechargers." + charname + ".z"));
			});
			return true;
		}
		if (section.equals("rooms")) {
			sender.sendMessage(ChatColor.AQUA + section + ":" + ChatColor.RESET);
			Set<String> roomNames = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
			roomNames.forEach((r) -> {
				sender.sendMessage("    " + ChatColor.GREEN + r + ":");
				sender.sendMessage("        enabled: " + plugin.getRoomsConfig().getString("rooms." + r + ".enabled"));
				sender.sendMessage("        cost: " + plugin.getRoomsConfig().getString("rooms." + r + ".cost"));
				sender.sendMessage("        offset: " + plugin.getRoomsConfig().getString("rooms." + r + ".offset"));
				sender.sendMessage("        seed: " + plugin.getRoomsConfig().getString("rooms." + r + ".seed"));
			});
			return true;
		}
		return false;
	}
}
