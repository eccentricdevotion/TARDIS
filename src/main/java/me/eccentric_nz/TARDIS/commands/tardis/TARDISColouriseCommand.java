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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * As with the rest of the Doctor's TARDIS, the aesthetic design of the time rotor occasionally changed throughout the
 * Doctor's travels. As it varied through designs, it alternated between being a single column and a series of
 * components that moved into each other from above and below.
 *
 * @author eccentric_nz
 */
class TARDISColouriseCommand {

	private final TARDIS plugin;

	TARDISColouriseCommand(TARDIS plugin) {
		this.plugin = plugin;
	}

	void updateBeaconGlass(Player player) {
		if (!TARDISPermission.hasPermission(player, "tardis.upgrade")) {
			TARDISMessage.send(player, "NO_PERMS");
			return;
		}
		// check they are still in the TARDIS world
		if (!plugin.getUtils().inTARDISWorld(player)) {
			TARDISMessage.send(player, "CMD_IN_WORLD");
			return;
		}
		// must have a TARDIS
		HashMap<String, Object> where = new HashMap<>();
		where.put("uuid", player.getUniqueId().toString());
		ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
		if (!rs.resultSet()) {
			TARDISMessage.send(player, "NOT_A_TIMELORD");
			return;
		}
		Tardis tardis = rs.getTardis();
		Schematic console = tardis.getSchematic();
		if (!console.hasBeacon()) {
			TARDISMessage.send(player, "COLOUR_NOT_VALID");
			return;
		}
		if (console.mustUseSonic()) {
			TARDISMessage.send(player, "COLOUR_SONIC");
			return;
		}
		int ownerid = tardis.getTardis_id();
		HashMap<String, Object> wheret = new HashMap<>();
		wheret.put("uuid", player.getUniqueId().toString());
		ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
		if (!rst.resultSet()) {
			TARDISMessage.send(player, "NOT_IN_TARDIS");
			return;
		}
		int thisid = rst.getTardis_id();
		// must be timelord of the TARDIS
		if (thisid != ownerid) {
			TARDISMessage.send(player, "CMD_ONLY_TL");
			return;
		}
		// track the player for 60 seconds
		UUID uuid = player.getUniqueId();
		plugin.getTrackerKeeper().getBeaconColouring().add(uuid);
		// message player
		TARDISMessage.send(player, "COLOUR_TIME");
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			plugin.getTrackerKeeper().getBeaconColouring().remove(uuid);
		}, 1200L);
	}
}
