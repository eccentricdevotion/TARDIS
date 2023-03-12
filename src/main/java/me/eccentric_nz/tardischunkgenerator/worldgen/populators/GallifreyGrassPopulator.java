package me.eccentric_nz.tardischunkgenerator.worldgen.populators;

import me.eccentric_nz.tardischunkgenerator.worldgen.feature.GrassPlacer;
import me.eccentric_nz.tardischunkgenerator.worldgen.feature.TARDISFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.DiskFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R2.CraftRegionAccessor;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

public class GallifreyGrassPopulator extends BlockPopulator {

    @Override
    public void populate(WorldInfo worldInfo, Random random, int x, int z, LimitedRegion limitedRegion) {
        if (isFeatureChunk(random, 30)) {
            int grassX = x * 16;
            int grassZ = z * 16;
            int grassY = 128;
            for (int i = 128; i > 60; i--) {
                if (limitedRegion.getType(grassX, grassY, grassZ).equals(Material.AIR)) {
                    grassY--;
                } else {
                    break;
                }
            }
            if (grassY > 60 && limitedRegion.isInRegion(grassX, grassY, grassZ) && !limitedRegion.getType(grassX, grassY, grassZ).equals(Material.WATER)) {
                DiskFeature base = new DiskFeature(DiskConfiguration.CODEC);
                WorldGenLevel level = ((CraftRegionAccessor) limitedRegion).getHandle();
                ChunkGenerator generator = level.getMinecraftWorld().getChunkSource().getGenerator();
                new GrassPlacer().place(TARDISFeatures.GRASS, level, new BlockPos(grassX, grassY, grassZ));
            }
        }
    }

    public boolean isFeatureChunk(Random random, int chance) {
        return random.nextInt(100) < chance;
    }
}
