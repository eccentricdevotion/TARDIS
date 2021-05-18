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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISSetDifficultyCommand {

	private final TARDIS plugin;

	TARDISSetDifficultyCommand(TARDIS plugin) {
		this.plugin = plugin;
	}

	boolean setDiff(Player player, String[] args) {
		if (!plugin.getConfig().getBoolean("allow.player_difficulty")) {
			TARDISMessage.send(player, "CMD_DISABLED");
			return true;
		}
		if (!TARDISPermission.hasPermission(player, "tardis.difficulty")) {
			TARDISMessage.send(player, "NO_PERMS");
			return true;
		}
		if (args.length < 2) {
			TARDISMessage.send(player, "TOO_FEW_ARGS");
			return false;
		}
		if (!args[1].equalsIgnoreCase("easy") && !args[1].equalsIgnoreCase("hard")) {
			TARDISMessage.send(player, "ARG_DIFF");
			return true;
		}
		int diff = (args[1].equalsIgnoreCase("easy")) ? 1 : 0;
		HashMap<String, Object> set = new HashMap<>();
		set.put("difficulty", diff);
		HashMap<String, Object> where = new HashMap<>();
		where.put("uuid", player.getUniqueId().toString());
		plugin.getQueryFactory().doUpdate("player_prefs", set, where);
		TARDISMessage.send(player, "DIFF_SAVED");
		return true;
	}
}
