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
package me.eccentric_nz.tardis.utility.recalculators;

import me.eccentric_nz.tardis.enumeration.COMPASS;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

/**
 * @author eccentric_nz
 */
public class TARDISTrapdoorRecalculator {

	/**
	 * Recalculate the data for directional block (TRAPDOOR) when the tardis preset changes direction.
	 *
	 * @param b the block data to convert
	 * @param d the new direction of the tardis
	 * @return the recalculated byte
	 */
	public BlockData recalculate(BlockData b, COMPASS d) {
		Directional trap = (Directional) b;
		switch (d) {
			case SOUTH:
				switch (trap.getFacing()) {
					case SOUTH -> trap.setFacing(BlockFace.WEST); // 3
					case NORTH -> trap.setFacing(BlockFace.EAST); // 2
					case EAST -> trap.setFacing(BlockFace.SOUTH); // 0
					default -> // WEST
							trap.setFacing(BlockFace.NORTH); // 1
				}
				break;
			case WEST:
				switch (trap.getFacing()) {
					case SOUTH -> trap.setFacing(BlockFace.NORTH);
					case NORTH -> trap.setFacing(BlockFace.SOUTH);
					case EAST -> trap.setFacing(BlockFace.WEST);
					default -> // WEST
							trap.setFacing(BlockFace.EAST);
				}
				break;
			default:
				switch (trap.getFacing()) {
					case SOUTH -> trap.setFacing(BlockFace.EAST);
					case NORTH -> trap.setFacing(BlockFace.WEST);
					case EAST -> trap.setFacing(BlockFace.NORTH);
					default -> // WEST
							trap.setFacing(BlockFace.SOUTH);
				}
				break;
		}
		return trap;
	}
}
