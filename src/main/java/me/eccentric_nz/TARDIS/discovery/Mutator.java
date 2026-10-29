package me.eccentric_nz.TARDIS.discovery;

import org.bukkit.Material;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class Mutator {

    private final long seed;
    private final int radius;
    private final Material[] gradient = new Material[]{
            Material.MUDDY_MANGROVE_ROOTS, Material.GREEN_TERRACOTTA,
            Material.MOSS_BLOCK, Material.MOSSY_COBBLESTONE,
            Material.COBBLESTONE
    };

    public Mutator(long seed, int radius) {
        this.seed = seed;
        this.radius = radius;
    }

    public Material[][][] addBlends(Material[][][] array) {
        return applyGradient(array, gradient, radius, seed);
    }

    private static int[][][] buildDistanceField(Material[][][] array) {
        int sizeX = array.length;
        int sizeY = array[0].length;
        int sizeZ = array[0][0].length;
        int[][][] distance = new int[sizeX][sizeY][sizeZ];
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    distance[x][y][z] = -1;
                }
            }
        }
        Queue<BlockPos> queue = new ArrayDeque<>();
        // seed all muddy mangrove root positions
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    if (array[x][y][z] == Material.MUDDY_MANGROVE_ROOTS) {
                        distance[x][y][z] = 0;
                        queue.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        int[][] directions = {
                {1, 0, 0},
                {-1, 0, 0},
                {0, 1, 0},
                {0, -1, 0},
                {0, 0, 1},
                {0, 0, -1}
        };
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            int currentDistance = distance[current.x()][current.y()][current.z()];
            for (int[] dir : directions) {
                int nx = current.x() + dir[0];
                int ny = current.y() + dir[1];
                int nz = current.z() + dir[2];
                if (nx < 0 || ny < 0 || nz < 0 || nx >= sizeX || ny >= sizeY || nz >= sizeZ) {
                    continue;
                }
                // ignore air entirely
                if (array[nx][ny][nz] == Material.AIR) {
                    continue;
                }
                if (distance[nx][ny][nz] != -1) {
                    continue;
                }
                distance[nx][ny][nz] = currentDistance + 1;
                queue.add(new BlockPos(nx, ny, nz));
            }
        }
        return distance;
    }

    public static Material[][][] applyGradient(Material[][][] array, Material[] gradientBlocks, int blendRadius, long seed) {
        int[][][] distanceField = buildDistanceField(array);
        SimplexOctaveGenerator noise = new SimplexOctaveGenerator(new Random(seed), 4);
        noise.setScale(0.15);
        double noiseStrength = 0.2;
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                for (int z = 0; z < array[x][y].length; z++) {
                    Material current = array[x][y][z];
                    // never modify air
                    if (current == Material.AIR) {
                        continue;
                    }
                    // keep seed blocks / walls / clay unchanged
                    if (current == Material.MUDDY_MANGROVE_ROOTS || current == Material.TUFF_WALL || current == Material.CLAY) {
                        continue;
                    }
                    int distance = distanceField[x][y][z];
                    // not connected to any seed
                    if (distance == -1) {
                        continue;
                    }
                    // outside influence radius - keep original block
                    if (distance >= blendRadius) {
                        continue;
                    }
                    double gradient = (double) distance / blendRadius;
                    // distort boundary with noise
                    gradient += noise.noise(x, y, z, 0.5, 0.5) * noiseStrength;
                    gradient = Math.clamp(gradient, 0.0, 1.0);
                    double scaled = gradient * (gradientBlocks.length - 1);
                    int lower = (int) Math.floor(scaled);
                    int upper = Math.min(lower + 1, gradientBlocks.length - 1);
                    double t = scaled - lower;
                    double blendNoise = (noise.noise(x * 2, y * 2, z * 2, 0.5, 0.5) + 1.0) / 2.0;
                    Material result = blendNoise < t ? gradientBlocks[upper] : gradientBlocks[lower];
                    array[x][y][z] = result;
                }
            }
        }
        return array;
    }
}
