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
package me.eccentric_nz.tardis.commands.remote;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.builders.BuildData;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISRemoteRebuildCommand {

	private final TARDIS plugin;

	TARDISRemoteRebuildCommand(TARDIS plugin) {
		this.plugin = plugin;
	}

	boolean doRemoteRebuild(CommandSender sender, int id, OfflinePlayer player, boolean hidden) {
		HashMap<String, Object> wherecl = new HashMap<>();
		wherecl.put("tardis_id", id);
		ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
		if (!rsc.resultSet()) {
			TARDISMessage.send(sender, "CURRENT_NOT_FOUND");
			return true;
		}
		ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
		if (rs.fromID(id) && rs.getPreset().equals(PRESET.INVISIBLE)) {
			TARDISMessage.send(player.getPlayer(), "INVISIBILITY_ENGAGED");
			return true;
		}
		BuildData bd = new BuildData(player.getUniqueId().toString());
		bd.setDirection(rsc.getDirection());
		bd.setLocation(new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ()));
		bd.setMalfunction(false);
		bd.setOutside(false);
		bd.setPlayer(player);
		bd.setRebuild(true);
		bd.setSubmarine(rsc.isSubmarine());
		bd.setTardisID(id);
		bd.setThrottle(SpaceTimeThrottle.REBUILD);
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 10L);
		TARDISMessage.send(sender, "TARDIS_REBUILT");
		// set hidden to false
		if (hidden) {
			HashMap<String, Object> whereh = new HashMap<>();
			whereh.put("tardis_id", id);
			HashMap<String, Object> seth = new HashMap<>();
			seth.put("hidden", 0);
			plugin.getQueryFactory().doUpdate("tardis", seth, whereh);
		}
		return true;
	}
}
