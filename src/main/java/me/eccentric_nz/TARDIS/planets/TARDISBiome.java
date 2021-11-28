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

    public static final TARDISBiome OCEAN = new TARDISBiome("OCEAN");
    public static final TARDISBiome PLAINS = new TARDISBiome("PLAINS");
    public static final TARDISBiome DESERT = new TARDISBiome("DESERT");
    public static final TARDISBiome WINDSWEPT_HILLS = new TARDISBiome("WINDSWEPT_HILLS");
    public static final TARDISBiome FOREST = new TARDISBiome("FOREST");
    public static final TARDISBiome TAIGA = new TARDISBiome("TAIGA");
    public static final TARDISBiome SWAMP = new TARDISBiome("SWAMP");
    public static final TARDISBiome RIVER = new TARDISBiome("RIVER");
    public static final TARDISBiome NETHER_WASTES = new TARDISBiome("NETHER_WASTES");
    public static final TARDISBiome THE_END = new TARDISBiome("THE_END");
    public static final TARDISBiome FROZEN_OCEAN = new TARDISBiome("FROZEN_OCEAN");
    public static final TARDISBiome FROZEN_RIVER = new TARDISBiome("FROZEN_RIVER");
    public static final TARDISBiome SNOWY_PLAINS = new TARDISBiome("SNOWY_PLAINS");
    public static final TARDISBiome MUSHROOM_FIELDS = new TARDISBiome("MUSHROOM_FIELDS");
    public static final TARDISBiome BEACH = new TARDISBiome("BEACH");
    public static final TARDISBiome JUNGLE = new TARDISBiome("JUNGLE");
    public static final TARDISBiome SPARSE_JUNGLE = new TARDISBiome("SPARSE_JUNGLE");
    public static final TARDISBiome DEEP_OCEAN = new TARDISBiome("DEEP_OCEAN");
    public static final TARDISBiome STONY_SHORE = new TARDISBiome("STONY_SHORE");
    public static final TARDISBiome SNOWY_BEACH = new TARDISBiome("SNOWY_BEACH");
    public static final TARDISBiome BIRCH_FOREST = new TARDISBiome("BIRCH_FOREST");
    public static final TARDISBiome DARK_FOREST = new TARDISBiome("DARK_FOREST");
    public static final TARDISBiome SNOWY_TAIGA = new TARDISBiome("SNOWY_TAIGA");
    public static final TARDISBiome OLD_GROWTH_PINE_TAIGA = new TARDISBiome("OLD_GROWTH_PINE_TAIGA");
    public static final TARDISBiome WINDSWEPT_FOREST = new TARDISBiome("WINDSWEPT_FOREST");
    public static final TARDISBiome SAVANNA = new TARDISBiome("SAVANNA");
    public static final TARDISBiome SAVANNA_PLATEAU = new TARDISBiome("SAVANNA_PLATEAU");
    public static final TARDISBiome BADLANDS = new TARDISBiome("BADLANDS");
    public static final TARDISBiome WOODED_BADLANDS = new TARDISBiome("WOODED_BADLANDS");
    public static final TARDISBiome SMALL_END_ISLANDS = new TARDISBiome("SMALL_END_ISLANDS");
    public static final TARDISBiome END_MIDLANDS = new TARDISBiome("END_MIDLANDS");
    public static final TARDISBiome END_HIGHLANDS = new TARDISBiome("END_HIGHLANDS");
    public static final TARDISBiome END_BARRENS = new TARDISBiome("END_BARRENS");
    public static final TARDISBiome WARM_OCEAN = new TARDISBiome("WARM_OCEAN");
    public static final TARDISBiome LUKEWARM_OCEAN = new TARDISBiome("LUKEWARM_OCEAN");
    public static final TARDISBiome COLD_OCEAN = new TARDISBiome("COLD_OCEAN");
    public static final TARDISBiome DEEP_LUKEWARM_OCEAN = new TARDISBiome("DEEP_LUKEWARM_OCEAN");
    public static final TARDISBiome DEEP_COLD_OCEAN = new TARDISBiome("DEEP_COLD_OCEAN");
    public static final TARDISBiome DEEP_FROZEN_OCEAN = new TARDISBiome("DEEP_FROZEN_OCEAN");
    public static final TARDISBiome THE_VOID = new TARDISBiome("THE_VOID");
    public static final TARDISBiome SUNFLOWER_PLAINS = new TARDISBiome("SUNFLOWER_PLAINS");
    public static final TARDISBiome WINDSWEPT_GRAVELLY_HILLS = new TARDISBiome("WINDSWEPT_GRAVELLY_HILLS");
    public static final TARDISBiome FLOWER_FOREST = new TARDISBiome("FLOWER_FOREST");
    public static final TARDISBiome ICE_SPIKES = new TARDISBiome("ICE_SPIKES");
    public static final TARDISBiome OLD_GROWTH_BIRCH_FOREST = new TARDISBiome("OLD_GROWTH_BIRCH_FOREST");
    public static final TARDISBiome OLD_GROWTH_SPRUCE_TAIGA = new TARDISBiome("OLD_GROWTH_SPRUCE_TAIGA");
    public static final TARDISBiome WINDSWEPT_SAVANNA = new TARDISBiome("WINDSWEPT_SAVANNA");
    public static final TARDISBiome ERODED_BADLANDS = new TARDISBiome("ERODED_BADLANDS");
    public static final TARDISBiome BAMBOO_JUNGLE = new TARDISBiome("BAMBOO_JUNGLE");
    public static final TARDISBiome SOUL_SAND_VALLEY = new TARDISBiome("SOUL_SAND_VALLEY");
    public static final TARDISBiome CRIMSON_FOREST = new TARDISBiome("CRIMSON_FOREST");
    public static final TARDISBiome WARPED_FOREST = new TARDISBiome("WARPED_FOREST");
    public static final TARDISBiome BASALT_DELTAS = new TARDISBiome("BASALT_DELTAS");
    public static final TARDISBiome DRIPSTONE_CAVES = new TARDISBiome("DRIPSTONE_CAVES");
    public static final TARDISBiome LUSH_CAVES = new TARDISBiome("LUSH_CAVES");
    public static final TARDISBiome MEADOW = new TARDISBiome("MEADOW");
    public static final TARDISBiome GROVE = new TARDISBiome("GROVE");
    public static final TARDISBiome SNOWY_SLOPES = new TARDISBiome("SNOWY_SLOPES");
    public static final TARDISBiome FROZEN_PEAKS = new TARDISBiome("FROZEN_PEAKS");
    public static final TARDISBiome JAGGED_PEAKS = new TARDISBiome("JAGGED_PEAKS");
    public static final TARDISBiome STONY_PEAKS = new TARDISBiome("STONY_PEAKS");
    
    private static final List<TARDISBiome> BIOMES_LEGACY = new ArrayList<>();
    private static final Map<String, TARDISBiome> BY_ENUM_NAME_LEGACY = new HashMap<>();
    private static final Map<NamespacedKey, TARDISBiome> BY_KEY = new HashMap<>();
    private final NamespacedKey key;

    /**
     * The legacy constructor used to fill the default biomes
     *
     * @param name the name of the biome
     */
    private TARDISBiome(String name) {
        this(NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT)));
        BY_ENUM_NAME_LEGACY.put(name, this);
        BIOMES_LEGACY.add(this);
    }

    /**
     * The constructor
     *
     * @param namespacedKey the biome's name spaced key
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
        if (!(o instanceof TARDISBiome biome)) {
            return false;
        }
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
