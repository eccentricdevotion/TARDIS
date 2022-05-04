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
                        BlockFace face;
                        switch (directional.getFacing()) {
                            case EAST -> face = BlockFace.WEST;
                            case SOUTH -> face = BlockFace.NORTH;
                            case WEST -> face = BlockFace.EAST;
                            // north
                            default -> face = BlockFace.SOUTH;
                        }
                        directional.setFacing(face);
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

    public static double getPlayerAngle(double point1X, double point1Y, double point2X, double point2Y, double fixedX, double fixedY) {
        double angle1 = Math.atan2(point1Y - fixedY, point1X - fixedX);
        double angle2 = Math.atan2(point2Y - fixedY, point2X - fixedX);
        return Math.toDegrees(angle1 - angle2);
    }
}
