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
package me.eccentric_nz.tardis.builders;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.enumeration.PRESET;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;

public class TARDISBuilderUtility {

	public static void saveDoorLocation(BuildData bd) {
		World world = bd.getLocation().getWorld();
		int x = bd.getLocation().getBlockX();
		int y = bd.getLocation().getBlockY();
		int z = bd.getLocation().getBlockZ();
		// remember the door location
		String doorloc = world.getName() + ":" + x + ":" + y + ":" + z;
		String doorStr = world.getBlockAt(x, y, z).getLocation().toString();
		TARDISPlugin.plugin.getGeneralKeeper().getProtectBlockMap().put(doorStr, bd.getTardisId());
		// should insert the door when tardis is first made, and then update location there after!
		HashMap<String, Object> whered = new HashMap<>();
		whered.put("door_type", 0);
		whered.put("tardis_id", bd.getTardisId());
		ResultSetDoors rsd = new ResultSetDoors(TARDISPlugin.plugin, whered, false);
		HashMap<String, Object> setd = new HashMap<>();
		setd.put("door_location", doorloc);
		setd.put("door_direction", bd.getDirection().toString());
		if (rsd.resultSet()) {
			HashMap<String, Object> whereid = new HashMap<>();
			whereid.put("door_id", rsd.getDoorId());
			TARDISPlugin.plugin.getQueryFactory().doUpdate("doors", setd, whereid);
		} else {
			setd.put("tardis_id", bd.getTardisId());
			setd.put("door_type", 0);
			TARDISPlugin.plugin.getQueryFactory().doInsert("doors", setd);
		}
	}

	public static Material getDyeMaterial(PRESET preset) {
		String split = preset.toString().replace("POLICE_BOX_", "");
		String dye = split + "_DYE";
		return Material.valueOf(dye);
	}
}
