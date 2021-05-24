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
package me.eccentric_nz.tardis.flight;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.QueryFactory;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Switch;

import java.util.HashMap;

public class TARDISHandbrake {

	public static void setLevers(Block block, boolean powered, boolean inside, String handbrake_loc, int id, TARDISPlugin plugin) {
		Switch lever = (Switch) block.getBlockData();
		lever.setPowered(powered);
		block.setBlockData(lever);
		if (inside) {
			// get other handbrakes in this tardis
			HashMap<String, Object> where = new HashMap<>();
			where.put("tardis_id", id);
			where.put("type", 0);
			ResultSetControls rsc = new ResultSetControls(plugin, where, true);
			if (rsc.resultSet()) {
				for (HashMap<String, String> map : rsc.getData()) {
					if (!map.get("location").equals(handbrake_loc)) {
						Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(map.get("location"));
						if (location != null) {
							Block other = location.getBlock();
							BlockData blockData = other.getBlockData();
							if (blockData instanceof Switch brake) {
								brake.setPowered(powered);
								other.setBlockData(brake);
							} else {
								// remove the control record because the lever no longer exists
								HashMap<String, Object> wherec = new HashMap<>();
								wherec.put("tardis_id", id);
								wherec.put("type", 0);
								wherec.put("location", map.get("location"));
								new QueryFactory(plugin).doDelete("controls", wherec);
							}
						}
					}
				}
			}
		}
	}
}
