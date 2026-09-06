package me.eccentric_nz.tardischunkgenerator.worldgen.caves;

import org.bukkit.util.noise.PerlinNoiseGenerator;

public class NoiseCache {

    /**
     * One reusable noise set per generation thread.
     * Paper generation callbacks may run concurrently, so each thread gets
     * its own mutable PerlinNoiseGenerator instances.
     */
    public static final ThreadLocal<NoiseCache> NOISE_CACHE = ThreadLocal.withInitial(NoiseCache::new);

    static final int NOISE_CAVE = 1;
    static final int NOISE_DECORATION = 2;
    static final int NOISE_SCULK_HERD = 3;
    static final int NOISE_LAVA_LAKE = 4;
    static final int NOISE_TUNNEL = 5;
    static final int NOISE_LAVA_SHAPE = 10;
    static final int NOISE_LAVA_CENTER = 11;
    static final int NOISE_LAVA_DETAIL = 12;
    static final int NOISE_LAVA_EDGE = 13;

    // large-scale regional layers
    static final int NOISE_REGION = 20;
    static final int NOISE_CHAMBER = 21;
    static final int NOISE_ROOT = 22;
    static final int NOISE_FLOOD = 23;

    // instance fields- a NoiseCache instance belongs to one ThreadLocal generation thread
    private long worldSeed;
    private NoiseSet noiseSet;

    private NoiseSet get(long seed) {
        if (noiseSet == null || worldSeed != seed) {
            worldSeed = seed;
            noiseSet = createNoiseSet(seed);
        }
        return noiseSet;
    }

    public static NoiseSet createNoiseSet(long worldSeed) {
        return new NoiseSet(
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_CAVE)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_DECORATION)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_SCULK_HERD)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_LAVA_LAKE)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_TUNNEL)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_LAVA_SHAPE)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_LAVA_CENTER)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_LAVA_DETAIL)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_LAVA_EDGE)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_REGION)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_CHAMBER)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_ROOT)),
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_FLOOD)));
    }

    /**
     * Derives an independent, deterministic seed for a named noise layer.
     * The mixing constants are internal to the hash; callers only deal with
     * the readable layer identifiers above.
     */
    private static long deriveNoiseSeed(long worldSeed, int layer) {
        long seed = worldSeed + layer * 0x9E3779B97F4A7C15L;
        seed ^= seed >>> 30;
        seed *= 0xBF58476D1CE4E5B9L;
        seed ^= seed >>> 27;
        seed *= 0x94D049BB133111EBL;
        seed ^= seed >>> 31;
        return seed;
    }

    public static NoiseSet getNoiseSet(long worldSeed) {
        return NOISE_CACHE.get().get(worldSeed);
    }
}
