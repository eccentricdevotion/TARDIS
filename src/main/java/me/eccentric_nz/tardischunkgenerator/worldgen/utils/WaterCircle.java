package me.eccentric_nz.tardischunkgenerator.worldgen.utils;

import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.concurrent.ThreadLocalRandom;

public class WaterCircle {

    public static boolean[][] MASK = new boolean[16][16];

    static {
        for (int col = 0; col < 16; col++) {
            MASK[0][col] = false;
            MASK[15][col] = false;
        }
        for (int row = 1; row < 15; row++) {
            MASK[row][0] = false;
            MASK[row][15] = false;
        }
        for (int row = 1; row < 15; row++) {
            for (int col = 1; col < 15; col++) {
                MASK[row][col] = true;
            }
        }
        MASK[1][1] = false;
        MASK[1][2] = false;
        MASK[1][3] = false;
        MASK[1][4] = false;
        MASK[1][11] = false;
        MASK[1][12] = false;
        MASK[1][13] = false;
        MASK[1][14] = false;
        MASK[2][1] = false;
        MASK[2][2] = false;
        MASK[2][3] = false;
        MASK[2][12] = false;
        MASK[2][13] = false;
        MASK[2][14] = false;
        MASK[3][1] = false;
        MASK[3][2] = false;
        MASK[3][13] = false;
        MASK[3][14] = false;
        MASK[4][1] = false;
        MASK[4][14] = false;
        MASK[11][1] = false;
        MASK[11][14] = false;
        MASK[12][1] = false;
        MASK[12][2] = false;
        MASK[12][13] = false;
        MASK[12][14] = false;
        MASK[13][1] = false;
        MASK[13][2] = false;
        MASK[13][3] = false;
        MASK[13][12] = false;
        MASK[13][13] = false;
        MASK[13][14] = false;
        MASK[14][1] = false;
        MASK[14][2] = false;
        MASK[14][3] = false;
        MASK[14][4] = false;
        MASK[14][11] = false;
        MASK[14][12] = false;
        MASK[14][13] = false;
        MASK[14][14] = false;
        makeBlob();
    }

    public static boolean[][] makeBlob() {
        boolean[][] blob = new boolean[16][16];
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(ThreadLocalRandom.current().nextLong(), 1);
        generator.setScale(0.08);
        for (int z = 0; z < 16; z++) {
            for (int x = 0; x < 16; x++) {
                double noise = generator.noise(x, z, 1, 1, true);
                boolean pixel = (noise > -0.5 && noise < 0.5) && MASK[x][z];
                blob[x][z] = pixel;
            }
        }
        return blob;
    }
}
