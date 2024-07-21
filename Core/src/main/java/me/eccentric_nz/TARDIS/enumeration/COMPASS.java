/*
 * Copyright (C) 2024 eccentric_nz
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

import org.bukkit.Rotation;

/**
 * @author eccentric_nz
 */
public enum COMPASS {

    EAST(Rotation.FLIPPED, 90.0f),
    SOUTH_EAST(Rotation.FLIPPED_45, 135.0f),
    SOUTH(Rotation.COUNTER_CLOCKWISE, 180.0f),
    SOUTH_WEST(Rotation.COUNTER_CLOCKWISE_45, -135.0f),
    WEST(Rotation.NONE, -90.0f),
    NORTH_WEST(Rotation.CLOCKWISE_45, -45.0f),
    NORTH(Rotation.CLOCKWISE, 0.0f),
    NORTH_EAST(Rotation.COUNTER_CLOCKWISE_45, 45.0f);

    private final Rotation rotation;
    private final float yaw;

    COMPASS(Rotation rotation, float yaw) {
        this.rotation = rotation;
        this.yaw = yaw;
    }

    public Rotation getRotation() {
        return rotation;
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
