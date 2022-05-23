/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility.recalculators;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rail.Shape;

public class TARDISRailRecalculator {


    public BlockData recalculate(BlockData b, COMPASS d) {
        switch (d) {
            case WEST -> {
                // rotate 180°
                return oneEighty(b);
            }
            case NORTH -> {
                // rotate anti-clockwise
                return antiClockwise(b);
            }
            case SOUTH -> {
                // rotate clockwise
                return clockwise(b);
            }
            default -> {
                // do nothing
                return b;
            }
        }
    }

    private BlockData clockwise(BlockData b) {
        Rail rail = (Rail) b;
        Rail.Shape shape;
        switch (rail.getShape()) {
            case NORTH_SOUTH -> shape = Shape.EAST_WEST;
            case EAST_WEST -> shape = Shape.NORTH_SOUTH;
            case ASCENDING_EAST -> shape = Shape.ASCENDING_SOUTH;
            case ASCENDING_WEST -> shape = Shape.ASCENDING_NORTH;
            case ASCENDING_NORTH -> shape = Shape.ASCENDING_EAST;
            case ASCENDING_SOUTH -> shape = Shape.ASCENDING_WEST;
            case SOUTH_EAST -> shape = Shape.SOUTH_WEST;
            case SOUTH_WEST -> shape = Shape.NORTH_WEST;
            case NORTH_WEST -> shape = Shape.NORTH_EAST;
            // NORTH_EAST
            default -> shape = Shape.SOUTH_EAST;
        }
        rail.setShape(shape);
        return rail;
    }

    private BlockData antiClockwise(BlockData b) {
        Rail rail = (Rail) b;
        Rail.Shape shape;
        switch (rail.getShape()) {
            case NORTH_SOUTH -> shape = Shape.EAST_WEST;
            case EAST_WEST -> shape = Shape.NORTH_SOUTH;
            case ASCENDING_EAST -> shape = Shape.ASCENDING_NORTH;
            case ASCENDING_WEST -> shape = Shape.ASCENDING_SOUTH;
            case ASCENDING_NORTH -> shape = Shape.ASCENDING_WEST;
            case ASCENDING_SOUTH -> shape = Shape.ASCENDING_EAST;
            case SOUTH_EAST -> shape = Shape.NORTH_EAST;
            case SOUTH_WEST -> shape = Shape.SOUTH_EAST;
            case NORTH_WEST -> shape = Shape.SOUTH_WEST;
            // NORTH_EAST
            default -> shape = Shape.NORTH_WEST;
        }
        rail.setShape(shape);
        return rail;
    }

    private BlockData oneEighty(BlockData b) {
        Rail rail = (Rail) b;
        Rail.Shape shape;
        switch (rail.getShape()) {
            case NORTH_SOUTH -> shape = Shape.NORTH_SOUTH;
            case EAST_WEST -> shape = Shape.EAST_WEST;
            case ASCENDING_EAST -> shape = Shape.ASCENDING_WEST;
            case ASCENDING_WEST -> shape = Shape.ASCENDING_EAST;
            case ASCENDING_NORTH -> shape = Shape.ASCENDING_SOUTH;
            case ASCENDING_SOUTH -> shape = Shape.ASCENDING_NORTH;
            case SOUTH_EAST -> shape = Shape.NORTH_WEST;
            case SOUTH_WEST -> shape = Shape.NORTH_EAST;
            case NORTH_WEST -> shape = Shape.SOUTH_EAST;
            // NORTH_EAST
            default -> shape = Shape.SOUTH_WEST;
        }
        rail.setShape(shape);
        return rail;
    }
}
