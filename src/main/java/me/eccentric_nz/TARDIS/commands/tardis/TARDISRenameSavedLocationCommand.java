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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetDestinations;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISRenameSavedLocationCommand {

	private final TARDISPlugin plugin;

	TARDISRenameSavedLocationCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	boolean doRenameSave(Player player, String[] args) {
		if (TARDISPermission.hasPermission(player, "tardis.save")) {
			if (args.length < 3) {
				TARDISMessage.send(player, "TOO_FEW_ARGS");
				return false;
			}
			ResultSetTardisID rs = new ResultSetTardisID(plugin);
			if (!rs.fromUUID(player.getUniqueId().toString())) {
				TARDISMessage.send(player, "NO_TARDIS");
				return false;
			}
			int id = rs.getTardisId();
			HashMap<String, Object> whered = new HashMap<>();
			whered.put("dest_name", args[1]);
			whered.put("tardis_id", id);
			ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
			if (!rsd.resultSet()) {
				TARDISMessage.send(player, "SAVE_NOT_FOUND");
				return false;
			}
			if (!args[2].matches("[A-Za-z0-9_]{2,16}")) {
				TARDISMessage.send(player, "SAVE_NAME_NOT_VALID");
				return false;
			} else if (args[2].equalsIgnoreCase("hide") || args[1].equalsIgnoreCase("rebuild") || args[1].equalsIgnoreCase("home")) {
				TARDISMessage.send(player, "SAVE_RESERVED");
				return false;
			} else {
				int destID = rsd.getDestId();
				HashMap<String, Object> did = new HashMap<>();
				did.put("dest_id", destID);
				HashMap<String, Object> set = new HashMap<>();
				set.put("dest_name", args[2]);
				plugin.getQueryFactory().doUpdate("destinations", set, did);
				TARDISMessage.send(player, "DEST_RENAMED", args[2]);
			}
			return true;
		} else {
			TARDISMessage.send(player, "NO_PERMS");
			return false;
		}
	}
}
