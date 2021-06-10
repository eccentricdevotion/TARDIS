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
package me.eccentric_nz.tardis.commands.config;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TARDISDefaultCommand {

	private final TARDISPlugin plugin;

	TARDISDefaultCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	boolean setDefaultItem(CommandSender sender, String[] args) {
		String which = args[0].toLowerCase(Locale.ENGLISH);
		int count = args.length;
		StringBuilder buf = new StringBuilder();
		for (int i = 1; i < count; i++) {
			buf.append(args[i]).append("_");
		}
		String tmp = buf.toString();
		String sonic = tmp.substring(0, tmp.length() - 1);
		plugin.getConfig().set("preferences." + which, sonic);
		plugin.saveConfig();
		TARDISMessage.send(sender, "CONFIG_UPDATED");
		TARDISMessage.send(sender, "RESTART");
		return true;
	}
}
