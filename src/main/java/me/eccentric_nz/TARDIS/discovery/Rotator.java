package me.eccentric_nz.TARDIS.discovery;

import org.bukkit.Material;

public class Rotator {

    public Material[][][] counterClockwise90(Material[][][] original) {
        int yLength = original.length;
        int xLength = original[0].length;
        int zLength = original[0][0].length;
        // the new array swaps X and Z dimensions
        Material[][][] rotated = new Material[yLength][zLength][xLength];
        for (int y = 0; y < yLength; y++) {
            for (int x = 0; x < xLength; x++) {
                for (int z = 0; z < zLength; z++) {
                    // rotate coordinates
                    int newZ = xLength - 1 - x;
                    rotated[y][z][newZ] = original[y][x][z];
                }
            }
        }
        return rotated;
    }
}
