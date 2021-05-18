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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.forcefield;

import org.bukkit.Location;
import org.bukkit.World;

public class TARDISForceFieldLocation {

	private final Location topFrontLeft;
	private final Location topFrontRight;
	private final Location topBacktLeft;
	private final Location topBackRight;
	private final Location bottomFrontLeft;
	private final Location bottomFrontRight;
	private final Location bottomBackLeft;
	private final Location bottomBackRight;

	public TARDISForceFieldLocation(Location location, double range) {
		double minX = location.getX() - range;
		double minY = location.getY();
		double minZ = location.getZ() - range;
		double maxX = location.getX() + range;
		double maxY = location.getY() + 1;
		double maxZ = location.getZ() + range;
		World world = location.getWorld();
		topFrontLeft = new Location(world, minX, maxY, minZ);
		topFrontRight = new Location(world, maxX, maxY, minZ);
		topBacktLeft = new Location(world, minX, maxY, maxZ);
		topBackRight = new Location(world, maxX, maxY, maxZ);
		bottomFrontLeft = new Location(world, minX, minY, minZ);
		bottomFrontRight = new Location(world, maxX, minY, minZ);
		bottomBackLeft = new Location(world, minX, minY, maxZ);
		bottomBackRight = new Location(world, maxX, minY, maxZ);
	}

	public Location getTopFrontLeft() {
		return topFrontLeft;
	}

	public Location getTopFrontRight() {
		return topFrontRight;
	}

	public Location getTopBackLeft() {
		return topBacktLeft;
	}

	public Location getTopBackRight() {
		return topBackRight;
	}

	public Location getBottomFrontLeft() {
		return bottomFrontLeft;
	}

	public Location getBottomFrontRight() {
		return bottomFrontRight;
	}

	public Location getBottomBackLeft() {
		return bottomBackLeft;
	}

	public Location getBottomBackRight() {
		return bottomBackRight;
	}
}
