package me.eccentric_nz.tardischunkgenerator.worldgen;

import me.eccentric_nz.tardischunkgenerator.worldgen.populators.IslandBlockPopulator;
import me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders.WaterBiomeProvider;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.List;
import java.util.Random;

public class WaterGenerator extends ChunkGenerator {

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 6);
        generator.setScale(0.008);
        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // normalised noise value between -1 and 1
                double noise = generator.noise(worldX + x, worldZ + z, 1, 1, true);
                // get a height between -40 and 40
                int height = (int) (noise * 40);
                // move height range up to between 24 and 104
                height += 64;
                if (height > chunkData.getMaxHeight()) {
                    height = chunkData.getMaxHeight();
                }
                // set blocks to stone with a top layer of sand
                for (int y = chunkData.getMinHeight(); y < height; y++) {
                    if (y <= height - 12) {
                        chunkData.setBlock(x, y, z, Material.STONE);
                    } else {
                        chunkData.setBlock(x, y, z, Material.SAND);
                    }
                }
            }
        }
    }

    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 23; y < 96; y++) {
                    if (chunkData.getType(x, y, z).isAir()) {
                        chunkData.setBlock(x, y, z, Material.WATER);
                    }
                }
            }
        }
    }

    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) {
        if (chunkData.getMinHeight() == worldInfo.getMinHeight()) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunkData.setBlock(x, chunkData.getMinHeight(), z, Material.BEDROCK);
                }
            }
        }
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return true;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }

    /**
     * Sets the entire world to the OCEAN biomes
     *
     * @param worldInfo the information about this world
     * @return the Water biome provider
     */
    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return new WaterBiomeProvider();
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> populators = super.getDefaultPopulators(world);
        populators.add(new IslandBlockPopulator());
        return populators;
    }
}
