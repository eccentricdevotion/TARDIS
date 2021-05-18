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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.sudo;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TARDISSudoTracker {

	public static final HashMap<UUID, UUID> SUDOERS = new HashMap<>();
	public static final UUID CONSOLE_UUID = UUID.randomUUID();

	public static boolean isSudo(CommandSender sender) {
		return SUDOERS.containsKey(getSudo(sender));
	}

	public static UUID getSudoPlayer(CommandSender sender) {
		return SUDOERS.get(getSudo(sender));
	}

	public static UUID getSudo(CommandSender sender) {
		UUID uuid;
		Player player;
		if (sender instanceof Player) {
			player = (Player) sender;
			uuid = player.getUniqueId();
		} else {
			// console
			uuid = CONSOLE_UUID;
		}
		return uuid;
	}
}
