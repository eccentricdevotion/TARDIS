package me.eccentric_nz.tardischunkgenerator.worldgen;

import me.eccentric_nz.tardischunkgenerator.worldgen.caves.BiomeProfile;
import me.eccentric_nz.tardischunkgenerator.worldgen.caves.BiomeStyle;
import me.eccentric_nz.tardischunkgenerator.worldgen.caves.NoiseCache;
import me.eccentric_nz.tardischunkgenerator.worldgen.caves.NoiseSet;
import me.eccentric_nz.tardischunkgenerator.worldgen.populators.CaveTreePopulator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.PerlinNoiseGenerator;

import java.util.List;
import java.util.Random;

public class CaveGenerator extends ChunkGenerator {

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        NoiseSet noise = NoiseCache.getNoiseSet(worldInfo.getSeed());
        int originX = chunkX << 4;
        int originZ = chunkZ << 4;
        int generationMinY = Math.max(chunkData.getMinHeight(), 0);
        int generationMaxY = Math.min(chunkData.getMaxHeight(), 129);
        this.generateBedrockLayer(worldInfo, chunkData, random, originX, originZ);
        this.generateDeepShrine(chunkData, random);
        this.generateFallenResearcher(chunkData, random);
        this.generateSealedPortal(chunkData, random);
        boolean[][] lavaBase = new boolean[16][16];
        boolean[][][] directLavaArea = new boolean[16][129][16];
        boolean[][][] burntZone = new boolean[16][129][16];
        boolean[][][] lavaVolume = new boolean[16][129][16];
        boolean[][] lavaFloorArea = new boolean[16][16];
        for (int wx = -16; wx <= 32; ++wx) {
            for (int wz = -16; wz <= 32; ++wz) {
                int worldX = originX + wx;
                int worldZ = originZ + wz;
                double lakeValue = Math.abs(noise.lavaLakeNoise().noise((double) worldX / (double) 60.0F, (double) worldZ / (double) 60.0F));
                double shapeValue = Math.abs(noise.lavaShapeNoise().noise((double) worldX / (double) 25.0F, (double) worldZ / (double) 25.0F));
                double centerValue = Math.abs(noise.lavaCenterNoise().noise((double) worldX / (double) 100.0F, (double) worldZ / (double) 100.0F));
                double detailValue = Math.abs(noise.lavaDetailNoise().noise((double) worldX / (double) 12.0F, (double) worldZ / (double) 12.0F));
                double edgeValue = Math.abs(noise.lavaEdgeNoise().noise((double) worldX / (double) 8.0F, (double) worldZ / (double) 8.0F));
                double combinedValue = lakeValue * 0.6 + shapeValue * 0.3 + detailValue * 0.1;
                if (lakeValue > 0.45 && centerValue > (double) 0.5F) {
                    int baseRadius = 5 + (int) (centerValue * (double) 10.0F);
                    for (int dx = -baseRadius - 2; dx <= baseRadius + 2; ++dx) {
                        for (int dz = -baseRadius - 2; dz <= baseRadius + 2; ++dz) {
                            int lx = wx + dx;
                            int lz = wz + dz;
                            if (lx >= 0 && lx < 16 && lz >= 0 && lz < 16) {
                                double distance = Math.sqrt(dx * dx + dz * dz);
                                double radiusModifier = (double) 1.0F + (detailValue - (double) 0.5F) * (double) 0.5F + (edgeValue - (double) 0.5F) * 0.3;
                                double effectiveRadius = (double) baseRadius * radiusModifier;
                                if (distance <= effectiveRadius * 0.8) {
                                    lavaBase[lx][lz] = true;
                                    lavaFloorArea[lx][lz] = true;
                                } else if (distance <= effectiveRadius) {
                                    double fadeOutFactor = (double) 1.0F - (distance - effectiveRadius * 0.8) / (effectiveRadius * 0.2);
                                    double randVal = random.nextDouble();
                                    if (randVal < fadeOutFactor * 0.8) {
                                        lavaBase[lx][lz] = true;
                                    }
                                    lavaFloorArea[lx][lz] = true;
                                }
                            }
                        }
                    }
                }
                if (wx >= 0 && wx < 16 && wz >= 0 && wz < 16 && combinedValue > 0.4 && !lavaBase[wx][wz] && (edgeValue > 0.7 && detailValue > 0.6 || combinedValue > 0.45 && edgeValue > 0.55)) {
                    lavaBase[wx][wz] = true;
                    lavaFloorArea[wx][wz] = true;
                }
            }
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                if (lavaBase[x][z]) {
                    int worldX = originX + x;
                    int worldZ = originZ + z;
                    double heightVar = noise.lavaDetailNoise().noise((double) worldX / (double) 8.0F, (double) worldZ / (double) 8.0F) * (double) 0.5F;
                    int baseHeight = 8;
                    int topHeight = heightVar < (double) 0.0F ? 9 : 10;
                    for (int y = baseHeight; y <= topHeight; ++y) {
                        lavaVolume[x][y][z] = true;
                    }
                    for (int ly = topHeight + 1; ly <= 20; ++ly) {
                        lavaVolume[x][ly][z] = true;
                    }
                    int directRadius = 3 + (int) (Math.abs(noise.lavaDetailNoise().noise((double) worldX / (double) 15.0F, (double) worldZ / (double) 15.0F)) * (double) 2.0F);
                    int burnRadius = 10 + (int) (Math.abs(noise.lavaEdgeNoise().noise((double) worldX / (double) 20.0F, (double) worldZ / (double) 20.0F)) * (double) 4.0F);
                    for (int dx = -burnRadius; dx <= burnRadius; ++dx) {
                        for (int dz = -burnRadius; dz <= burnRadius; ++dz) {
                            for (int dy = -4; dy <= 10; ++dy) {
                                int nx = x + dx;
                                int nz = z + dz;
                                int ny = baseHeight + dy;
                                if (nx >= 0 && nx < 16 && nz >= 0 && nz < 16 && ny >= 4 && ny <= 20) {
                                    double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                                    double noiseFactor = (double) 1.0F + noise.lavaDetailNoise().noise((double) worldX / (double) 10.0F + (double) dx / (double) 5.0F, (double) worldZ / (double) 10.0F + (double) dz / (double) 5.0F) * 0.3;
                                    if (distance < (double) directRadius * noiseFactor) {
                                        directLavaArea[nx][ny][nz] = true;
                                        lavaFloorArea[nx][nz] = true;
                                    } else if (distance < (double) burnRadius * noiseFactor) {
                                        double probability = (double) 1.0F - (distance - (double) directRadius * noiseFactor) / ((double) burnRadius * noiseFactor - (double) directRadius * noiseFactor);
                                        if (random.nextDouble() < probability * 0.9) {
                                            burntZone[nx][ny][nz] = true;
                                            lavaFloorArea[nx][nz] = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                if (lavaFloorArea[x][z]) {
                    for (int y = 1; y <= 7; ++y) {
                        if (chunkData.getType(x, y, z) == Material.AIR) {
                            double randVal = random.nextDouble();
                            Material material;
                            if (randVal < 0.65) {
                                material = Material.DEEPSLATE;
                            } else if (randVal < 0.8) {
                                material = Material.BLACKSTONE;
                            } else if (randVal < 0.92) {
                                material = Material.BASALT;
                            } else if (randVal < 0.97) {
                                material = Material.MAGMA_BLOCK;
                            } else {
                                material = Material.SMOOTH_BASALT;
                            }
                            chunkData.setBlock(x, y, z, material);
                        }
                    }
                }
            }
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = generationMinY; y < generationMaxY; ++y) {
                    int worldX = originX + x;
                    int worldZ = originZ + z;
                    double density = getCaveNoise(noise.caveNoise(), worldX, y, worldZ);
                    double decoNoise = noise.decorationNoise().noise((double) worldX / (double) 10.0F, (double) worldZ / (double) 10.0F);
                    double tunnelVal = noise.tunnelNoise().noise((double) worldX / (double) 25.0F, (double) y / (double) 25.0F, (double) worldZ / (double) 25.0F);
                    if (!lavaVolume[x][y][z]) {
                        int roofY = 120 + (int) (decoNoise * (double) 8.0F);
                        BiomeStyle biomeStyle = BiomeStyle.fromBiome(getVanillaBiome(worldInfo, worldX, y, worldZ));
                        BiomeProfile biomeProfile = BiomeStyle.getProfile(biomeStyle);
                        if (y >= roofY) {
                            chunkData.setBlock(x, y, z, biomeProfile.ceiling());
                        } else if (y == 0) {
                            chunkData.setBlock(x, y, z, Material.BEDROCK);
                        } else if (!(tunnelVal > 0.2) || !(tunnelVal < (double) 0.25F)) {
                            if (density > 0.05) {
                                chunkData.setBlock(x, y, z, biomeProfile.wall());
                                if (y < 30 && random.nextDouble() < 0.01) {
                                    chunkData.setBlock(x, y, z, Material.DEEPSLATE_COAL_ORE);
                                }
                                if (y < 25 && decoNoise > (double) 0.5F && random.nextDouble() < 0.1) {
                                    chunkData.setBlock(x, y, z, Material.DEEPSLATE_IRON_ORE);
                                }
                                if (chunkData.getType(x, y + 1, z) == Material.AIR && random.nextDouble() < 0.005) {
                                    chunkData.setBlock(x, y + 1, z, Material.SCULK_SENSOR);
                                }
                                if (chunkData.getType(x, y + 1, z) == Material.AIR && random.nextDouble() < 0.1) {
                                    chunkData.setBlock(x, y + 1, z, biomeProfile.featureOne());
                                }
                                if (chunkData.getType(x, y + 1, z) == Material.AIR && random.nextDouble() < 0.1) {
                                    chunkData.setBlock(x, y + 1, z, biomeProfile.featureTwo());
                                }
                                if (random.nextDouble() < 0.002 && y < 20) {
                                    chunkData.setBlock(x, y, z, Material.DEEPSLATE_DIAMOND_ORE);
                                }
                                if (random.nextDouble() < 0.001 && chunkData.getType(x, y - 1, z).isSolid()) {
                                    chunkData.setBlock(x, y, z, Material.SCULK_CATALYST);
                                }
                            } else {
                                if (chunkData.getType(x, y + 1, z) == biomeProfile.wall() && random.nextDouble() < 0.002) {
                                    chunkData.setBlock(x, y, z, Material.GLOWSTONE);
                                }
                                if (chunkData.getType(x, y - 1, z).isSolid() && y < 20 && random.nextDouble() < 0.005) {
                                    chunkData.setBlock(x, y, z, Material.SCULK);
                                }
                                if (y > 1 && y < 30 && random.nextDouble() < 0.002 && chunkData.getType(x, y - 1, z).isSolid() && chunkData.getType(x, y, z) == Material.AIR) {
                                    chunkData.setBlock(x, y, z, Material.SCULK_VEIN);
                                }
                            }
                            double herdNoise = noise.sculkHerdNoise().noise((double) worldX / (double) 30.0F, (double) worldZ / (double) 30.0F);
                            if (herdNoise > 0.6 && y >= 12 && y <= 20 && random.nextDouble() < herdNoise - (double) 0.5F) {
                                chunkData.setBlock(x, y, z, Material.SCULK);
                                if (random.nextDouble() < 0.1 && chunkData.getType(x, y + 1, z) == Material.AIR) {
                                    chunkData.setBlock(x, y + 1, z, Material.SCULK_VEIN);
                                }
                                if (random.nextDouble() < 0.05 && chunkData.getType(x, y + 1, z) == Material.AIR) {
                                    chunkData.setBlock(x, y + 1, z, Material.SCULK_SHRIEKER);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 20; y >= 4; --y) {
                    if ((directLavaArea[x][y][z] || burntZone[x][y][z]) && chunkData.getType(x, y, z) == Material.AIR && !lavaVolume[x][y][z]) {
                        int supportY;
                        supportY = y - 1;
                        while (supportY > 0 && chunkData.getType(x, supportY, z) == Material.AIR) {
                            --supportY;
                        }
                        if (supportY < y - 1) {
                            double supportRand = random.nextDouble();
                            Material material;
                            if (directLavaArea[x][y][z]) {
                                if (supportRand < 0.4) {
                                    material = Material.BLACKSTONE;
                                } else if (supportRand < 0.7) {
                                    material = Material.BASALT;
                                } else if (supportRand < 0.9) {
                                    material = Material.POLISHED_BLACKSTONE;
                                } else {
                                    material = Material.MAGMA_BLOCK;
                                }
                            } else if (supportRand < (double) 0.5F) {
                                material = Material.DEEPSLATE;
                            } else if (supportRand < (double) 0.75F) {
                                material = Material.BLACKSTONE;
                            } else if (supportRand < 0.9) {
                                material = Material.BASALT;
                            } else {
                                material = Material.SMOOTH_BASALT;
                            }
                            for (int sy = supportY + 1; sy < y; ++sy) {
                                chunkData.setBlock(x, sy, z, material);
                            }
                        }
                    }
                }
            }
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 4; y <= 20; ++y) {
                    if (directLavaArea[x][y][z] && chunkData.getType(x, y, z) == Material.AIR && !lavaVolume[x][y][z] && random.nextDouble() < 0.98) {
                        double randVal = random.nextDouble();
                        Material material;
                        if (randVal < 0.3) {
                            material = Material.BLACKSTONE;
                        } else if (randVal < 0.6) {
                            material = Material.BASALT;
                        } else if (randVal < 0.85) {
                            material = Material.POLISHED_BLACKSTONE;
                        } else if (randVal < 0.95) {
                            material = Material.MAGMA_BLOCK;
                        } else {
                            material = Material.ANCIENT_DEBRIS;
                        }
                        chunkData.setBlock(x, y, z, material);
                    }
                }
            }
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 4; y <= 20; ++y) {
                    if (burntZone[x][y][z] && chunkData.getType(x, y, z) == Material.AIR && !lavaVolume[x][y][z] && random.nextDouble() < 0.9) {
                        double randVal = random.nextDouble();
                        Material material;
                        if (randVal < 0.4) {
                            material = Material.DEEPSLATE;
                        } else if (randVal < 0.7) {
                            material = Material.BLACKSTONE;
                        } else if (randVal < 0.85) {
                            material = Material.COBBLED_DEEPSLATE;
                        } else if (randVal < 0.95) {
                            material = Material.TUFF;
                        } else if (randVal < 0.99) {
                            material = Material.SMOOTH_BASALT;
                        } else {
                            material = Material.GILDED_BLACKSTONE;
                        }
                        chunkData.setBlock(x, y, z, material);
                    }
                }
            }
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 4; y <= 20; ++y) {
                    if (burntZone[x][y][z]) {
                        int[][] offsets = new int[][]{{-1, 0, 0}, {1, 0, 0}, {0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {0, 0, 1}};
                        for (int[] offset : offsets) {
                            int nx = x + offset[0];
                            int ny = y + offset[1];
                            int nz = z + offset[2];
                            if (nx >= 0 && nx < 16 && ny >= 4 && ny <= 20 && nz >= 0 && nz < 16 && !burntZone[nx][ny][nz] && chunkData.getType(nx, ny, nz) == Material.AIR && !lavaVolume[nx][ny][nz]) {
                                chunkData.setBlock(nx, ny, nz, Material.DEEPSLATE);
                            }
                        }
                    }
                }
            }
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                if (lavaBase[x][z]) {
                    if (!chunkData.getType(x, 7, z).isSolid()) {
                        chunkData.setBlock(x, 7, z, Material.DEEPSLATE);
                    }
                    int worldX = originX + x;
                    int worldZ = originZ + z;
                    double depthVar = noise.lavaDetailNoise().noise((double) worldX / (double) 10.0F, (double) worldZ / (double) 10.0F);
                    chunkData.setBlock(x, 8, z, Material.LAVA);
                    chunkData.setBlock(x, 9, z, Material.LAVA);
                    if (depthVar > 0.4) {
                        chunkData.setBlock(x, 10, z, Material.LAVA);
                    }
                    if (depthVar < (double) 0.0F || random.nextDouble() < 0.4) {
                        int maxDepth = 1 + random.nextInt(3) + (depthVar < -0.3 ? 1 : 0);
                        for (int depth = 1; depth <= maxDepth; ++depth) {
                            if (8 - depth > 0) {
                                chunkData.setBlock(x, 8 - depth, z, Material.LAVA);
                            }
                        }
                    }
                }
            }
        }
        boolean skulkGenerated = false;
        int centerX = 0;
        int centerZ = 0;
        int minY = 0;
        int height = 1;
        if (random.nextDouble() < 0.12) {
            centerX = 4 + random.nextInt(8);
            centerZ = 4 + random.nextInt(8);
            minY = 6 + random.nextInt(6);
            int maxY = 110 + random.nextInt(15);
            height = maxY - minY;
            for (int y = minY; y <= maxY; ++y) {
                double progress = (double) (y - minY) / (double) height;
                double radialTaper = Math.sin(progress * Math.PI);
                double baseRadius = 1.8 + radialTaper * 4.2;
                for (int dx = -6; dx <= 6; ++dx) {
                    for (int dz = -6; dz <= 6; ++dz) {
                        double dist = Math.sqrt(dx * dx + dz * dz);
                        if (!(dist > baseRadius)) {
                            int x = centerX + dx;
                            int z = centerZ + dz;
                            if (x >= 0 && x < 16 && z >= 0 && z < 16) {
                                Material current = chunkData.getType(x, y, z);
                                if (current == Material.AIR || this.isSkulkRootTarget(current)) {
                                    chunkData.setBlock(x, y, z, Material.SCULK);
                                    if (y + 1 < 256 && chunkData.getType(x, y + 1, z) == Material.AIR && random.nextDouble() < 0.15) {
                                        chunkData.setBlock(x, y + 1, z, Material.SCULK_VEIN);
                                    }
                                    if (random.nextDouble() < 0.006 && y > 12 && y < 100) {
                                        chunkData.setBlock(x, y, z, Material.SCULK_SHRIEKER);
                                    } else if (random.nextDouble() < 0.01 && y < 30) {
                                        chunkData.setBlock(x, y, z, Material.SCULK_CATALYST);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            skulkGenerated = true;
        }
        if (skulkGenerated) {
            int tendrils = 4 + random.nextInt(5);
            for (int i = 0; i < tendrils; ++i) {
                int length = 6 + random.nextInt(6);
                double angle = random.nextDouble() * Math.PI * (double) 2.0F;
                double dx = Math.cos(angle);
                double dz = Math.sin(angle);
                int cx = centerX;
                int cz = centerZ;
                int cy = minY + 5 + random.nextInt(Math.max(1, height - 10));
                for (int step = 0; step < length; ++step) {
                    cx += (int) Math.round(dx);
                    cz += (int) Math.round(dz);
                    cy += random.nextInt(3) - 1;
                    if (cx >= 0 && cx < 16 && cz >= 0 && cz < 16 && cy >= 4 && cy < 120) {
                        Material current = chunkData.getType(cx, cy, cz);
                        if (current == Material.AIR || this.isSkulkRootTarget(current)) {
                            chunkData.setBlock(cx, cy, cz, Material.SCULK_VEIN);
                            if (random.nextDouble() < 0.07 && chunkData.getType(cx, cy - 1, cz).isSolid()) {
                                chunkData.setBlock(cx, cy, cz, Material.SCULK_SENSOR);
                            }
                            if (random.nextDouble() < 0.12) {
                                int ex = cx + random.nextInt(3) - 1;
                                int ez = cz + random.nextInt(3) - 1;
                                int ey = cy + random.nextInt(3) - 1;
                                if (ex >= 0 && ex < 16 && ez >= 0 && ez < 16 && ey >= 4 && ey < 120 && chunkData.getType(ex, ey, ez) == Material.AIR) {
                                    chunkData.setBlock(ex, ey, ez, Material.SCULK_VEIN);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                if (lavaFloorArea[x][z]) {
                    for (int y = 4; y <= 12; ++y) {
                        if (chunkData.getType(x, y, z) == Material.AIR) {
                            int solidNeighbors = 0;
                            if (x > 0 && chunkData.getType(x - 1, y, z).isSolid()) {
                                ++solidNeighbors;
                            }
                            if (x < 15 && chunkData.getType(x + 1, y, z).isSolid()) {
                                ++solidNeighbors;
                            }
                            if (z > 0 && chunkData.getType(x, y, z - 1).isSolid()) {
                                ++solidNeighbors;
                            }
                            if (z < 15 && chunkData.getType(x, y, z + 1).isSolid()) {
                                ++solidNeighbors;
                            }
                            if (y > 0 && chunkData.getType(x, y - 1, z).isSolid()) {
                                ++solidNeighbors;
                            }
                            if (y < 128 && chunkData.getType(x, y + 1, z).isSolid()) {
                                ++solidNeighbors;
                            }
                            if (solidNeighbors >= 4 || y <= 7 && solidNeighbors >= 3) {
                                double randVal = random.nextDouble();
                                Material material;
                                if (randVal < (double) 0.5F) {
                                    material = Material.DEEPSLATE;
                                } else if (randVal < (double) 0.75F) {
                                    material = Material.BLACKSTONE;
                                } else if (randVal < 0.95) {
                                    material = Material.TUFF;
                                } else {
                                    material = Material.MAGMA_BLOCK;
                                }
                                chunkData.setBlock(x, y, z, material);
                            }
                        }
                    }
                }
            }
        }
        int centerX1 = 0;
        int centerZ1 = 0;
        int waterY = 0;
        boolean hotSpringGenerated = false;
        if (random.nextDouble() < 0.05) {
            centerX1 = 4 + random.nextInt(8);
            centerZ1 = 4 + random.nextInt(8);
            waterY = 10 + random.nextInt(10);
            boolean hasSolidGround = false;
            for (int y = waterY - 1; y >= 2; --y) {
                Material mat = chunkData.getType(centerX1, y, centerZ1);
                if (mat.isSolid()) {
                    hasSolidGround = true;
                    break;
                }
            }
            if (hasSolidGround) {
                hotSpringGenerated = true;
                for (int dx = -4; dx <= 4; ++dx) {
                    for (int dz = -4; dz <= 4; ++dz) {
                        int x = centerX1 + dx;
                        int z = centerZ1 + dz;
                        if (x >= 0 && x < 16 && z >= 0 && z < 16) {
                            double dist = Math.sqrt(dx * dx + dz * dz);
                            boolean isEdge = dist >= (double) 3.5F && dist <= 4.2;
                            boolean isInner = dist <= 3.2;
                            if (isEdge) {
                                for (int y = waterY - 2; y <= waterY + 1; ++y) {
                                    Material material;
                                    switch (random.nextInt(4)) {
                                        case 0 -> material = Material.TUFF;
                                        case 1 -> material = Material.COBBLED_DEEPSLATE;
                                        case 2 -> material = Material.DEEPSLATE;
                                        default -> material = Material.BASALT;
                                    }
                                    Material wallMat = material;
                                    chunkData.setBlock(x, y, z, wallMat);
                                }
                            }
                            if (isInner) {
                                if (waterY - 2 >= 0) {
                                    chunkData.setBlock(x, waterY - 2, z, Material.BASALT);
                                }
                                if (waterY - 1 >= 0) {
                                    chunkData.setBlock(x, waterY - 1, z, Material.MAGMA_BLOCK);
                                }
                                chunkData.setBlock(x, waterY, z, Material.WATER);
                                if (dist >= (double) 2.5F && random.nextDouble() < 0.4 && waterY + 1 <= 128) {
                                    chunkData.setBlock(x, waterY + 1, z, Material.WATER);
                                }
                                if (random.nextDouble() < 0.1 && waterY + 1 <= 128) {
                                    chunkData.setBlock(x, waterY + 1, z, Material.GLOW_LICHEN);
                                }
                                if (random.nextDouble() < 0.07 && waterY + 4 < 128) {
                                    chunkData.setBlock(x, waterY + 5, z, Material.DEEPSLATE);
                                    chunkData.setBlock(x, waterY + 4, z, Material.SPORE_BLOSSOM);
                                }
                                if (dx == 0 && dz == 0 && waterY - 3 >= 0) {
                                    chunkData.setBlock(x, waterY - 3, z, Material.STRUCTURE_VOID);
                                }
                            }
                        }
                    }
                }
                if (random.nextDouble() < (double) 0.25F) {
                    int lx = centerX1 + 4;
                    if (lx >= 0 && lx < 16 && centerZ1 >= 0 && centerZ1 < 16 && waterY - 1 >= 0) {
                        chunkData.setBlock(lx, waterY - 1, centerZ1, Material.LAVA);
                    }
                }
            }
        }
        if (hotSpringGenerated) {
            for (int dx = -4; dx <= 4; ++dx) {
                for (int dz = -4; dz <= 4; ++dz) {
                    int x = centerX1 + dx;
                    int z = centerZ1 + dz;
                    if (x >= 0 && x < 16 && z >= 0 && z < 16) {
                        double dist = Math.sqrt(dx * dx + dz * dz);
                        if (!(dist > 3.2)) {
                            for (int y = waterY - 1; y >= 1; --y) {
                                Material current = chunkData.getType(x, y, z);
                                if (current == Material.AIR || !current.isSolid()) {
                                    Material mat;
                                    if (y <= 5) {
                                        mat = Material.BEDROCK;
                                    } else if (y <= 20) {
                                        mat = Material.DEEPSLATE;
                                    } else if (y <= 50) {
                                        mat = Material.TUFF;
                                    } else if (random.nextDouble() < 0.4) {
                                        mat = Material.BLACKSTONE;
                                    } else {
                                        mat = Material.COBBLED_DEEPSLATE;
                                    }

                                    chunkData.setBlock(x, y, z, mat);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Reinterpret exposed cave surfaces using the real vanilla biome map.
        // This happens after the cave/lava/special-feature passes so those
        // features keep their identity and are not overwritten.
        applyVanillaBiomeFeatures(worldInfo, random, chunkX, chunkZ, chunkData);
    }

    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Terrain is fully generated in generateNoise().
    }

    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Bedrock is intentionally generated during generateNoise() to preserve
        // the ordering of the original generator.
    }

    @Override
    public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Custom cave carving is part of generateNoise().
    }

    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        // null means Paper uses the world's normal vanilla biome provider.
        // We deliberately do not replace Minecraft's biome distribution.
        return null;
    }

    /* These don't need to be included if they return false as that is the default in the super class
    *
    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }
     */

    @Override
    public boolean shouldGenerateMobs() {
        // Let normal biome-aware mob spawning remain enabled.
        return true;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> populators = super.getDefaultPopulators(world);
        populators.add(new CaveTreePopulator());
        return populators;
    }

    private Biome getVanillaBiome(WorldInfo worldInfo, int x, int y, int z) {
        return worldInfo.vanillaBiomeProvider().getBiome(worldInfo, x, y, z);
    }

    private void applyVanillaBiomeFeatures(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        int originX = chunkX << 4;
        int originZ = chunkZ << 4;
        int minY = Math.max(chunkData.getMinHeight(), 4);
        int maxY = Math.min(chunkData.getMaxHeight() - 2, 127);
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int worldX = originX + x;
                int worldZ = originZ + z;
                // Sample the vanilla surface biome rather than the underground
                // biome noise so the entire cavern region follows the surface biome.
                Biome biome = getVanillaBiome(worldInfo, worldX, 64, worldZ);
                BiomeStyle style = BiomeStyle.fromBiome(biome);
                BiomeProfile profile = BiomeStyle.getProfile(style);
                for (int y = minY; y <= maxY; ++y) {
                    Material current = chunkData.getType(x, y, z);
                    if (!isCaveRock(current)) {
                        continue;
                    }
                    boolean airAbove = chunkData.getType(x, y + 1, z) == Material.AIR;
                    boolean exposedSide = (x > 0 && chunkData.getType(x - 1, y, z) == Material.AIR) || (x < 15 && chunkData.getType(x + 1, y, z) == Material.AIR) || (z > 0 && chunkData.getType(x, y, z - 1) == Material.AIR) || (z < 15 && chunkData.getType(x, y, z + 1) == Material.AIR);
                    if (airAbove) {
                        chunkData.setBlock(x, y, z, profile.floor());
                    } else if (exposedSide && random.nextDouble() < 0.18) {
                        chunkData.setBlock(x, y, z, profile.accent());
                    }
                    if (airAbove && y + 2 < chunkData.getMaxHeight() && chunkData.getType(x, y + 2, z) == Material.AIR) {
                        placeBiomeFloorFeature(style, profile, chunkData, random, x, y + 1, z);
                    }
                }
            }
        }
        // A second pass adds hanging features to cave ceilings without touching
        // the actual roof shell.
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int worldX = originX + x;
                int worldZ = originZ + z;
                BiomeStyle style = BiomeStyle.fromBiome(getVanillaBiome(worldInfo, worldX, 64, worldZ));
                if (style == BiomeStyle.LUSH || style == BiomeStyle.JUNGLE || style == BiomeStyle.SWAMP) {
                    for (int y = 15; y < Math.min(115, chunkData.getMaxHeight() - 3); ++y) {
                        if (chunkData.getType(x, y, z).isSolid() && chunkData.getType(x, y - 1, z) == Material.AIR && chunkData.getType(x, y - 2, z) == Material.AIR && random.nextDouble() < 0.004) {
                            chunkData.setBlock(x, y - 1, z, Material.GLOW_LICHEN);
                        }
                    }
                }
            }
        }
    }

    private void placeBiomeFloorFeature(BiomeStyle style, BiomeProfile profile, ChunkData chunkData, Random random, int x, int y, int z) {
        double chance = switch (style) {
            case JUNGLE, LUSH, FOREST, TAIGA, SWAMP, PALE -> 0.035;
            case MUSHROOM -> 0.045;
            case DESERT, BADLANDS, FROZEN, SNOWY -> 0.018;
            case OCEAN, COLD_OCEAN, WARM_OCEAN -> 0.012;
            case CHERRY, STONY, WINDSWEPT -> 0.025;
            case SCULK -> 0.01;
            case SAVANNA, SULFUR, DRIPSTONE -> 0.03;
            case CAVE -> 0.008;
        };
        if (random.nextDouble() >= chance) {
            return;
        }
        Material feature = random.nextBoolean() ? profile.featureOne() : profile.featureTwo();
        if (feature == Material.SEAGRASS) {
            return; // Seagrass needs water; don't create invalid dry plants.
        }
        if (feature == Material.BAMBOO) {
            feature = Material.MOSS_CARPET;
        }
        if (feature == Material.CHORUS_PLANT) {
            feature = Material.CHORUS_FLOWER;
        }
        chunkData.setBlock(x, y, z, feature);
    }

    private boolean isCaveRock(Material mat) {
        return mat == Material.DEEPSLATE || mat == Material.COBBLED_DEEPSLATE || mat == Material.TUFF || mat == Material.BLACKSTONE || mat == Material.BASALT || mat == Material.SMOOTH_BASALT || mat == Material.STONE || mat == Material.ANDESITE || mat == Material.DIORITE || mat == Material.GRANITE || mat == Material.CALCITE;
    }

    private void generateBedrockLayer(WorldInfo worldInfo, ChunkData chunkData, Random random, int worldX, int worldZ) {
        BiomeStyle biomeStyle = BiomeStyle.fromBiome(getVanillaBiome(worldInfo, worldX, 64, worldZ));
        BiomeProfile biomeProfile = BiomeStyle.getProfile(biomeStyle);
        for (int y = 0; y <= 3; ++y) {
            for (int x = 0; x < 16; ++x) {
                for (int z = 0; z < 16; ++z) {
                    if (y == 0) {
                        chunkData.setBlock(x, y, z, Material.BEDROCK);
                    } else {
                        double chance;
                        switch (y) {
                            case 1 -> chance = 0.85;
                            case 2 -> chance = 0.6;
                            case 3 -> chance = 0.3;
                            default -> chance = 0;
                        }
                        if (random.nextDouble() < chance) {
                            chunkData.setBlock(x, y, z, Material.BEDROCK);
                        } else {
                            double roll = random.nextDouble();
                            Material base = roll < (double) 0.25F ? biomeProfile.floor() : (roll < (double) 0.5F ? biomeProfile.wall() : (roll < 0.9 ? biomeProfile.ceiling() : biomeProfile.light()));
                            chunkData.setBlock(x, y, z, base);
                        }
                    }
                }
            }
        }
    }

    private void generateDeepShrine(ChunkData chunkData, Random random) {
        if (!(random.nextDouble() > 0.005)) {
            int centerX = 4 + random.nextInt(8);
            int centerZ = 4 + random.nextInt(8);
            int baseY = 3;
            for (int dx = -2; dx <= 2; ++dx) {
                for (int dz = -2; dz <= 2; ++dz) {
                    int x = centerX + dx;
                    int z = centerZ + dz;
                    chunkData.setBlock(x, baseY, z, Material.REINFORCED_DEEPSLATE);
                }
            }
            for (int dx = -2; dx <= 2; dx += 4) {
                for (int dz = -2; dz <= 2; dz += 4) {
                    int x = centerX + dx;
                    int z = centerZ + dz;
                    if (random.nextBoolean()) {
                        chunkData.setBlock(x, baseY + 1, z, Material.SOUL_FIRE);
                    } else {
                        chunkData.setBlock(x, baseY + 1, z, Material.CANDLE);
                    }
                }
            }
            chunkData.setBlock(centerX, baseY + 1, centerZ, Material.SCULK_SHRIEKER);
            if (random.nextDouble() < (double) 0.5F) {
                chunkData.setBlock(centerX, baseY + 2, centerZ, Material.SKELETON_SKULL);
            }
            chunkData.setBlock(centerX, baseY, centerZ, Material.SCULK);
            for (int i = 0; i < 4; ++i) {
                int dx = -3 + random.nextInt(7);
                int dz = -3 + random.nextInt(7);
                int x = centerX + dx;
                int z = centerZ + dz;
                if (random.nextBoolean()) {
                    chunkData.setBlock(x, baseY + 1, z, Material.SCULK_SENSOR);
                } else {
                    chunkData.setBlock(x, baseY + 1, z, Material.DEAD_BUSH);
                }
            }
        }
    }

    private void generateFallenResearcher(ChunkData chunkData, Random random) {
        if (!(random.nextDouble() > 0.005)) {
            int baseX = 4 + random.nextInt(8);
            int baseZ = 4 + random.nextInt(8);
            int baseY = 4;
            chunkData.setBlock(baseX, baseY, baseZ, Material.BLACKSTONE);
            chunkData.setBlock(baseX, baseY + 1, baseZ, Material.SKELETON_SKULL);
            if (random.nextBoolean()) {
                chunkData.setBlock(baseX + 1, baseY + 1, baseZ, Material.BOOKSHELF);
            } else {
                chunkData.setBlock(baseX + 1, baseY + 1, baseZ, Material.CANDLE);
            }
            chunkData.setBlock(baseX, baseY, baseZ + 1, Material.SCULK);
            if (random.nextDouble() < 0.3) {
                chunkData.setBlock(baseX, baseY + 1, baseZ + 1, Material.SCULK_SENSOR);
            }
        }
    }

    private void generateSealedPortal(ChunkData chunkData, Random random) {
        if (!(random.nextDouble() > 0.0025)) {
            int cx = 4 + random.nextInt(8);
            int cz = 4 + random.nextInt(8);
            int cy = 5;
            for (int dx = -1; dx <= 1; ++dx) {
                for (int dz = -1; dz <= 1; ++dz) {
                    if (Math.abs(dx) + Math.abs(dz) != 2) {
                        chunkData.setBlock(cx + dx, cy, cz + dz, Material.REINFORCED_DEEPSLATE);
                    }
                }
            }
            chunkData.setBlock(cx, cy, cz, Material.SCULK_SHRIEKER);
            chunkData.setBlock(cx, cy + 1, cz, Material.SCULK_VEIN);
            if (random.nextDouble() < 0.3) {
                chunkData.setBlock(cx, cy + 2, cz, Material.SOUL_FIRE);
            }
        }
    }

    private boolean isSkulkRootTarget(Material mat) {
        return mat == Material.DEEPSLATE || mat == Material.COBBLED_DEEPSLATE || mat == Material.TUFF || mat == Material.BLACKSTONE || mat == Material.BASALT;
    }

    private double getCaveNoise(PerlinNoiseGenerator caveNoise, int x, int y, int z) {
        double nx = (double) x / (double) 40.0F;
        double ny = (double) y / (double) 40.0F;
        double nz = (double) z / (double) 40.0F;
        double n1 = caveNoise.noise(nx, ny, nz);
        double n2 = caveNoise.noise(nx * (double) 2.0F, ny * (double) 2.0F, nz * (double) 2.0F) * (double) 0.5F;
        double n3 = caveNoise.noise(nx * (double) 4.0F, ny * (double) 4.0F, nz * (double) 4.0F) * (double) 0.25F;
        return n1 + n2 + n3;
    }
}