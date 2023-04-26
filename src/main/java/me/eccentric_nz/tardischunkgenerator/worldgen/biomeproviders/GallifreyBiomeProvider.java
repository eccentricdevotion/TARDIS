package me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.List;
import java.util.Random;

public class GallifreyBiomeProvider extends BiomeProvider {

    @Override
    public Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {

        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 6);
        generator.setScale(0.01);
        // get noise value between -1 and 1
        double noise = generator.noise(x, z, 1, 1, true);
        if (noise < 0.2) {
            return Biome.BADLANDS;
        } else if (noise < 0.6) {
            return Biome.ERODED_BADLANDS;
        } else {
            return Biome.WOODED_BADLANDS;
        }
    }

    @Override
    public List<Biome> getBiomes(WorldInfo worldInfo) {
        return List.of(Biome.BADLANDS, Biome.ERODED_BADLANDS, Biome.WOODED_BADLANDS);
    }
}
