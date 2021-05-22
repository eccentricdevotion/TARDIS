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

import com.google.common.collect.Sets;
import me.eccentric_nz.tardis.enumeration.COMPASS;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;

import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISFenceRecalculator {

	private final Set<BlockFace> eastwest = Sets.newHashSet(BlockFace.WEST, BlockFace.EAST);
	private final Set<BlockFace> northsouth = Sets.newHashSet(BlockFace.SOUTH, BlockFace.NORTH);

	/**
	 * Recalculate the data for multiple facing block (FENCE) when the tardis preset changes direction.
	 *
	 * @param b the block data stored in the preset data
	 * @param d the new direction of the tardis
	 * @return the recalculated block data
	 */
	public BlockData recalculate(BlockData b, COMPASS d) {
		MultipleFacing fence = (MultipleFacing) b;
		// get all set faces
		Set<BlockFace> has = fence.getFaces();
		Set<BlockFace> set = null;
		if (d.equals(COMPASS.NORTH) || d.equals(COMPASS.SOUTH)) {
			if (has.equals(eastwest)) {
				set = northsouth;
			} else if (has.equals(northsouth)) {
				set = eastwest;
			}
		}
		if (set != null) {
			has.forEach((f) -> fence.setFace(f, false));
			set.forEach((f) -> fence.setFace(f, true));
		}
		return fence;
	}
}
