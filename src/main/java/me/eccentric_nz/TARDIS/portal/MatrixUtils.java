package me.eccentric_nz.TARDIS.portal;

import org.bukkit.block.data.BlockData;

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
                    // TODO process problem blocks
                    data[size - 1 - c][r] = capture[l][r][c];
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
                    // TODO process problem blocks
                    data[c][size - 1 - r] = capture[l][r][c];
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
                    // TODO process problem blocks
                    data[r][size - 1 - c] = capture[l][size - 1 - r][c];
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
