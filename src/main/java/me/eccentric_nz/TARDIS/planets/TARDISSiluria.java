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
package me.eccentric_nz.tardis.planets;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.utility.TARDISChecker;
import org.bukkit.ChatColor;

/**
 * @author eccentric_nz
 */
public class TARDISSiluria {

	private final TARDISPlugin plugin;

	public TARDISSiluria(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	public void loadSilurianUnderworld() {
		String s_world = plugin.getServer().getWorlds().get(0).getName();
		// copy datapack files
		if (!TARDISChecker.hasDimension("siluria")) {
			plugin.getServer().reloadData();
			// message console to restart server
			TARDISMessage.message(plugin.getConsole(), ChatColor.RED +
													   "Siluria data pack has been installed, please restart the server to enable the world.");
			// get default server world
			// add world to config
			if (!plugin.getPlanetsConfig().getBoolean("planets." + s_world + "_tardis_siluria.time_travel")) {
				plugin.getPlanetsConfig().set("planets." + s_world + "_tardis_siluria.time_travel", true);
				plugin.savePlanetsConfig();
			}
		} else {
			plugin.getServer().getWorld(s_world + "_tardis_siluriao");
		}
	}
}
