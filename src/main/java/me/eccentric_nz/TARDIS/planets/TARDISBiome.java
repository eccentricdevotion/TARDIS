/*
 * Copyright (C) 2021 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.planets;

import com.google.common.base.Preconditions;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Holds all accepted Biomes in the default server
 */
public final class TardisBiome implements Keyed {

    public static final TardisBiome OCEAN = new TardisBiome("OCEAN");
    public static final TardisBiome PLAINS = new TardisBiome("PLAINS");
    public static final TardisBiome DESERT = new TardisBiome("DESERT");
    public static final TardisBiome MOUNTAINS = new TardisBiome("MOUNTAINS");
    public static final TardisBiome FOREST = new TardisBiome("FOREST");
    public static final TardisBiome TAIGA = new TardisBiome("TAIGA");
    public static final TardisBiome SWAMP = new TardisBiome("SWAMP");
    public static final TardisBiome RIVER = new TardisBiome("RIVER");
    public static final TardisBiome NETHER_WASTES = new TardisBiome("NETHER_WASTES");
    public static final TardisBiome THE_END = new TardisBiome("THE_END");
    public static final TardisBiome FROZEN_OCEAN = new TardisBiome("FROZEN_OCEAN");
    public static final TardisBiome FROZEN_RIVER = new TardisBiome("FROZEN_RIVER");
    public static final TardisBiome SNOWY_TUNDRA = new TardisBiome("SNOWY_TUNDRA");
    public static final TardisBiome SNOWY_MOUNTAINS = new TardisBiome("SNOWY_MOUNTAINS");
    public static final TardisBiome MUSHROOM_FIELDS = new TardisBiome("MUSHROOM_FIELDS");
    public static final TardisBiome MUSHROOM_FIELD_SHORE = new TardisBiome("MUSHROOM_FIELD_SHORE");
    public static final TardisBiome BEACH = new TardisBiome("BEACH");
    public static final TardisBiome DESERT_HILLS = new TardisBiome("DESERT_HILLS");
    public static final TardisBiome WOODED_HILLS = new TardisBiome("WOODED_HILLS");
    public static final TardisBiome TAIGA_HILLS = new TardisBiome("TAIGA_HILLS");
    public static final TardisBiome MOUNTAIN_EDGE = new TardisBiome("MOUNTAIN_EDGE");
    public static final TardisBiome JUNGLE = new TardisBiome("JUNGLE");
    public static final TardisBiome JUNGLE_HILLS = new TardisBiome("JUNGLE_HILLS");
    public static final TardisBiome JUNGLE_EDGE = new TardisBiome("JUNGLE_EDGE");
    public static final TardisBiome DEEP_OCEAN = new TardisBiome("DEEP_OCEAN");
    public static final TardisBiome STONE_SHORE = new TardisBiome("STONE_SHORE");
    public static final TardisBiome SNOWY_BEACH = new TardisBiome("SNOWY_BEACH");
    public static final TardisBiome BIRCH_FOREST = new TardisBiome("BIRCH_FOREST");
    public static final TardisBiome BIRCH_FOREST_HILLS = new TardisBiome("BIRCH_FOREST_HILLS");
    public static final TardisBiome DARK_FOREST = new TardisBiome("DARK_FOREST");
    public static final TardisBiome SNOWY_TAIGA = new TardisBiome("SNOWY_TAIGA");
    public static final TardisBiome SNOWY_TAIGA_HILLS = new TardisBiome("SNOWY_TAIGA_HILLS");
    public static final TardisBiome GIANT_TREE_TAIGA = new TardisBiome("GIANT_TREE_TAIGA");
    public static final TardisBiome GIANT_TREE_TAIGA_HILLS = new TardisBiome("GIANT_TREE_TAIGA_HILLS");
    public static final TardisBiome WOODED_MOUNTAINS = new TardisBiome("WOODED_MOUNTAINS");
    public static final TardisBiome SAVANNA = new TardisBiome("SAVANNA");
    public static final TardisBiome SAVANNA_PLATEAU = new TardisBiome("SAVANNA_PLATEAU");
    public static final TardisBiome BADLANDS = new TardisBiome("BADLANDS");
    public static final TardisBiome WOODED_BADLANDS_PLATEAU = new TardisBiome("WOODED_BADLANDS_PLATEAU");
    public static final TardisBiome BADLANDS_PLATEAU = new TardisBiome("BADLANDS_PLATEAU");
    public static final TardisBiome SMALL_END_ISLANDS = new TardisBiome("SMALL_END_ISLANDS");
    public static final TardisBiome END_MIDLANDS = new TardisBiome("END_MIDLANDS");
    public static final TardisBiome END_HIGHLANDS = new TardisBiome("END_HIGHLANDS");
    public static final TardisBiome END_BARRENS = new TardisBiome("END_BARRENS");
    public static final TardisBiome WARM_OCEAN = new TardisBiome("WARM_OCEAN");
    public static final TardisBiome LUKEWARM_OCEAN = new TardisBiome("LUKEWARM_OCEAN");
    public static final TardisBiome COLD_OCEAN = new TardisBiome("COLD_OCEAN");
    public static final TardisBiome DEEP_WARM_OCEAN = new TardisBiome("DEEP_WARM_OCEAN");
    public static final TardisBiome DEEP_LUKEWARM_OCEAN = new TardisBiome("DEEP_LUKEWARM_OCEAN");
    public static final TardisBiome DEEP_COLD_OCEAN = new TardisBiome("DEEP_COLD_OCEAN");
    public static final TardisBiome DEEP_FROZEN_OCEAN = new TardisBiome("DEEP_FROZEN_OCEAN");
    public static final TardisBiome THE_VOID = new TardisBiome("THE_VOID");
    public static final TardisBiome SUNFLOWER_PLAINS = new TardisBiome("SUNFLOWER_PLAINS");
    public static final TardisBiome DESERT_LAKES = new TardisBiome("DESERT_LAKES");
    public static final TardisBiome GRAVELLY_MOUNTAINS = new TardisBiome("GRAVELLY_MOUNTAINS");
    public static final TardisBiome FLOWER_FOREST = new TardisBiome("FLOWER_FOREST");
    public static final TardisBiome TAIGA_MOUNTAINS = new TardisBiome("TAIGA_MOUNTAINS");
    public static final TardisBiome SWAMP_HILLS = new TardisBiome("SWAMP_HILLS");
    public static final TardisBiome ICE_SPIKES = new TardisBiome("ICE_SPIKES");
    public static final TardisBiome MODIFIED_JUNGLE = new TardisBiome("MODIFIED_JUNGLE");
    public static final TardisBiome MODIFIED_JUNGLE_EDGE = new TardisBiome("MODIFIED_JUNGLE_EDGE");
    public static final TardisBiome TALL_BIRCH_FOREST = new TardisBiome("TALL_BIRCH_FOREST");
    public static final TardisBiome TALL_BIRCH_HILLS = new TardisBiome("TALL_BIRCH_HILLS");
    public static final TardisBiome DARK_FOREST_HILLS = new TardisBiome("DARK_FOREST_HILLS");
    public static final TardisBiome SNOWY_TAIGA_MOUNTAINS = new TardisBiome("SNOWY_TAIGA_MOUNTAINS");
    public static final TardisBiome GIANT_SPRUCE_TAIGA = new TardisBiome("GIANT_SPRUCE_TAIGA");
    public static final TardisBiome GIANT_SPRUCE_TAIGA_HILLS = new TardisBiome("GIANT_SPRUCE_TAIGA_HILLS");
    public static final TardisBiome MODIFIED_GRAVELLY_MOUNTAINS = new TardisBiome("MODIFIED_GRAVELLY_MOUNTAINS");
    public static final TardisBiome SHATTERED_SAVANNA = new TardisBiome("SHATTERED_SAVANNA");
    public static final TardisBiome SHATTERED_SAVANNA_PLATEAU = new TardisBiome("SHATTERED_SAVANNA_PLATEAU");
    public static final TardisBiome ERODED_BADLANDS = new TardisBiome("ERODED_BADLANDS");
    public static final TardisBiome MODIFIED_WOODED_BADLANDS_PLATEAU = new TardisBiome("MODIFIED_WOODED_BADLANDS_PLATEAU");
    public static final TardisBiome MODIFIED_BADLANDS_PLATEAU = new TardisBiome("MODIFIED_BADLANDS_PLATEAU");
    public static final TardisBiome BAMBOO_JUNGLE = new TardisBiome("BAMBOO_JUNGLE");
    public static final TardisBiome BAMBOO_JUNGLE_HILLS = new TardisBiome("BAMBOO_JUNGLE_HILLS");
    public static final TardisBiome SOUL_SAND_VALLEY = new TardisBiome("SOUL_SAND_VALLEY");
    public static final TardisBiome CRIMSON_FOREST = new TardisBiome("CRIMSON_FOREST");
    public static final TardisBiome WARPED_FOREST = new TardisBiome("WARPED_FOREST");
    public static final TardisBiome BASALT_DELTAS = new TardisBiome("BASALT_DELTAS");
    private static final List<TardisBiome> BIOMES_LEGACY = new ArrayList<>();
    private static final Map<String, TardisBiome> BY_ENUM_NAME_LEGACY = new HashMap<>();
    private static final Map<NamespacedKey, TardisBiome> BY_KEY = new HashMap<>();
    private final NamespacedKey key;

    /**
     * The legacy constructor used to fill the default biomes
     *
     * @param name the name of the biome
     */
    private TardisBiome(String name) {
        this(NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT)));
        BY_ENUM_NAME_LEGACY.put(name, this);
        BIOMES_LEGACY.add(this);
    }

    /**
     * The constructor
     *
     * @param namespacedKey the biome's name spaced key
     */
    private TardisBiome(NamespacedKey namespacedKey) {
        key = namespacedKey;
        BY_KEY.put(namespacedKey, this);
    }

    public static TardisBiome of(NamespacedKey namespacedKey) {
        return BY_KEY.putIfAbsent(namespacedKey, new TardisBiome(namespacedKey));
    }

    public static Iterator<TardisBiome> iterator() {
        return BY_KEY.values().iterator();
    }

    public static TardisBiome get(NamespacedKey namespacedKey) {
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
    public static TardisBiome valueOf(String name) {
        Preconditions.checkNotNull(name, "Name is null");
        TardisBiome defined = BY_ENUM_NAME_LEGACY.get(name);
        Preconditions.checkArgument(defined != null, "No enum constant " + TardisBiome.class.getName() + "." + name);
        return defined;
    }

    /**
     * Get an array of all default biomes.
     *
     * @return copied array of all default biomes
     * @deprecated holdover from when this class was an enum
     */
    @Deprecated
    public static TardisBiome[] values() {
        return BIOMES_LEGACY.toArray(new TardisBiome[0]);
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
        if (!(o instanceof TardisBiome biome)) {
            return false;
        }
        return key.equals(biome.key);
    }

    @Override
    public String toString() {
        return "TARDISBiome{" + "key=" + key + '}';
    }

    /**
     * @return biome name
     */
    public String name() {
        return key.getKey().toUpperCase(Locale.ROOT);
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
