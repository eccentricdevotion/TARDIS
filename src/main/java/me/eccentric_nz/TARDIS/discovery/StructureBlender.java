package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class StructureBlender {

    /**
     * Blends the flat sides of a cuboid structure outwards into the surrounding world using Simplex noise.
     *
     * @param world The world where the structure is placed.
     * @param minX  Minimum X coordinate of the structure.
     * @param maxX  Maximum X coordinate of the structure.
     * @param minY  Minimum Y coordinate of the structure.
     * @param maxY  Maximum Y coordinate of the structure.
     * @param minZ  Minimum Z coordinate of the structure.
     * @param maxZ  Maximum Z coordinate of the structure.
     */
    public static void blendStructureOutwards(World world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        SimplexNoiseGenerator noiseGen = new SimplexNoiseGenerator(TARDISConstants.RANDOM.nextLong());
        double frequency = 0.1;
        int maxExtension = 4;
        // west face (X = minX, towards negative X)
        for (int z = minZ; z <= maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                int extension = calculateExtension(noiseGen, y, z, frequency, maxExtension);
                if (extension <= 0) {
                    continue;
                }
                Block outerBlock = world.getBlockAt(minX, y, z);
                BlockData blockData = outerBlock.getBlockData();
                if (blockData.getMaterial().isAir()) {
                    continue;
                }
                for (int d = 1; d <= extension; d++) {
                    Block targetBlock = world.getBlockAt(minX - d, y, z);
                    if (isReplaceable(targetBlock.getType())) {
                        targetBlock.setBlockData(blockData, false);
                    }
                }
            }
        }
        // east face (X = maxX, towards positive X)
        for (int z = minZ; z <= maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                int extension = calculateExtension(noiseGen, y, z, frequency, maxExtension);
                if (extension <= 0) {
                    continue;
                }
                Block outerBlock = world.getBlockAt(maxX, y, z);
                BlockData blockData = outerBlock.getBlockData();
                if (blockData.getMaterial().isAir()) {
                    continue;
                }
                for (int d = 1; d <= extension; d++) {
                    Block targetBlock = world.getBlockAt(maxX + d, y, z);
                    if (isReplaceable(targetBlock.getType())) {
                        targetBlock.setBlockData(blockData, false);
                    }
                }
            }
        }
        // north face (Z = minZ, towards negative Z)
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                int extension = calculateExtension(noiseGen, x, y, frequency, maxExtension);
                if (extension <= 0) {
                    continue;
                }
                Block outerBlock = world.getBlockAt(x, y, minZ);
                BlockData blockData = outerBlock.getBlockData();
                if (blockData.getMaterial().isAir()) {
                    continue;
                }
                for (int d = 1; d <= extension; d++) {
                    Block targetBlock = world.getBlockAt(x, y, minZ - d);
                    if (isReplaceable(targetBlock.getType())) {
                        targetBlock.setBlockData(blockData, false);
                    }
                }
            }
        }
        // south face (Z = maxZ, towards positive Z)
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                int extension = calculateExtension(noiseGen, x, y, frequency, maxExtension);
                if (extension <= 0) {
                    continue;
                }
                Block outerBlock = world.getBlockAt(x, y, maxZ);
                BlockData blockData = outerBlock.getBlockData();
                if (blockData.getMaterial().isAir()) {
                    continue;
                }
                for (int d = 1; d <= extension; d++) {
                    Block targetBlock = world.getBlockAt(x, y, maxZ + d);
                    if (isReplaceable(targetBlock.getType())) {
                        targetBlock.setBlockData(blockData, false);
                    }
                }
            }
        }
    }

    private static int calculateExtension(SimplexNoiseGenerator noiseGen, double xOrZ, double y, double frequency, int maxExtension) {
        double noise = noiseGen.noise(xOrZ * frequency, y * frequency); // returns [-1, 1]
        double normalized = (noise + 1.0) / 2.0; // maps to [0, 1]
        return (int) Math.round(normalized * maxExtension);
    }

    /**
     * Determines if a block is safe to overwrite during the blend.
     * Prevents overwriting other structures or air pockets you want to keep intact.
     */
    private static boolean isReplaceable(Material material) {
        return material == Material.AIR ||
                material == Material.CAVE_AIR ||
                material == Material.STONE ||
                material == Material.DEEPSLATE ||
                material == Material.TUFF ||
                material == Material.ANDESITE ||
                material == Material.DIORITE ||
                material == Material.DIRT ||
                material == Material.END_STONE ||
                material == Material.NETHERRACK ||
                material == Material.GRAVEL ||
                material == Material.GRANITE;
    }
}
