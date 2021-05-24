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
import me.eccentric_nz.tardis.database.resultset.ResultSetCount;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.TARDISNumberParsers;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISRepairCommand {

	private final TARDISPlugin plugin;

	TARDISRepairCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	boolean setFreeCount(CommandSender sender, String[] args) {
		if (args.length < 3) {
			TARDISMessage.send(sender, "TOO_FEW_ARGS");
			return true;
		}
		// Look up this player's UUID
		OfflinePlayer op = plugin.getServer().getOfflinePlayer(((Player) sender).getUniqueId());
		String uuid = op.getUniqueId().toString();
		ResultSetCount rs = new ResultSetCount(plugin, uuid);
		if (!rs.resultSet()) {
			TARDISMessage.send(sender, "PLAYER_NO_TARDIS");
			return true;
		}
		// set repair
		int r = 1;
		if (args.length == 3) {
			r = TARDISNumberParsers.parseInt(args[2]);
		}
		HashMap<String, Object> where = new HashMap<>();
		where.put("uuid", uuid);
		HashMap<String, Object> set = new HashMap<>();
		set.put("repair", r);
		plugin.getQueryFactory().doUpdate("t_count", set, where);
		TARDISMessage.send(sender, "REPAIR_SET", args[1], args[2]);
		return true;
	}
}
