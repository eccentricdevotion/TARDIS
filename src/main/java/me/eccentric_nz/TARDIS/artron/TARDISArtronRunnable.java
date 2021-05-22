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
package me.eccentric_nz.tardis.artron;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisArtron;
import org.bukkit.Location;

import java.util.HashMap;

/**
 * Within the tardis' generator room is an Artron Energy Capacitor. The Eighth Doctor had a habit of using Artron Energy
 * to make toast.
 *
 * @author eccentric_nz
 */
class TARDISArtronRunnable implements Runnable {

	private final TARDIS plugin;
	private final int id;
	private int task;

	TARDISArtronRunnable(TARDIS plugin, int id) {
		this.plugin = plugin;
		this.id = id;
	}

	/**
	 * A runnable task that recharges the tardis.
	 */
	@Override
	public void run() {
		int level = isFull(id);
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", id);
		boolean near = isNearCharger(id);
		if (!near || level > (plugin.getArtronConfig().getInt("full_charge") - 1)) {
			plugin.getServer().getScheduler().cancelTask(task);
			task = 0;
			HashMap<String, Object> set = new HashMap<>();
			set.put("recharging", 0);
			plugin.getQueryFactory().doUpdate("tardis", set, where);
		} else if (near) {
			// calculate percentage
			int onepercent = Math.round(plugin.getArtronConfig().getInt("full_charge") / 100.0F);
			// update tardis artron_level
			plugin.getQueryFactory().alterEnergyLevel("tardis", onepercent, where, null);
		}
	}

	/**
	 * Checks whether the tardis is near a recharge location.
	 */
	private boolean isNearCharger(int id) {
		HashMap<String, Object> where = new HashMap<>();
		where.put("tardis_id", id);
		ResultSetCurrentLocation rs = new ResultSetCurrentLocation(plugin, where);
		if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id) || !rs.resultSet()) {
			return false;
		}
		if (rs.getWorld() == null) {
			return false;
		}
		// get Police Box location
		Location pb_loc = new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ());
		// check location is within configured blocks of a recharger
		for (Location l : plugin.getGeneralKeeper().getRechargers()) {
			if (plugin.getUtils().compareLocations(pb_loc, l)) {
				// strike lightning to the Police Box torch location
				if (plugin.getConfig().getBoolean("preferences.strike_lightning")) {
					pb_loc.setY(pb_loc.getY() + 3);
					rs.getWorld().strikeLightningEffect(pb_loc);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the current Artron Energy Level for the specified tardis.
	 */
	private int isFull(int id) {
		ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
		return (rs.fromID(id)) ? rs.getArtronLevel() : plugin.getArtronConfig().getInt("full_charge");
	}

	public void setTask(int task) {
		this.task = task;
	}
}
