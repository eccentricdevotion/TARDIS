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
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
class TARDISJunkReturn {

	private final TARDIS plugin;

	TARDISJunkReturn(TARDIS plugin) {
		this.plugin = plugin;
	}

	boolean recall(CommandSender sender) {
		if (!sender.hasPermission("tardis.admin")) {
			TARDISMessage.send(sender, "CMD_ADMIN");
			return true;
		}
		TARDISJunkLocation tjl = new TARDISJunkLocation(plugin);
		if (tjl.isNotHome()) {
			Location home = tjl.getHome();
			// fly home
			DestroyData dd = new DestroyData();
			dd.setLocation(tjl.getCurrent());
			dd.setDirection(COMPASS.SOUTH);
			dd.setHide(false);
			dd.setOutside(false);
			dd.setSubmarine(false);
			dd.setTardisID(tjl.getId());
			dd.setTardisBiome(tjl.getTardisBiome());
			dd.setThrottle(SpaceTimeThrottle.JUNK);
			plugin.getPresetDestroyer().destroyPreset(dd);
			// fly my pretties
			plugin.getGeneralKeeper().setJunkTravelling(true);
			plugin.getGeneralKeeper().setJunkDestination(home);
			TARDISMessage.send(sender, "JUNK_RETURN");
			return true;
		} else {
			TARDISMessage.send(sender, "JUNK_AT_HOME");
			return true;
		}
	}
}
