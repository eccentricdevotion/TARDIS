package me.eccentric_nz.TARDIS.planets;

import com.google.common.base.Preconditions;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Holds all accepted Biomes in the default server
 */
public final class TARDISBiome implements Keyed {

    private static final List<TARDISBiome> BIOMES_LEGACY = new ArrayList<>();
    private static final Map<String, TARDISBiome> BY_ENUM_NAME_LEGACY = new HashMap<>();
    private static final Map<NamespacedKey, TARDISBiome> BY_KEY = new HashMap<>();
    private final NamespacedKey key;

    public static final TARDISBiome OCEAN = new TARDISBiome("OCEAN");
    public static final TARDISBiome PLAINS = new TARDISBiome("PLAINS");
    public static final TARDISBiome DESERT = new TARDISBiome("DESERT");
    public static final TARDISBiome MOUNTAINS = new TARDISBiome("MOUNTAINS");
    public static final TARDISBiome FOREST = new TARDISBiome("FOREST");
    public static final TARDISBiome TAIGA = new TARDISBiome("TAIGA");
    public static final TARDISBiome SWAMP = new TARDISBiome("SWAMP");
    public static final TARDISBiome RIVER = new TARDISBiome("RIVER");
    public static final TARDISBiome NETHER_WASTES = new TARDISBiome("NETHER_WASTES");
    public static final TARDISBiome THE_END = new TARDISBiome("THE_END");
    public static final TARDISBiome FROZEN_OCEAN = new TARDISBiome("FROZEN_OCEAN");
    public static final TARDISBiome FROZEN_RIVER = new TARDISBiome("FROZEN_RIVER");
    public static final TARDISBiome SNOWY_TUNDRA = new TARDISBiome("SNOWY_TUNDRA");
    public static final TARDISBiome SNOWY_MOUNTAINS = new TARDISBiome("SNOWY_MOUNTAINS");
    public static final TARDISBiome MUSHROOM_FIELDS = new TARDISBiome("MUSHROOM_FIELDS");
    public static final TARDISBiome MUSHROOM_FIELD_SHORE = new TARDISBiome("MUSHROOM_FIELD_SHORE");
    public static final TARDISBiome BEACH = new TARDISBiome("BEACH");
    public static final TARDISBiome DESERT_HILLS = new TARDISBiome("DESERT_HILLS");
    public static final TARDISBiome WOODED_HILLS = new TARDISBiome("WOODED_HILLS");
    public static final TARDISBiome TAIGA_HILLS = new TARDISBiome("TAIGA_HILLS");
    public static final TARDISBiome MOUNTAIN_EDGE = new TARDISBiome("MOUNTAIN_EDGE");
    public static final TARDISBiome JUNGLE = new TARDISBiome("JUNGLE");
    public static final TARDISBiome JUNGLE_HILLS = new TARDISBiome("JUNGLE_HILLS");
    public static final TARDISBiome JUNGLE_EDGE = new TARDISBiome("JUNGLE_EDGE");
    public static final TARDISBiome DEEP_OCEAN = new TARDISBiome("DEEP_OCEAN");
    public static final TARDISBiome STONE_SHORE = new TARDISBiome("STONE_SHORE");
    public static final TARDISBiome SNOWY_BEACH = new TARDISBiome("SNOWY_BEACH");
    public static final TARDISBiome BIRCH_FOREST = new TARDISBiome("BIRCH_FOREST");
    public static final TARDISBiome BIRCH_FOREST_HILLS = new TARDISBiome("BIRCH_FOREST_HILLS");
    public static final TARDISBiome DARK_FOREST = new TARDISBiome("DARK_FOREST");
    public static final TARDISBiome SNOWY_TAIGA = new TARDISBiome("SNOWY_TAIGA");
    public static final TARDISBiome SNOWY_TAIGA_HILLS = new TARDISBiome("SNOWY_TAIGA_HILLS");
    public static final TARDISBiome GIANT_TREE_TAIGA = new TARDISBiome("GIANT_TREE_TAIGA");
    public static final TARDISBiome GIANT_TREE_TAIGA_HILLS = new TARDISBiome("GIANT_TREE_TAIGA_HILLS");
    public static final TARDISBiome WOODED_MOUNTAINS = new TARDISBiome("WOODED_MOUNTAINS");
    public static final TARDISBiome SAVANNA = new TARDISBiome("SAVANNA");
    public static final TARDISBiome SAVANNA_PLATEAU = new TARDISBiome("SAVANNA_PLATEAU");
    public static final TARDISBiome BADLANDS = new TARDISBiome("BADLANDS");
    public static final TARDISBiome WOODED_BADLANDS_PLATEAU = new TARDISBiome("WOODED_BADLANDS_PLATEAU");
    public static final TARDISBiome BADLANDS_PLATEAU = new TARDISBiome("BADLANDS_PLATEAU");
    public static final TARDISBiome SMALL_END_ISLANDS = new TARDISBiome("SMALL_END_ISLANDS");
    public static final TARDISBiome END_MIDLANDS = new TARDISBiome("END_MIDLANDS");
    public static final TARDISBiome END_HIGHLANDS = new TARDISBiome("END_HIGHLANDS");
    public static final TARDISBiome END_BARRENS = new TARDISBiome("END_BARRENS");
    public static final TARDISBiome WARM_OCEAN = new TARDISBiome("WARM_OCEAN");
    public static final TARDISBiome LUKEWARM_OCEAN = new TARDISBiome("LUKEWARM_OCEAN");
    public static final TARDISBiome COLD_OCEAN = new TARDISBiome("COLD_OCEAN");
    public static final TARDISBiome DEEP_WARM_OCEAN = new TARDISBiome("DEEP_WARM_OCEAN");
    public static final TARDISBiome DEEP_LUKEWARM_OCEAN = new TARDISBiome("DEEP_LUKEWARM_OCEAN");
    public static final TARDISBiome DEEP_COLD_OCEAN = new TARDISBiome("DEEP_COLD_OCEAN");
    public static final TARDISBiome DEEP_FROZEN_OCEAN = new TARDISBiome("DEEP_FROZEN_OCEAN");
    public static final TARDISBiome THE_VOID = new TARDISBiome("THE_VOID");
    public static final TARDISBiome SUNFLOWER_PLAINS = new TARDISBiome("SUNFLOWER_PLAINS");
    public static final TARDISBiome DESERT_LAKES = new TARDISBiome("DESERT_LAKES");
    public static final TARDISBiome GRAVELLY_MOUNTAINS = new TARDISBiome("GRAVELLY_MOUNTAINS");
    public static final TARDISBiome FLOWER_FOREST = new TARDISBiome("FLOWER_FOREST");
    public static final TARDISBiome TAIGA_MOUNTAINS = new TARDISBiome("TAIGA_MOUNTAINS");
    public static final TARDISBiome SWAMP_HILLS = new TARDISBiome("SWAMP_HILLS");
    public static final TARDISBiome ICE_SPIKES = new TARDISBiome("ICE_SPIKES");
    public static final TARDISBiome MODIFIED_JUNGLE = new TARDISBiome("MODIFIED_JUNGLE");
    public static final TARDISBiome MODIFIED_JUNGLE_EDGE = new TARDISBiome("MODIFIED_JUNGLE_EDGE");
    public static final TARDISBiome TALL_BIRCH_FOREST = new TARDISBiome("TALL_BIRCH_FOREST");
    public static final TARDISBiome TALL_BIRCH_HILLS = new TARDISBiome("TALL_BIRCH_HILLS");
    public static final TARDISBiome DARK_FOREST_HILLS = new TARDISBiome("DARK_FOREST_HILLS");
    public static final TARDISBiome SNOWY_TAIGA_MOUNTAINS = new TARDISBiome("SNOWY_TAIGA_MOUNTAINS");
    public static final TARDISBiome GIANT_SPRUCE_TAIGA = new TARDISBiome("GIANT_SPRUCE_TAIGA");
    public static final TARDISBiome GIANT_SPRUCE_TAIGA_HILLS = new TARDISBiome("GIANT_SPRUCE_TAIGA_HILLS");
    public static final TARDISBiome MODIFIED_GRAVELLY_MOUNTAINS = new TARDISBiome("MODIFIED_GRAVELLY_MOUNTAINS");
    public static final TARDISBiome SHATTERED_SAVANNA = new TARDISBiome("SHATTERED_SAVANNA");
    public static final TARDISBiome SHATTERED_SAVANNA_PLATEAU = new TARDISBiome("SHATTERED_SAVANNA_PLATEAU");
    public static final TARDISBiome ERODED_BADLANDS = new TARDISBiome("ERODED_BADLANDS");
    public static final TARDISBiome MODIFIED_WOODED_BADLANDS_PLATEAU = new TARDISBiome("MODIFIED_WOODED_BADLANDS_PLATEAU");
    public static final TARDISBiome MODIFIED_BADLANDS_PLATEAU = new TARDISBiome("MODIFIED_BADLANDS_PLATEAU");
    public static final TARDISBiome BAMBOO_JUNGLE = new TARDISBiome("BAMBOO_JUNGLE");
    public static final TARDISBiome BAMBOO_JUNGLE_HILLS = new TARDISBiome("BAMBOO_JUNGLE_HILLS");
    public static final TARDISBiome SOUL_SAND_VALLEY = new TARDISBiome("SOUL_SAND_VALLEY");
    public static final TARDISBiome CRIMSON_FOREST = new TARDISBiome("CRIMSON_FOREST");
    public static final TARDISBiome WARPED_FOREST = new TARDISBiome("WARPED_FOREST");
    public static final TARDISBiome BASALT_DELTAS = new TARDISBiome("BASALT_DELTAS");

    /**
     * The legacy constructor used to fill the default biomes
     *
     * @param name the name of the biome
     */
    private TARDISBiome(String name) {
        this(NamespacedKey.minecraft(name.toLowerCase(Locale.ENGLISH)));
        BY_ENUM_NAME_LEGACY.put(name, this);
        BIOMES_LEGACY.add(this);
    }

    /**
     * The constructor
     *
     * @param namespacedKey
     */
    private TARDISBiome(NamespacedKey namespacedKey) {
        key = namespacedKey;
        BY_KEY.put(namespacedKey, this);
    }

    public static TARDISBiome of(NamespacedKey namespacedKey) {
        return BY_KEY.putIfAbsent(namespacedKey, new TARDISBiome(namespacedKey));
    }

    public static Iterator<TARDISBiome> iterator() {
        return BY_KEY.values().iterator();
    }

    public static TARDISBiome get(NamespacedKey namespacedKey) {
        return BY_KEY.get(namespacedKey);
    }

    /**
     * See {@link Enum#valueOf(java.lang.Class, java.lang.String)}.
     *
     * @param name biome name
     * @return TARDISBiome
     * @deprecated holdover from when this class was an enum
     */
    @Deprecated
    public static TARDISBiome valueOf(String name) {
        Preconditions.checkNotNull(name, "Name is null");
        TARDISBiome defined = BY_ENUM_NAME_LEGACY.get(name);
        Preconditions.checkArgument(defined != null, "No enum constant " + TARDISBiome.class.getName() + "." + name);
        return defined;
    }

    /**
     * Get an array of all default biomes.
     *
     * @return copied array of all default biomes
     * @deprecated holdover from when this class was an enum
     */
    @Deprecated
    public static TARDISBiome[] values() {
        return BIOMES_LEGACY.toArray(new TARDISBiome[0]);
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TARDISBiome)) {
            return false;
        }
        TARDISBiome biome = (TARDISBiome) o;
        return Objects.equals(key, biome.key);
    }

    @Override
    public String toString() {
        return "TARDISBiome{" + "key=" + key + '}';
    }

    /**
     * @return biome name
     */
    public String name() {
        return key.getKey().toUpperCase(Locale.ENGLISH);
    }

    /**
     * See {@link Enum#ordinal()}.
     *
     * @return ordinal
     * @deprecated holdover from when this class was an enum
     */
    @Deprecated
    public int ordinal() {
        return BIOMES_LEGACY.indexOf(this);
    }
}
