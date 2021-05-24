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
package me.eccentric_nz.tardis.rooms;

import org.bukkit.Art;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class TARDISPainting {

	public static Location calculatePosition(Art art, BlockFace facing, Location loc) {
		switch (art) {
			// 1x2
			case GRAHAM:
			case WANDERER:
				return loc.getBlock().getLocation().add(0, -1, 0);
			// 2x1 & 4x3
			case CREEBET:
			case COURBET:
			case POOL:
			case SEA:
			case SUNSET:
			case DONKEY_KONG:
			case SKELETON:
				if (facing == BlockFace.WEST) {
					return loc.getBlock().getLocation().add(0, 0, -1);
				} else if (facing == BlockFace.SOUTH) {
					return loc.getBlock().getLocation().add(-1, 0, 0);
				}
				return loc;
			// 2x2, 4x2 & 4x4
			case BUST:
			case MATCH:
			case SKULL_AND_ROSES:
			case STAGE:
			case VOID:
			case WITHER:
			case FIGHTERS:
			case BURNING_SKULL:
			case PIGSCENE:
			case POINTER:
				if (facing == BlockFace.WEST) {
					return loc.getBlock().getLocation().add(0, -1, -1);
				} else if (facing == BlockFace.SOUTH) {
					return loc.getBlock().getLocation().add(-1, -1, 0);
				}
				return loc.add(0, -1, 0);
			// 1x1 or unsupported artwork
			default:
				return loc;
		}
	}
}
