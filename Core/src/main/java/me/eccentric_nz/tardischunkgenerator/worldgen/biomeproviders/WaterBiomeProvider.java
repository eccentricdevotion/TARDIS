package me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders;

import java.util.List;
import java.util.Random;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class WaterBiomeProvider extends BiomeProvider {

    @Override
    public Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(worldInfo.getSeed()), 6);
        generator.setScale(0.01);
        // get noise value between -1 and 1
        double noise = generator.noise(x, z, 1, 1, true);
        if (noise < -0.2) {
            return Biome.OCEAN;
        } else if (noise < 0) {
            return Biome.DEEP_OCEAN;
        } else if (noise < 0.2) {
            return Biome.DEEP_LUKEWARM_OCEAN;
        } else if (noise < 0.4) {
            return Biome.LUKEWARM_OCEAN;
        } else if (noise < 0.7) {
            return Biome.WARM_OCEAN;
        } else if (noise < 0.9) {
            return Biome.OCEAN;
        } else if (noise < 1) {
            return Biome.COLD_OCEAN;
        }
        return Biome.OCEAN;
    }

    @Override
    public List<Biome> getBiomes(WorldInfo worldInfo) {
        return List.of(Biome.OCEAN, Biome.DEEP_OCEAN, Biome.WARM_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.LUKEWARM_OCEAN, Biome.COLD_OCEAN);
    }
}
