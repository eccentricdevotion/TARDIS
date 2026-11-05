package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.WeightedChoice;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class Mutator {

    private final long seed;
    private final int radius;
    private final StructureVariant variant;

    public Mutator(long seed, int radius, Location location) {
        this.seed = seed;
        this.radius = radius;
        variant = getVariant(location.getWorld().getEnvironment(), location.getBlockY());
    }

    private StructureVariant getVariant(World.Environment environment, int y) {
        Material[] gradient;
        Material base;
        Material seed;
        Material wall;
        WeightedChoice<Material> floor;
        WeightedChoice<BlockData[]> column;
        BlockData hanging;
        BlockData light;
        BlockData end;
        Material decor;
        BlockData carpet;
        switch (environment) {
            case NETHER -> {
                gradient = TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.DEAD_HORN_CORAL_BLOCK);
                base = Material.DEAD_HORN_CORAL_BLOCK;
                seed = Material.CRIMSON_HYPHAE;
                wall = Material.MUD_BRICK_WALL;
                floor = WaterFeature.netherFloor;
                column = WaterFeature.netherColumn;
                hanging = Material.WEEPING_VINES_PLANT.createBlockData();
                light = hanging;
                end = Material.SHROOMLIGHT.createBlockData();
                decor = Material.HANGING_ROOTS;
                carpet = Material.WARPED_ROOTS.createBlockData();
            }
            case THE_END -> {
                gradient = TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.END_STONE);
                base = Material.END_STONE;
                seed = Material.CRYING_OBSIDIAN;
                wall = Material.END_STONE_BRICK_WALL;
                floor = WaterFeature.endFloor;
                column = WaterFeature.endColumn;
                hanging = Material.IRON_CHAIN.createBlockData();
                light = hanging;
                end = Material.END_ROD.createBlockData("[facing=down]");
                decor = Material.SPORE_BLOSSOM;
                carpet = Material.WHITE_CARPET.createBlockData();
            }
            default -> {
                if (y < -10) {
                    gradient = TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.COBBLESTONE);
                    base = Material.COBBLESTONE;
                    seed = Material.WARPED_HYPHAE;
                    wall = Material.COBBLESTONE_WALL;
                    floor = WaterFeature.cobbleFloor;
                    column = WaterFeature.cobbleColumn;
                    hanging = Material.CAVE_VINES_PLANT.createBlockData();
                    light = Material.CAVE_VINES_PLANT.createBlockData("[berries=true]");
                    end = Material.CAVE_VINES.createBlockData();
                    decor = Material.SPORE_BLOSSOM;
                    carpet = Material.SCULK_VEIN.createBlockData("[down=true,up=false,east=false,north=false,south=false,west=false]");
                } else {
                    gradient = TARDIS.plugin.getBuildKeeper().getFade().getGradients().get(Material.TUFF);
                    base = Material.TUFF;
                    seed = Material.MUDDY_MANGROVE_ROOTS;
                    wall = Material.TUFF_WALL;
                    floor = WaterFeature.tuffFloor;
                    column = WaterFeature.tuffColumn;
                    hanging = Material.CAVE_VINES_PLANT.createBlockData();
                    light = Material.CAVE_VINES_PLANT.createBlockData("[berries=true]");
                    end = Material.CAVE_VINES.createBlockData();
                    decor = Material.SPORE_BLOSSOM;
                    carpet = Material.MOSS_CARPET.createBlockData();
                }
            }
        }
        return new StructureVariant(gradient, base, seed, wall, floor, column, hanging, light, end, decor, carpet);
    }

    public Material[][][] addBlends(Material[][][] array) {
        return applyGradient(array, variant.gradient(), radius, seed);
    }

    private int[][][] buildDistanceField(Material[][][] array) {
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
                    if (array[x][y][z] == variant.seed()) {
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

    public Material[][][] applyGradient(Material[][][] array, Material[] gradientBlocks, int blendRadius, long seed) {
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
                    if (current == variant.seed() || current == variant.wall() || current == Material.CLAY) {
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

    public StructureVariant getVariant() {
        return variant;
    }
}
