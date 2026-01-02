/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.api;

import org.bukkit.World;

/**
 * @author eccentric_nz
 */
public class WorldAndRange {

    private final World world;
    private final int minX;
    private final int minZ;
    private final int rangeX;
    private final int rangeZ;

    /**
     * Data storage for generating random time travel coordinates
     *
     * @param world  the world
     * @param minX   the minimum x coordinate
     * @param minZ   the minimum z coordinate
     * @param rangeX the upper range x value
     * @param rangeZ the upper range z value
     */
    WorldAndRange(World world, int minX, int minZ, int rangeX, int rangeZ) {
        this.world = world;
        this.minX = minX;
        this.minZ = minZ;
        this.rangeX = rangeX;
        this.rangeZ = rangeZ;
    }

    /**
     * Gets the world
     *
     * @return a world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the min x coordinate
     *
     * @return the min x
     */
    public int getMinX() {
        return minX;
    }

    /**
     * Gets the min z coordinate
     *
     * @return the min z
     */
    public int getMinZ() {
        return minZ;
    }

    /**
     * Gets the x range
     *
     * @return the x range
     */
    int getRangeX() {
        return rangeX;
    }

    /**
     * Gets the z range
     *
     * @return the z range
     */
    int getRangeZ() {
        return rangeZ;
    }
}
