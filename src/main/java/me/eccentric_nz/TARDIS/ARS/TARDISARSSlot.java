/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.ARS;

import org.bukkit.Chunk;

/**
 * Data container for performing Architectural Reconfiguration System room
 * growing.
 *
 * @author eccentric_nz
 */
public class TARDISARSSlot {

    private Chunk chunk;
    private int y;
    private int x;
    private int z;

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        // convert to the actual y coordinate of the start of the chunk
        int sl = getChunkY(y);
        this.y = sl;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        // convert to the actual x coordinate of the start of the chunk
        int sx = getChunkX(x, this.chunk);
        this.x = sx;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        // convert to the actual z coordinate of the start of the chunk
        int sz = getChunkZ(z, this.chunk);
        this.z = sz;
    }

    private int getChunkX(int xx, Chunk c) {
        int cx = c.getX();
        int cz = c.getZ();
        switch (xx) {
            case 0:
                return c.getWorld().getChunkAt(cx - 4, cz).getBlock(0, 64, 0).getX();
            case 1:
                return c.getWorld().getChunkAt(cx - 3, cz).getBlock(0, 64, 0).getX();
            case 2:
                return c.getWorld().getChunkAt(cx - 2, cz).getBlock(0, 64, 0).getX();
            case 3:
                return c.getWorld().getChunkAt(cx - 1, cz).getBlock(0, 64, 0).getX();
            case 5:
                return c.getWorld().getChunkAt(cx + 1, cz).getBlock(0, 64, 0).getX();
            case 6:
                return c.getWorld().getChunkAt(cx + 2, cz).getBlock(0, 64, 0).getX();
            case 7:
                return c.getWorld().getChunkAt(cx + 3, cz).getBlock(0, 64, 0).getX();
            case 8:
                return c.getWorld().getChunkAt(cx + 4, cz).getBlock(0, 64, 0).getX();
            default:
                return c.getBlock(0, 64, 0).getX();
        }
    }

    private int getChunkZ(int zz, Chunk c) {
        int cx = c.getX();
        int cz = c.getZ();
        switch (zz) {
            case 0:
                return c.getWorld().getChunkAt(cx, cz - 4).getBlock(0, 64, 0).getZ();
            case 1:
                return c.getWorld().getChunkAt(cx, cz - 3).getBlock(0, 64, 0).getZ();
            case 2:
                return c.getWorld().getChunkAt(cx, cz - 2).getBlock(0, 64, 0).getZ();
            case 3:
                return c.getWorld().getChunkAt(cx, cz - 1).getBlock(0, 64, 0).getZ();
            case 5:
                return c.getWorld().getChunkAt(cx, cz + 1).getBlock(0, 64, 0).getZ();
            case 6:
                return c.getWorld().getChunkAt(cx, cz + 2).getBlock(0, 64, 0).getZ();
            case 7:
                return c.getWorld().getChunkAt(cx, cz + 3).getBlock(0, 64, 0).getZ();
            case 8:
                return c.getWorld().getChunkAt(cx, cz + 4).getBlock(0, 64, 0).getZ();
            default:
                return c.getBlock(0, 64, 0).getZ();
        }
    }

    private int getChunkY(int yy) {
        switch (yy) {
            case -1:
                return 32;
            case 0:
                return 48;
            case 2:
                return 80;
            case 3:
                return 96;
            default:
                return 64;
        }
    }
}
