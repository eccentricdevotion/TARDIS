package me.eccentric_nz.tardischunkgenerator.worldgen.caves;

import org.bukkit.util.noise.PerlinNoiseGenerator;

public record NoiseSet(
        PerlinNoiseGenerator caveNoise,
        PerlinNoiseGenerator decorationNoise,
        PerlinNoiseGenerator sculkHerdNoise,
        PerlinNoiseGenerator lavaLakeNoise,
        PerlinNoiseGenerator tunnelNoise,
        PerlinNoiseGenerator lavaShapeNoise,
        PerlinNoiseGenerator lavaCenterNoise,
        PerlinNoiseGenerator lavaDetailNoise,
        PerlinNoiseGenerator lavaEdgeNoise,
        PerlinNoiseGenerator regionNoise,
        PerlinNoiseGenerator chamberNoise,
        PerlinNoiseGenerator rootNoise,
        PerlinNoiseGenerator floodNoise) {
}
