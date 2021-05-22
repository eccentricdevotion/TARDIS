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
package me.eccentric_nz.tardis.ars;

import org.bukkit.Chunk;

/**
 * Data container for performing Architectural Reconfiguration System room jettisons.
 *
 * @author eccentric_nz
 */
public class TARDISARSJettison {

	private Chunk chunk;
	private int y;
	private int x;
	private int z;

	TARDISARSJettison() {
	}

	public TARDISARSJettison(Chunk chunk, int y, int x, int z) {
		this.chunk = chunk;
		setY(y);
		setX(x);
		setZ(z);
	}

	public Chunk getChunk() {
		return chunk;
	}

	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}

	public int getY() {
		return y;
	}

	public final void setY(int y) {
		// convert to the actual y coordinate of the start of the chunk
		this.y = getChunkY(y);
	}

	public int getX() {
		return x;
	}

	public final void setX(int x) {
		// convert to the actual x coordinate of the start of the chunk
		this.x = getChunkX(x, chunk);
	}

	public int getZ() {
		return z;
	}

	public final void setZ(int z) {
		// convert to the actual z coordinate of the start of the chunk
		this.z = getChunkZ(z, chunk);
	}

	private int getChunkX(int xx, Chunk c) {
		int cx = c.getX();
		int cz = c.getZ();
		return switch (xx) {
			case 0 -> c.getWorld().getChunkAt(cx - 4, cz).getBlock(0, 64, 0).getX();
			case 1 -> c.getWorld().getChunkAt(cx - 3, cz).getBlock(0, 64, 0).getX();
			case 2 -> c.getWorld().getChunkAt(cx - 2, cz).getBlock(0, 64, 0).getX();
			case 3 -> c.getWorld().getChunkAt(cx - 1, cz).getBlock(0, 64, 0).getX();
			case 5 -> c.getWorld().getChunkAt(cx + 1, cz).getBlock(0, 64, 0).getX();
			case 6 -> c.getWorld().getChunkAt(cx + 2, cz).getBlock(0, 64, 0).getX();
			case 7 -> c.getWorld().getChunkAt(cx + 3, cz).getBlock(0, 64, 0).getX();
			case 8 -> c.getWorld().getChunkAt(cx + 4, cz).getBlock(0, 64, 0).getX();
			default -> c.getBlock(0, 64, 0).getX();
		};
	}

	private int getChunkZ(int zz, Chunk c) {
		int cx = c.getX();
		int cz = c.getZ();
		return switch (zz) {
			case 0 -> c.getWorld().getChunkAt(cz, cz - 4).getBlock(0, 64, 0).getZ();
			case 1 -> c.getWorld().getChunkAt(cx, cz - 3).getBlock(0, 64, 0).getZ();
			case 2 -> c.getWorld().getChunkAt(cx, cz - 2).getBlock(0, 64, 0).getZ();
			case 3 -> c.getWorld().getChunkAt(cx, cz - 1).getBlock(0, 64, 0).getZ();
			case 5 -> c.getWorld().getChunkAt(cx, cz + 1).getBlock(0, 64, 0).getZ();
			case 6 -> c.getWorld().getChunkAt(cx, cz + 2).getBlock(0, 64, 0).getZ();
			case 7 -> c.getWorld().getChunkAt(cx, cz + 3).getBlock(0, 64, 0).getZ();
			case 8 -> c.getWorld().getChunkAt(cx, cz + 4).getBlock(0, 64, 0).getZ();
			default -> c.getBlock(0, 64, 0).getZ();
		};
	}

	private int getChunkY(int yy) {
		return switch (yy) {
			case -1 -> 32;
			case 0 -> 48;
			case 2 -> 80;
			case 3 -> 96;
			default -> 64;
		};
	}
}
