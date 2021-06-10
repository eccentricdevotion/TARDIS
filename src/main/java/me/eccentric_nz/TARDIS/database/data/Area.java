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
package me.eccentric_nz.tardis.database.data;

/**
 * @author eccentric_nz
 */
public class Area {

    private final String areaName;
    private final String world;
    private final int minX;
    private final int minZ;
    private final int maxX;
    private final int maxZ;
    private final int y;
    private final int parkingDistance;
    private final String invisibility;
    private final String direction;

    public Area(String areaName, String world, int minX, int minZ, int maxX, int maxZ, int y, int parkingDistance, String invisibility, String direction) {
        this.areaName = areaName;
        this.world = world;
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
        this.y = y;
        this.parkingDistance = parkingDistance;
        this.invisibility = invisibility;
        this.direction = direction;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getWorld() {
        return world;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getY() {
        return y;
    }

    public int getParkingDistance() {
        return parkingDistance;
    }

    public String getInvisibility() {
        return invisibility;
    }

    public String getDirection() {
        return direction;
    }
}
