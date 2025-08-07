/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.enumeration;

/**
 * @author eccentric_nz
 */
public enum COMPASS {

    EAST(90.0f),
    SOUTH_EAST(135.0f),
    SOUTH(180.0f),
    SOUTH_WEST(-135.0f),
    WEST(-90.0f),
    NORTH_WEST(-45.0f),
    NORTH(0.0f),
    NORTH_EAST(45.0f);

    private final float yaw;

    COMPASS(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public COMPASS forPreset() {
        return switch (this) {
            case EAST, NORTH_EAST -> COMPASS.EAST;
            case SOUTH, SOUTH_EAST -> COMPASS.SOUTH;
            case WEST, SOUTH_WEST -> COMPASS.WEST;
            default -> COMPASS.NORTH;
        };
    }
}
