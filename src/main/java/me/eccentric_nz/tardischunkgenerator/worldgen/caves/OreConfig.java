package me.eccentric_nz.tardischunkgenerator.worldgen.caves;

import org.bukkit.Material;

import java.util.Set;

public record OreConfig(Material stoneOre,
                        Material deepslateOre,
                        int minHeight,
                        int maxHeight,
                        int peakHeight,
                        OreDistribution oreDistribution,
                        int veinsPerChunk,
                        int veinSize,
                        Set<String> biomeFilter // contains biome key sub-strings or null for all
                ) {
}
