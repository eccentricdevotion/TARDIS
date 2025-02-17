package me.eccentric_nz.TARDIS.commands.travel;

import org.terraform.biome.BiomeBank;

import java.util.HashSet;
import java.util.Set;

public class TerraBiomes {

    public Set<String> get() {
        Set<String> biomes = new HashSet<>();
        for (BiomeBank bb : BiomeBank.values()) {
            biomes.add(bb.toString());
        }
        return biomes;
    }
}
