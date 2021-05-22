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
package me.eccentric_nz.tardis.move;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISSpectaclesRunnable implements Runnable {

	private final TARDIS plugin;
	private final HashMap<COMPASS, Door> lower = new HashMap<>();
	private final Door upper;

	public TARDISSpectaclesRunnable(TARDIS plugin) {
		this.plugin = plugin;
		Door door = (Door) Material.IRON_DOOR.createBlockData();
		door.setHalf(Bisected.Half.BOTTOM);
		door.setHinge(Door.Hinge.RIGHT);
		lower.put(COMPASS.EAST, calculateFacing(door, COMPASS.EAST));
		lower.put(COMPASS.SOUTH, calculateFacing(door, COMPASS.SOUTH));
		lower.put(COMPASS.WEST, calculateFacing(door, COMPASS.WEST));
		lower.put(COMPASS.NORTH, calculateFacing(door, COMPASS.NORTH));
		upper = (Door) Material.IRON_DOOR.createBlockData();
		upper.setHalf(Bisected.Half.TOP);
	}

	@Override

	public void run() {
		plugin.getTrackerKeeper().getInvisibleDoors().forEach((key, value) -> {
			Player p = plugin.getServer().getPlayer(key);
			if (p != null && p.isOnline() && plugin.getTrackerKeeper().getSpectacleWearers().contains(key)) {
				String b = p.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getRelative(BlockFace.UP).toString();
				if (b.equals(value.toString())) {
					ResultSetTardisID rs = new ResultSetTardisID(plugin);
					if (rs.fromUUID(key.toString())) {
						HashMap<String, Object> wherec = new HashMap<>();
						wherec.put("tardis_id", rs.getTardis_id());
						ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
						if (rsc.resultSet()) {
							p.sendBlockChange(value.getLocation(), lower.get(rsc.getDirection()));
							p.sendBlockChange(value.getRelative(BlockFace.UP).getLocation(), upper);
						}
					}
				}
			}
		});
	}

	private Door calculateFacing(Door door, COMPASS compass) {
		switch (compass) {
			case SOUTH -> door.setFacing(BlockFace.SOUTH);
			case WEST -> door.setFacing(BlockFace.WEST);
			case NORTH -> door.setFacing(BlockFace.NORTH);
			default -> door.setFacing(BlockFace.EAST);
		}
		return door;
	}
}
