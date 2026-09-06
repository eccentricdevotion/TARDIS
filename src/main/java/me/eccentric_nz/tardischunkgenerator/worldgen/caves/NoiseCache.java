package me.eccentric_nz.tardischunkgenerator.worldgen.caves;

import org.bukkit.util.noise.PerlinNoiseGenerator;

public class NoiseCache {

    /**
     * One reusable noise set per generation thread.
     * <p>
     * Paper requires generator callbacks to be thread-safe. PerlinNoiseGenerator
     * exposes mutable instance state internally, so sharing one NoiseSet between
     * generation threads would be unnecessarily risky. This cache gives each
     * thread its own set and reuses it for subsequent chunks from the same world.
     * <p>
     * When a thread starts generating a different world, its cached set is replaced.
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
    private static long worldSeed;
    private static NoiseSet noiseSet;

    public static NoiseSet get(long seed) {
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
                new PerlinNoiseGenerator(deriveNoiseSeed(worldSeed, NOISE_LAVA_EDGE)));
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
        NOISE_CACHE.get();
        return get(worldSeed);
    }
}