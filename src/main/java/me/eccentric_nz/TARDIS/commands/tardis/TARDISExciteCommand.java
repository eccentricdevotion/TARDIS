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
import me.eccentric_nz.tardis.control.TARDISAtmosphericExcitation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISExciteCommand {

	private final TARDISPlugin plugin;

	TARDISExciteCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	boolean excite(Player player) {
		if (plugin.getTrackerKeeper().getExcitation().contains(player.getUniqueId())) {
			TARDISMessage.send(player, "CMD_EXCITE");
			return true;
		}
		// get tardis id
		ResultSetTardisID rs = new ResultSetTardisID(plugin);
		if (rs.fromUUID(player.getUniqueId().toString())) {
			new TARDISAtmosphericExcitation(plugin).excite(rs.getTardisId(), player);
			plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
			return true;
		}
		return true;
	}
}
