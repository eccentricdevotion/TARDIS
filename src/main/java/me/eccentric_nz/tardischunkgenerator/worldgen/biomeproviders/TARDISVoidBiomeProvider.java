package me.eccentric_nz.tardischunkgenerator.worldgen.biomeproviders;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

import java.util.List;

public class TARDISVoidBiomeProvider extends BiomeProvider {

    @Override
    public Biome getBiome(WorldInfo worldInfo, int i, int i1, int i2) {
        return Biome.THE_VOID;
    }

    @Override
    public List<Biome> getBiomes(WorldInfo worldInfo) {
        return List.of(Biome.THE_VOID);
    }
}
