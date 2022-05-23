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
package me.eccentric_nz.TARDIS.portal;

import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

public class MatrixUtils {

    /**
     * Rotate an array anti-clockwise.
     *
     * @param capture the array to rotate
     * @return the rotated array
     */
    public static BlockData[][][] rotateToWest(BlockData[][][] capture) {
        // anti-clockwise
        int layers = capture.length;
        int size = capture[0].length;
        BlockData[][][] rotated = new BlockData[layers][size][size];
        for (int l = 0; l < layers; l++) {
            BlockData data[][] = new BlockData[size][size];
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    // process problem blocks
                    BlockData blockData = capture[l][r][c];
                    if (blockData instanceof Directional directional) {
                        BlockFace face;
                        switch (directional.getFacing()) {
                            case EAST -> face = BlockFace.SOUTH;
                            case SOUTH -> face = BlockFace.WEST;
                            case WEST -> face = BlockFace.NORTH;
                            // north
                            default -> face = BlockFace.EAST;
                        }
                        directional.setFacing(face);
                        data[size - 1 - c][r] = directional;
                    } else {
                        data[size - 1 - c][r] = blockData;
                    }
                }
            }
            rotated[l] = data;
        }
        return rotated;
    }

    /**
     * Rotate an array clockwise.
     *
     * @param capture the array to rotate
     * @return the rotated array
     */
    public static BlockData[][][] rotateToEast(BlockData[][][] capture) {
        // clockwise
        int layers = capture.length;
        final int size = capture[0].length;
        BlockData[][][] rotated = new BlockData[layers][size][size];
        for (int l = 0; l < layers; l++) {
            BlockData[][] data = new BlockData[size][size];
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    // process problem blocks
                    BlockData blockData = capture[l][r][c];
                    if (blockData instanceof Directional directional) {
                        BlockFace face;
                        switch (directional.getFacing()) {
                            case EAST -> face = BlockFace.NORTH;
                            case SOUTH -> face = BlockFace.EAST;
                            case WEST -> face = BlockFace.SOUTH;
                            // north
                            default -> face = BlockFace.WEST;
                        }
                        directional.setFacing(face);
                        data[c][size - 1 - r] = directional;
                    } else {
                        data[c][size - 1 - r] = blockData;
                    }
                }
            }
            rotated[l] = data;
        }
        return rotated;
    }

    /**
     * Rotate an array 180 degrees.
     *
     * @param capture the array to rotate
     * @return the rotated array
     */
    public static BlockData[][][] rotateToNorth(BlockData[][][] capture) {
        // 180°
        int layers = capture.length;
        final int size = capture[0].length;
        BlockData[][][] rotated = new BlockData[layers][size][size];
        for (int l = 0; l < layers; l++) {
            BlockData[][] data = new BlockData[size][size];
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    // process problem blocks
                    BlockData blockData = capture[l][size - 1 - r][c];
                    if (blockData instanceof Directional directional) {
                        directional.setFacing(directional.getFacing().getOppositeFace());
                        data[r][size - 1 - c] = directional;
                    } else {

                        data[r][size - 1 - c] = blockData;
                    }
                }
            }
            rotated[l] = data;
        }
        return rotated;
    }

    /**
     * Get the angle between two points, with a fixed point as the center.
     * <p>
     * The first two parameters are the coordinates of a point directly in front of the TARDIS door,
     * the second two parameters are the coordinates of the player, and the last two parameters are
     * the coordinates of the TARDIS door.
     *
     * @param vx The X coordinate of the point directly in front of the TARDIS door
     * @param vz The Z coordinate of the point directly in front of the TARDIS door
     * @param px The x coordinate of the player
     * @param pz The Z coordinate of the player
     * @param dx The x coordinate of the TARDIS door
     * @param dz The Z coordinate of the TARDIS door
     * @return The angle between the two points
     */
    public static double getPlayerAngle(double vx, double vz, double px, double pz, double dx, double dz) {
        double angle1 = Math.atan2(vz - dz, vx - dx);
        double angle2 = Math.atan2(pz - dz, px - dx);
        return Math.toDegrees(angle1 - angle2);
    }
}
