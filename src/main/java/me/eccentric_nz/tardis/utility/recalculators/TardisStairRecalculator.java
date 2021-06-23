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
package me.eccentric_nz.tardis.utility.recalculators;

import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

/**
 * @author eccentric_nz
 */
public class TardisStairRecalculator {

    /**
     * Recalculate the data for directional block (STAIR) when the tardis preset changes direction.
     *
     * @param b    the block data stored in the preset data
     * @param d    the new direction of the tardis
     * @param col  the preset column that is being calculated
     * @param duck whether this is the Rubber Duck preset
     * @return the recalculated block data
     */
    public BlockData recalculate(BlockData b, CardinalDirection d, int col, boolean duck) {
        Directional stair = (Directional) b;
        switch (d) {
            case SOUTH:
                switch (stair.getFacing()) {
                    case EAST: // 0
                        stair.setFacing(BlockFace.SOUTH); // 2
                        break;
                    case WEST: // 1
                        stair.setFacing(BlockFace.NORTH); // 3
                        break;
                    case SOUTH: // 2
                        stair.setFacing(BlockFace.WEST); // 1
                        break;
                    case NORTH: // 3
                        stair.setFacing(BlockFace.EAST); // 0
                        break;
                    default:
                        break;
                }
                break;
            case WEST:
                if (duck) {
                    switch (stair.getFacing()) {
                        case EAST:
                            stair.setFacing(BlockFace.WEST); // 1
                            break;
                        case WEST:
                            stair.setFacing(BlockFace.EAST); // 0
                            break;
                        case SOUTH:
                            stair.setFacing(BlockFace.NORTH); // 3
                            break;
                        case NORTH:
                            stair.setFacing(BlockFace.SOUTH); // 2
                            break;
                        default:
                            break;
                    }
                } else {
                    if (col == 3 || col == 7) {
                        switch (stair.getFacing()) {
                            case EAST:
                                stair.setFacing(BlockFace.WEST); // 1
                                break;
                            case WEST:
                                stair.setFacing(BlockFace.EAST); // 0
                                break;
                            default:
                                break;
                        }
                    } else {
                        break;
                    }
                }
                break;
            default:
                switch (stair.getFacing()) {
                    case EAST:
                        stair.setFacing(BlockFace.NORTH); // 3
                        break;
                    case WEST:
                        stair.setFacing(BlockFace.SOUTH); // 2
                        break;
                    case SOUTH:
                        stair.setFacing(BlockFace.EAST); // 0
                        break;
                    case NORTH:
                        stair.setFacing(BlockFace.WEST); // 1
                        break;
                    default:
                        break;
                }
                break;
        }
        return stair;
    }
}
