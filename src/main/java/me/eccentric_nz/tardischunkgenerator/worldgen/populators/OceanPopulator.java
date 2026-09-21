package me.eccentric_nz.tardischunkgenerator.worldgen.populators;

import me.eccentric_nz.tardischunkgenerator.custombiome.CubicMaterial;
import me.eccentric_nz.tardischunkgenerator.worldgen.caves.BiomeStyle;
import me.eccentric_nz.tardischunkgenerator.worldgen.caves.CaveCoral;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

public class OceanPopulator extends BlockPopulator {

    private final BlockData KELP = Material.KELP_PLANT.createBlockData();
    private final BlockData SEAGRASS = Material.SEAGRASS.createBlockData();
    private final BlockData OBSIDIAN = Material.OBSIDIAN.createBlockData();

    @Override
    public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion) {
        int sx = chunkX * 16;
        int sy = 6;
        int sz = chunkZ * 16;
        Biome biome = worldInfo.vanillaBiomeProvider().getBiome(worldInfo, sx + 8, sy, sz + 8);
        BiomeStyle style = BiomeStyle.fromBiome(biome);
        // only ocean biomes
        if (style != BiomeStyle.OCEAN && style != BiomeStyle.COLD_OCEAN && style != BiomeStyle.WARM_OCEAN) {
            return;
        }
        for (int h = 0; h < 11; h++) {
            for (int d = 0; d < 16; d++) {
                for (int w = 0; w < 16; w++) {
                    int tx = sx + d;
                    int ty = sy + h; // Ranges from y=6 to y=17
                    int tz = sz + w;
                    // use 'continue' instead of 'return' so out-of-bounds blocks don't abort the entire chunk
                    if (!limitedRegion.isInRegion(tx, ty, tz)) {
                        continue;
                    }
                    BlockData data = limitedRegion.getBlockData(tx, ty, tz);
                    // plant kelp/seagrass if resting on solid ground
                    if (CubicMaterial.cubes.contains(limitedRegion.getType(tx, ty - 1, tz)) && random.nextFloat() < 0.1f) {
                        float which = random.nextFloat();
                        if (which < 0.75f) {
                            limitedRegion.setBlockData(tx, ty, tz, SEAGRASS);
                        } else {
                            if (style == BiomeStyle.WARM_OCEAN) {
                                CaveCoral.placeBlocks(limitedRegion, random, tx, ty, tz);
                            } else {
                                limitedRegion.setBlockData(tx, ty, tz, KELP);
                            }
                        }
                    }
                    // turn lava to obsidian
                    if (data.getMaterial() == Material.LAVA) {
                        limitedRegion.setBlockData(tx, ty, tz, OBSIDIAN);
                    }
                }
            }
        }
    }
}