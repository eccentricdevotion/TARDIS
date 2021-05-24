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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.hads.TARDISCloisterBell;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.entity.Player;

public class TARDISBellCommand {

	private final TARDISPlugin plugin;

	public TARDISBellCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean toggle(int id, Player player, String[] args) {
		if (args.length < 2) {
			// cloister bell
			if (plugin.getTrackerKeeper().getCloisterBells().containsKey(id)) {
				stopCloisterBell(id);
			} else {
				startCloisterBell(id);
			}
		} else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("off")) {
				if (plugin.getTrackerKeeper().getCloisterBells().containsKey(id)) {
					stopCloisterBell(id);
				} else {
					TARDISMessage.send(player, "CLOISTER_BELL_CMD", "off");
				}
				return true;
			} else if (args[1].equalsIgnoreCase("on")) {
				if (!plugin.getTrackerKeeper().getCloisterBells().containsKey(id)) {
					startCloisterBell(id);
				} else {
					TARDISMessage.send(player, "CLOISTER_BELL_CMD", "on");
				}
				return true;
			}
		}
		return false;
	}

	private void stopCloisterBell(int id) {
		plugin.getServer().getScheduler().cancelTask(plugin.getTrackerKeeper().getCloisterBells().get(id));
		plugin.getTrackerKeeper().getCloisterBells().remove(id);
	}

	private void startCloisterBell(int id) {
		TARDISCloisterBell bell = new TARDISCloisterBell(plugin, Integer.MAX_VALUE, id);
		int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
		bell.setTask(taskId);
		plugin.getTrackerKeeper().getCloisterBells().put(id, taskId);
	}
}
