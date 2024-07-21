package me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders;

import java.util.List;
import java.util.Random;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class SiluriaBiomeProvider extends BiomeProvider {

    @Override
    public Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 6);
        generator.setScale(0.01);
        // get noise value between -1 and 1
        double noise = generator.noise(x, z, 1, 1, true);
        if (noise < 0) {
            return Biome.BAMBOO_JUNGLE;
        } else {
            return Biome.SPARSE_JUNGLE;
        }
    }

    @Override
    public List<Biome> getBiomes(WorldInfo worldInfo) {
        return List.of(Biome.BAMBOO_JUNGLE, Biome.SPARSE_JUNGLE);
    }
}
