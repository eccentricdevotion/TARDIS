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
package me.eccentric_nz.tardis.utility.recalculators;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

public class TARDISBedRecalculator {

	public BlockData recalculate(BlockData data, COMPASS d) {
		Directional bed = (Directional) data;
		switch (bed.getFacing()) {
			case WEST -> bed.setFacing(BlockFace.valueOf(TARDIS.plugin.getPresetBuilder().getOppositeFace(d).toString()));
			case EAST -> bed.setFacing(BlockFace.valueOf(d.toString()));
			case NORTH ->
					// anticlockwise 90°
					bed.setFacing(rotate90Anticlockwise(d));
			default ->
					// clockwise 90°
					bed.setFacing(rotate90Clockwise(d));
		}
		return bed;
	}

	private BlockFace rotate90Clockwise(COMPASS d) {
		return switch (d) {
			case SOUTH -> BlockFace.WEST;
			case WEST -> BlockFace.NORTH;
			case NORTH -> BlockFace.EAST;
			default -> BlockFace.SOUTH;
		};
	}

	private BlockFace rotate90Anticlockwise(COMPASS d) {
		return switch (d) {
			case SOUTH -> BlockFace.EAST;
			case WEST -> BlockFace.SOUTH;
			case NORTH -> BlockFace.WEST;
			default -> BlockFace.NORTH;
		};
	}
}
