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
import me.eccentric_nz.tardis.database.resultset.ResultSetTag;
import me.eccentric_nz.tardis.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author eccentric_nz
 */
class TARDISTagCommand {

	private final TARDISPlugin plugin;

	TARDISTagCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	boolean getStats(Player player) {
		ResultSetTag rs = new ResultSetTag(plugin);
		player.sendMessage(plugin.getPluginName() + "Here are the stats:");
		String who = (!Objects.equals(plugin.getTagConfig().getString("it"), "")) ? plugin.getTagConfig().getString("it") : "No one";
		player.sendMessage(who + " is currently the " + ChatColor.RED + "'OOD'");
		player.sendMessage("-----------");
		player.sendMessage(ChatColor.GOLD + "Top 5 OODs");
		player.sendMessage("-----------");
		if (rs.resultSet()) {
			ArrayList<HashMap<String, String>> data = rs.getData();
			data.forEach((map) -> {
				String p = map.get("player");
				long t = TARDISNumberParsers.parseLong(map.get("time"));
				player.sendMessage(p + ": " + ChatColor.GREEN + getHoursMinutesSeconds(t));
			});
		} else {
			player.sendMessage("The are no stats yet :(");
		}
		player.sendMessage("-----------");
		return true;
	}

	private String getHoursMinutesSeconds(long millis) {
		return String.format("%02dh:%02dm:%02ds", TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) -
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis) -
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	}
}
