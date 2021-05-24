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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.utility.TARDISUpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISVersionCommand {

	private final TARDISPlugin plugin;

	TARDISVersionCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	boolean displayVersion(CommandSender sender) {
		String pluginName = plugin.getPluginName();
		List<String> hooks = plugin.getDescription().getSoftDepend();
		String tardisversion = plugin.getDescription().getVersion();
		String chunkversion = plugin.getPM().getPlugin("TARDISChunkGenerator").getDescription().getVersion();
		String cb = Bukkit.getVersion();
		// send server and tardis versions
		sender.sendMessage(pluginName + "Server version: " + ChatColor.AQUA + cb);
		sender.sendMessage(pluginName + "tardis version: " + ChatColor.AQUA + tardisversion);
		sender.sendMessage(pluginName + "TARDISChunkGenerator version: " + ChatColor.AQUA + chunkversion);
		// send dependent plugin versions
		for (Plugin hook : plugin.getPM().getPlugins()) {
			PluginDescriptionFile desc = hook.getDescription();
			String name = desc.getName();
			String version = desc.getVersion();
			if (hooks.contains(name)) {
				sender.sendMessage(pluginName + name + " version: " + ChatColor.AQUA + version);
			}
		}
		// check for new tardis build
		if (sender.isOp()) {
			sender.sendMessage(pluginName + "Checking for new tardis builds...");
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new TARDISUpdateChecker(plugin, sender));
		}
		return true;
	}
}
