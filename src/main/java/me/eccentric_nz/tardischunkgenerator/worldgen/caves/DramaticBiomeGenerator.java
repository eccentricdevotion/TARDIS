package me.eccentric_nz.tardischunkgenerator.worldgen.caves;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.WorldInfo;

/**
 * Converts selected vanilla-biome regions into large underground environments.
 * <p>
 * The important distinction from the normal cave pass is scale: regionNoise
 * operates over hundreds of blocks, while chamber/root/flood noise supplies
 * local shape. All sampling is in absolute world coordinates, so features do
 * not restart at chunk borders.
 */
public final class DramaticBiomeGenerator {

    public static void generate(WorldInfo worldInfo, ChunkData chunkData, int chunkX, int chunkZ, NoiseSet noise) {
        int originX = chunkX << 4;
        int originZ = chunkZ << 4;
        int minY = Math.max(chunkData.getMinHeight(), 4);
        int maxY = Math.min(chunkData.getMaxHeight() - 1, 127);
        // main regional chamber pass
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int wx = originX + x;
                int wz = originZ + z;
                Biome biome = worldInfo.vanillaBiomeProvider().getBiome(worldInfo, wx, 64, wz);
                BiomeStyle style = BiomeStyle.fromBiome(biome);
                if (!isDramatic(style)) {
                    continue;
                }
                double region = noise.regionNoise().noise(wx / 256.0, wz / 256.0);
                double chamber = noise.chamberNoise().noise(wx / 112.0, wz / 112.0);
                double flood = noise.floodNoise().noise(wx / 180.0, wz / 180.0);
                double strength = region * 0.62 + chamber * 0.38;
                if (strength < dramaticThreshold(style)) {
                    continue;
                }
                ChamberShape shape = shapeFor(style, strength, flood);
                carveColumn(chunkData, noise, style, shape, wx, wz, x, z, minY, maxY);
            }
        }
        // second pass for structures whose shape depends on the finished chamber ceiling/floor
        addLargeFeatures(worldInfo, chunkData, noise, chunkX, chunkZ, minY, maxY);
    }

    private static void carveColumn(ChunkData chunkData, NoiseSet noise, BiomeStyle style, ChamberShape shape, int wx, int wz, int x, int z, int minY, int maxY) {
        BiomeProfile profile = BiomeStyle.getProfile(style);
        double local3d = noise.chamberNoise().noise(wx / 42.0, shape.centerY() / 30.0, wz / 42.0);
        // the broad field controls the existence of the chamber; the 3D field only roughens the edge.
        double openness = shape.strength() * 0.78 + local3d * 0.22;
        if (openness < 0.18) {
            return;
        }
        double verticalRadius = shape.radiusY() * (0.82 + openness * 0.28);
        double verticalCenter = shape.centerY() + shape.verticalDrift() * 8.0;
        int bottom = Math.max(minY + 2, (int) Math.floor(verticalCenter - verticalRadius));
        int top = Math.min(maxY - 4, (int) Math.ceil(verticalCenter + verticalRadius));
        // never let a dramatic chamber punch through the existing enclosed roof
        double roofNoise = noise.decorationNoise().noise(wx / 10.0, wz / 10.0);
        int roofY = 120 + (int) (roofNoise * 8.0);
        top = Math.min(top, roofY - 10);
        if (top <= bottom + 3) {
            return;
        }
        for (int y = bottom; y <= top; y++) {
            double normalizedY = Math.abs(y - verticalCenter) / verticalRadius;
            double verticalFade = 1.0 - normalizedY * normalizedY;
            if (verticalFade <= 0.0) {
                continue;
            }
            double detail = noise.chamberNoise().noise(wx / 22.0, y / 18.0, wz / 22.0);
            double threshold = 0.10 - verticalFade * 0.12;
            if (detail + openness * 0.28 < threshold) {
                continue;
            }
            Material current = chunkData.getType(x, y, z);
            if (current == Material.BEDROCK || current == Material.LAVA) {
                continue;
            }
            chunkData.setBlock(x, y, z, Material.AIR);
        }
        if (!shape.flooded()) {
            int floor = findFloor(chunkData, x, z, bottom, Math.min(top, (int) verticalCenter));
            if (floor >= bottom && floor <= top) {
                chunkData.setBlock(x, floor, z, profile.floor());
            }
        }
    }

    private static void addLargeFeatures(WorldInfo worldInfo, ChunkData chunkData, NoiseSet noise, int chunkX, int chunkZ, int minY, int maxY) {
        int originX = chunkX << 4;
        int originZ = chunkZ << 4;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int wx = originX + x;
                int wz = originZ + z;
                BiomeStyle style = BiomeStyle.fromBiome(worldInfo.vanillaBiomeProvider().getBiome(worldInfo, wx, 64, wz));
                if (!isDramatic(style)) {
                    continue;
                }
                double region = noise.regionNoise().noise(wx / 256.0, wz / 256.0);
                double chamber = noise.chamberNoise().noise(wx / 112.0, wz / 112.0);
                double strength = region * 0.62 + chamber * 0.38;
                if (strength < dramaticThreshold(style)) {
                    continue;
                }
                if (style == BiomeStyle.JUNGLE || style == BiomeStyle.SWAMP) {
                    addRoots(chunkData, noise, x, z, wx, wz, minY, maxY);
                }
                if (isOcean(style)) {
                    addOceanDetails(chunkData, noise, x, z, wx, wz, minY, maxY);
                }
                if (style == BiomeStyle.MUSHROOM) {
                    addMushroomDetails(chunkData, noise, x, z, wx, wz, minY, maxY);
                }
                if (style == BiomeStyle.FROZEN || style == BiomeStyle.SNOWY || style == BiomeStyle.COLD_OCEAN) {
                    addIceDetails(chunkData, noise, x, z, wx, wz, minY, maxY);
                }
            }
        }
    }

    private static void addRoots(ChunkData chunkData, NoiseSet noise, int x, int z, int wx, int wz, int minY, int maxY) {
        double rootField = noise.rootNoise().noise(wx / 34.0, wz / 34.0);
        if (rootField < 0.28) {
            return;
        }
        int ceiling = findCeiling(chunkData, x, z, maxY);
        if (ceiling < 45 || ceiling > 115) {
            return;
        }
        double detail = noise.rootNoise().noise(wx / 11.0, wz / 11.0);
        int length = 5 + (int) ((detail + 1.0) * 0.5 * 19.0);
        length = Math.min(length, ceiling - minY - 4);
        if (length < 4) {
            return;
        }
        int thickness = rootField > 0.62 ? 2 : 1;
        for (int i = 1; i <= length; i++) {
            int y = ceiling - i;
            if (y <= minY + 2 || y >= maxY) {
                break;
            }
            placeRoot(chunkData, x, y, z, thickness);
            if (i > 2 && i % 3 == 0) {
                double branch = noise.rootNoise().noise((wx + i * 7) / 15.0, (wz - i * 5) / 15.0);
                if (branch > 0.35) {
                    int side = branch > 0.7 ? 2 : 1;
                    int bx = x + (branch > 0.55 ? side : -side);
                    if (bx >= 0 && bx < 16 && chunkData.getType(bx, y, z).isAir()) {
                        chunkData.setBlock(bx, y, z, Material.JUNGLE_LOG);
                        if (chunkData.getType(bx, y - 1, z).isAir()) {
                            chunkData.setBlock(bx, y - 1, z, Material.HANGING_ROOTS);
                        }
                    }
                }
            }
            if (i == length && chunkData.getType(x, y - 1, z).isAir()) {
                chunkData.setBlock(x, y, z, Material.HANGING_ROOTS);
            }
        }
    }

    private static void placeRoot(ChunkData chunkData, int x, int y, int z, int thickness) {
        if (chunkData.getType(x, y, z).isAir()) {
            chunkData.setBlock(x, y, z, Material.JUNGLE_LOG);
        }
        if (thickness > 1) {
            if (x + 1 < 16 && chunkData.getType(x + 1, y, z).isAir()) {
                chunkData.setBlock(x + 1, y, z, Material.JUNGLE_LOG);
            }
            if (z + 1 < 16 && chunkData.getType(x, y, z + 1).isAir()) {
                chunkData.setBlock(x, y, z + 1, Material.JUNGLE_LOG);
            }
        }
    }

    private static void addOceanDetails(ChunkData chunkData, NoiseSet noise, int x, int z, int wx, int wz, int minY, int maxY) {
        int floor = findWaterFloor(chunkData, x, z, minY, maxY);
        if (floor < minY || floor > 80) {
            return;
        }
        double shelf = noise.floodNoise().noise(wx / 28.0, wz / 28.0);
        if (shelf > 0.35 && floor + 1 <= maxY && chunkData.getType(x, floor + 1, z) == Material.WATER) {
            chunkData.setBlock(x, floor, z, Material.GRAVEL);
            if (noise.floodNoise().noise(wx / 9.0, wz / 9.0) > 0.25) {
                chunkData.setBlock(x, floor + 1, z, Material.SEAGRASS);
            }
        }
        if (noise.floodNoise().noise(wx / 55.0, wz / 55.0) > 0.72 && floor + 1 < maxY && chunkData.getType(x, floor + 1, z) == Material.WATER) {
            chunkData.setBlock(x, floor, z, Material.SEA_LANTERN);
        }
    }

    private static void addMushroomDetails(ChunkData chunkData, NoiseSet noise, int x, int z, int wx, int wz, int minY, int maxY) {
        int floor = findFloor(chunkData, x, z, minY, maxY);
        if (floor < minY || floor + 1 >= maxY || chunkData.getType(x, floor + 1, z) != Material.AIR) {
            return;
        }
        double n = noise.rootNoise().noise(wx / 24.0, wz / 24.0);
        if (n > 0.50) {
            chunkData.setBlock(x, floor, z, Material.MYCELIUM);
            if (noise.rootNoise().noise(wx / 8.0, wz / 8.0) > 0.45) {
                chunkData.setBlock(x, floor + 1, z, noise.rootNoise().noise(wx / 5.0, wz / 5.0) > 0 ? Material.RED_MUSHROOM : Material.BROWN_MUSHROOM);
            }
        }
    }

    private static void addIceDetails(ChunkData chunkData, NoiseSet noise, int x, int z, int wx, int wz, int minY, int maxY) {
        int ceiling = findCeiling(chunkData, x, z, maxY);
        if (ceiling < 45 || ceiling > 115) {
            return;
        }
        double n = noise.rootNoise().noise(wx / 18.0, wz / 18.0);
        if (n > 0.55 && chunkData.getType(x, ceiling - 1, z).isAir()) {
            int length = 3 + (int) ((n - 0.55) * 18.0);
            for (int i = 1; i <= length && ceiling - i > minY; i++) {
                if (chunkData.getType(x, ceiling - i, z).isAir()) {
                    chunkData.setBlock(x, ceiling - i, z, Material.PACKED_ICE);
                }
            }
        }
    }

    private static int findWaterFloor(ChunkData chunkData, int x, int z, int minY, int maxY) {
        boolean wasWater = false;
        for (int y = minY; y <= maxY; y++) {
            Material mat = chunkData.getType(x, y, z);
            if (mat == Material.WATER) {
                wasWater = true;
            } else if (wasWater && mat.isSolid()) {
                return y;
            } else if (wasWater && mat.isAir()) {
                return -1;
            }
        }
        return -1;
    }

    private static int findFloor(ChunkData chunkData, int x, int z, int minY, int maxY) {
        boolean wasAir = false;
        for (int y = minY; y <= maxY; y++) {
            Material mat = chunkData.getType(x, y, z);
            if (mat.isAir()) {
                wasAir = true;
            } else if (wasAir) {
                return y;
            }
        }
        return -1;
    }

    private static int findCeiling(ChunkData chunkData, int x, int z, int maxY) {
        boolean wasAir = false;
        for (int y = maxY; y >= 20; y--) {
            Material mat = chunkData.getType(x, y, z);
            if (mat.isAir()) {
                wasAir = true;
            } else if (wasAir) {
                return y;
            }
        }
        return -1;
    }

    private static boolean isOcean(BiomeStyle style) {
        return style == BiomeStyle.OCEAN || style == BiomeStyle.COLD_OCEAN || style == BiomeStyle.WARM_OCEAN;
    }

    private static boolean isDramatic(BiomeStyle style) {
        return switch (style) {
            case JUNGLE, OCEAN, COLD_OCEAN, WARM_OCEAN, SWAMP, MUSHROOM, FROZEN, SNOWY, TAIGA, FOREST, LUSH, DESERT,
                 BADLANDS, SCULK, CHERRY, PALE, DRIPSTONE -> true;
            default -> false;
        };
    }

    private static double dramaticThreshold(BiomeStyle style) {
        return switch (style) {
            // oceans are intentionally common because their regional identity is the flooded mega cavern itself
            case OCEAN, COLD_OCEAN, WARM_OCEAN -> 0.08;
            case JUNGLE, MUSHROOM, SWAMP -> 0.16;
            case SCULK, FROZEN, SNOWY -> 0.20;
            case DESERT, BADLANDS, DRIPSTONE -> 0.24;
            default -> 0.30;
        };
    }

    private static ChamberShape shapeFor(BiomeStyle style, double strength, double flood) {
        double variation = Math.clamp((strength + 1.0) * 0.5, 0.0, 1.0);
        double drift = flood * 0.45;
        return switch (style) {
            // very large vertical space: intentionally compatible with tall trees produced later by CaveTreePopulator
            case JUNGLE -> new ChamberShape(77 + flood * 8, 31 + variation * 10, false, 0, drift, strength);
            case OCEAN, COLD_OCEAN, WARM_OCEAN -> new ChamberShape(58 + flood * 5, 34 + variation * 12, true, 46 + flood * 4, drift, strength);
            case SWAMP -> new ChamberShape(61 + flood * 6, 26 + variation * 8, true, 57 + flood * 5, drift, strength);
            case MUSHROOM -> new ChamberShape(76, 35 + variation * 10, false, 0, drift, strength);
            case FROZEN, SNOWY -> new ChamberShape(72, 31 + variation * 9, false, 0, drift, strength);
            case SCULK -> new ChamberShape(54, 28 + variation * 9, false, 0, drift, strength);
            case DESERT, BADLANDS -> new ChamberShape(69 + flood * 5, 24 + variation * 7, false, 0, drift, strength);
            case TAIGA, FOREST, LUSH, CHERRY, PALE, DRIPSTONE -> new ChamberShape(75 + flood * 5, 27 + variation * 9, false, 0, drift, strength);
            default -> new ChamberShape(70, 23, false, 0, drift, strength);
        };
    }
}
