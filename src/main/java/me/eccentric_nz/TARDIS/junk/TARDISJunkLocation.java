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
package me.eccentric_nz.tardis.junk;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.planets.TARDISBiome;
import org.bukkit.Location;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISJunkLocation {

	private final TARDISPlugin plugin;
	private Location current;
	private Location home;
	private int id;
	private TARDISBiome tardisBiome;

	TARDISJunkLocation(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	boolean isNotHome() {
		// check the Junk tardis is not home already
		ResultSetTardisID rs = new ResultSetTardisID(plugin);
		if (rs.fromUUID("00000000-aaaa-bbbb-cccc-000000000000")) {
			id = rs.getTardisId();
			// get current location
			HashMap<String, Object> wherec = new HashMap<>();
			wherec.put("tardis_id", id);
			ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
			if (rsc.resultSet()) {
				tardisBiome = TARDISBiome.get(rsc.getBiomeKey());
				// get home location
				HashMap<String, Object> whereh = new HashMap<>();
				whereh.put("tardis_id", id);
				ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, whereh);
				if (rsh.resultSet()) {
					current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
					home = new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ());
					// compare locations
					return !current.equals(home);
				}
			}
		}
		return true;
	}

	public Location getCurrent() {
		return current;
	}

	public Location getHome() {
		return home;
	}

	public int getId() {
		return id;
	}

	public TARDISBiome getTardisBiome() {
		return tardisBiome;
	}
}
