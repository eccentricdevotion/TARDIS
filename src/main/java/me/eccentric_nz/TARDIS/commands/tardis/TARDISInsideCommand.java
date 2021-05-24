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
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISInsideCommand {

	private final TARDISPlugin plugin;

	TARDISInsideCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	boolean whosInside(Player player) {
		// check they are a timelord
		ResultSetTardisID rs = new ResultSetTardisID(plugin);
		if (!rs.fromUUID(player.getUniqueId().toString())) {
			TARDISMessage.send(player, "NOT_A_TIMELORD");
			return true;
		}
		int id = rs.getTardisId();
		HashMap<String, Object> wheret = new HashMap<>();
		wheret.put("tardis_id", id);
		ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, true);
		if (rst.resultSet()) {
			List<UUID> data = rst.getData();
			TARDISMessage.send(player, "INSIDE_PLAYERS");
			data.forEach((s) -> {
				Player p = plugin.getServer().getPlayer(s);
				if (p != null) {
					player.sendMessage(p.getDisplayName());
				}
			});
		} else {
			TARDISMessage.send(player, "INSIDE");
		}
		return true;
	}
}
