package me.eccentric_nz.TARDIS.builders.interior;

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.discovery.BlockPos;
import me.eccentric_nz.TARDIS.discovery.StructureVariant;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class FadeMutator {

    private final long seed;
    private final int radius;
    private final StructureVariant variant;

    public FadeMutator(long seed, int radius, StructureVariant variant) {
        this.seed = seed;
        this.radius = radius;
        this.variant = variant;
    }

    private final Set<Material> notThese = Set.of(Material.AIR, Material.CLAY, Material.JUKEBOX, Material.NOTE_BLOCK,
            Material.COMMAND_BLOCK, Material.CRAFTING_TABLE, Material.CHEST, Material.IRON_BARS, Material.REPEATER,
            Material.REDSTONE_WALL_TORCH, Material.REDSTONE_WIRE, Material.STICKY_PISTON, Material.PISTON_HEAD,
            Material.SMOOTH_QUARTZ_SLAB, Material.SMOOTH_QUARTZ_STAIRS, Material.OAK_WALL_SIGN, Material.OAK_BUTTON,
            Material.STONE_BUTTON, Material.RED_MUSHROOM_BLOCK, Material.LEVER, Material.IRON_DOOR, Material.BLACK_WOOL,
            Material.STONE_PRESSURE_PLATE, Material.STONE_SLAB, Material.BLUE_WOOL);

    private int[][][] buildDistanceField(BlockData[][][] array) {
        Material seed = variant.seed();
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
        // seed all start block positions
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    if (array[x][y][z].getMaterial() == seed) {
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
                if (array[nx][ny][nz].equals(TARDISConstants.AIR)) {
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

    public BlockData[][][] applyGradient(BlockData[][][] array, Material[] gradientBlocks) {
        int[][][] distanceField = buildDistanceField(array);
        SimplexOctaveGenerator noise = new SimplexOctaveGenerator(new Random(seed), 4);
        noise.setScale(0.15);
        double noiseStrength = 0.2;
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                for (int z = 0; z < array[x][y].length; z++) {
                    Material current = array[x][y][z].getMaterial();
                    // never modify air / clay / other TARDIS special blocks
                    if (notThese.contains(current)) {
                        continue;
                    }
                    // keep seed blocks / walls unchanged
                    if (current == variant.seed() || current == variant.wall()) {
                        continue;
                    }
                    int distance = distanceField[x][y][z];
                    // not connected to any seed
                    if (distance == -1) {
                        continue;
                    }
                    // outside influence radius - keep original block
                    if (distance >= radius) {
                        continue;
                    }
                    double gradient = (double) distance / radius;
                    // distort boundary with noise
                    gradient += noise.noise(x, y, z, 0.5, 0.5) * noiseStrength;
                    gradient = Math.clamp(gradient, 0.0, 1.0);
                    double scaled = gradient * (gradientBlocks.length - 1);
                    int lower = (int) Math.floor(scaled);
                    int upper = Math.min(lower + 1, gradientBlocks.length - 1);
                    double t = scaled - lower;
                    double blendNoise = (noise.noise(x * 2, y * 2, z * 2, 0.5, 0.5) + 1.0) / 2.0;
                    BlockData result = blendNoise < t ? gradientBlocks[upper].createBlockData() : gradientBlocks[lower].createBlockData();
                    array[x][y][z] = result;
                }
            }
        }
        return array;
    }

    public StructureVariant getVariant() {
        return variant;
    }
}
