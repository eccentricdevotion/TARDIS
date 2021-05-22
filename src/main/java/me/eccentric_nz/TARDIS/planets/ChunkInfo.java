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
package me.eccentric_nz.tardis.planets;

import java.util.Objects;

public class ChunkInfo {

	final String world;
	final int x;
	final int z;

	public ChunkInfo(String world, int x, int z) {
		this.world = world;
		this.x = x;
		this.z = z;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ChunkInfo chunkInfo = (ChunkInfo) o;
		return x == chunkInfo.x && z == chunkInfo.z && Objects.equals(world, chunkInfo.world);
	}

	@Override
	public int hashCode() {
		return Objects.hash(world, x, z);
	}
}
