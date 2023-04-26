package me.eccentric_nz.tardischunkgenerator.worldgen;

import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders.FlatBiomeProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

public class FlatGenerator extends ChunkGenerator {

    private final TARDIS plugin;
    private final Material bottom;
    private final Material rock;
    private final Material middle;
    private final Material surface;

    public FlatGenerator(TARDIS plugin) {
        this.plugin = plugin;
        this.bottom = Material.valueOf(plugin.getGeneratorConfig().getString("bottom"));
        this.rock = Material.valueOf(plugin.getGeneratorConfig().getString("rock"));
        this.middle = Material.valueOf(plugin.getGeneratorConfig().getString("middle"));
        this.surface = Material.valueOf(plugin.getGeneratorConfig().getString("surface"));
    }

    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int x, int z, ChunkData chunkData) {
        if (chunkData.getMinHeight() == worldInfo.getMinHeight()) {
            for (int bx = 0; bx < 16; bx++) {
                for (int bz = 0; bz < 16; bz++) {
                    for (int by = 1; by < 65; by++) {
                        if (by < 60) {
                            chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, rock);
                        } else if (by < 64) {
                            chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, middle);
                        } else {
                            chunkData.setBlock(bx, chunkData.getMinHeight() + by, bz, surface);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int x, int z, ChunkData chunkData) {
        if (chunkData.getMinHeight() == worldInfo.getMinHeight()) {
            for (int bx = 0; bx < 16; bx++) {
                for (int bz = 0; bz < 16; bz++) {
                    chunkData.setBlock(bx, chunkData.getMinHeight(), bz, bottom);
                }
            }
        }
    }

    /**
     * Sets the entire world to the PLAINS biome
     *
     * @param worldInfo the information about this world
     * @return a biome provider with just PLAINS
     */
    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return new FlatBiomeProvider();
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, -60, 0);
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return true;
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
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }
}
