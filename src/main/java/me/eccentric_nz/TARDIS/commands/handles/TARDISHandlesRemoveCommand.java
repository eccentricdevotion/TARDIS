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
package me.eccentric_nz.tardis.commands.handles;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISHandlesRemoveCommand {

	private final TARDISPlugin plugin;

	public TARDISHandlesRemoveCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean purge(Player player) {
		ResultSetTardisID rs = new ResultSetTardisID(plugin);
		if (rs.fromUUID(player.getUniqueId().toString())) {
			HashMap<String, Object> whereh = new HashMap<>();
			whereh.put("tardis_id", rs.getTardisId());
			whereh.put("type", 26);
			plugin.getQueryFactory().doDelete("controls", whereh);
			TARDISMessage.send(player, "HANDLES_DELETED");
		} else {
			TARDISMessage.send(player, "NOT_A_TIMELORD");
		}
		return true;
	}
}
