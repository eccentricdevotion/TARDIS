/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.siegemode;

import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * @author eccentric_nz
 */
public class TARDISSiegeArea {

    private final int id;
    private static final int MIN_Y = 48;
    private static final int MAX_Y = 80;
    private int minX;
    private int maxX;
    private int minZ;
    private int maxZ;

    public TARDISSiegeArea(int id, Chunk chunk) {
        this.id = id;
        setMinMaxXZ(chunk);
    }

    private void setMinMaxXZ(Chunk chunk) {
        minX = getChunkX(true, chunk);
        maxX = getChunkX(false, chunk);
        minZ = getChunkZ(true, chunk);
        maxZ = getChunkZ(false, chunk);
    }

    private int getChunkX(boolean min, Chunk c) {
        int cx = c.getX();
        int cz = c.getZ();
        if (min) {
            return c.getWorld().getChunkAt(cx - 4, cz).getBlock(0, 64, 0).getX();
        } else {
            return c.getWorld().getChunkAt(cx + 4, cz).getBlock(0, 64, 0).getX();
        }
    }

    private int getChunkZ(boolean min, Chunk c) {
        int cx = c.getX();
        int cz = c.getZ();
        if (min) {
            return c.getWorld().getChunkAt(cx, cz - 4).getBlock(0, 64, 0).getZ();
        } else {
            return c.getWorld().getChunkAt(cx, cz + 4).getBlock(0, 64, 0).getZ();
        }
    }

    public boolean isInSiegeArea(Location l) {
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        return (x >= minX && x <= maxX && y >= MIN_Y && y <= MAX_Y && z >= minZ && z <= maxZ);
    }

    public int getId() {
        return id;
    }
}
