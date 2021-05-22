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

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.TARDISStaticUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.entity.Player;

import java.util.Date;

/**
 * @author eccentric_nz
 */
class TARDISHandlesTimeCommand {

	private final TARDIS plugin;

	TARDISHandlesTimeCommand(TARDIS plugin) {
		this.plugin = plugin;
	}

	boolean sayTime(Player player) {
		long minecraftTime = player.getWorld().getTime();
		String daynight = TARDISStaticUtils.getTime(minecraftTime);
		// get current server time (in a nice format)
		Date date = new Date();
		String formatted = DateFormatUtils.format(date, "h:mm a");
		// send message to player with current time
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			TARDISMessage.handlesSend(player, "HANDLES_TIME", minecraftTime, daynight, parseTime(minecraftTime));
			TARDISMessage.handlesSend(player, "HANDLES_SERVER_TIME", formatted);
		}, 2L);
		return true;
	}

	private String parseTime(long time) {
		long hours = time / 1000 + 6;
		long minutes = (time % 1000) * 60 / 1000;
		String ampm = "AM";
		if (hours >= 12) {
			hours -= 12;
			ampm = "PM";
		}
		if (hours == 0) {
			hours = 12;
		}
		String mm = (minutes < 10) ? "0" + minutes : "" + minutes;
		return hours + ":" + mm + " " + ampm;
	}
}
