/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.enumeration;

import org.bukkit.block.Biome;

import java.util.Map;

/**
 * @author eccentric_nz
 */
public class TardisOldBiomeLookup {

    public final static Map<String, Biome> OLD_BIOME_LOOKUP;

    static {
        OLD_BIOME_LOOKUP = Map.ofEntries(
                Map.entry("BADLANDS_PLATEAU", Biome.BADLANDS),
                Map.entry("BEACHES", Biome.BEACH),
                Map.entry("BIRCH_FOREST_HILLS", Biome.BIRCH_FOREST),
                Map.entry("BIRCH_FOREST_HILLS_MOUNTAINS", Biome.BIRCH_FOREST),
                Map.entry("BIRCH_FOREST_MOUNTAINS", Biome.BIRCH_FOREST),
                Map.entry("COLD_BEACH", Biome.SNOWY_BEACH),
                Map.entry("COLD_DEEP_OCEAN", Biome.DEEP_COLD_OCEAN),
                Map.entry("COLD_TAIGA", Biome.SNOWY_TAIGA),
                Map.entry("COLD_TAIGA_HILLS", Biome.SNOWY_TAIGA),
                Map.entry("COLD_TAIGA_MOUNTAINS", Biome.SNOWY_SLOPES),
                Map.entry("DARK_FOREST_HILLS", Biome.DARK_FOREST),
                Map.entry("DESERT_HILLS", Biome.DESERT),
                Map.entry("DESERT_LAKES", Biome.DESERT),
                Map.entry("DESERT_MOUNTAINS", Biome.DESERT),
                Map.entry("EXTREME_HILLS", Biome.WINDSWEPT_HILLS),
                Map.entry("EXTREME_HILLS_MOUNTAINS", Biome.WINDSWEPT_HILLS),
                Map.entry("EXTREME_HILLS_PLUS", Biome.WINDSWEPT_GRAVELLY_HILLS),
                Map.entry("EXTREME_HILLS_PLUS_MOUNTAINS", Biome.WINDSWEPT_HILLS),
                Map.entry("EXTREME_HILLS_WITH_TREES", Biome.WINDSWEPT_FOREST),
                Map.entry("FLOWER_FOREST", Biome.FLOWER_FOREST),
                Map.entry("FOREST_HILLS", Biome.WINDSWEPT_FOREST),
                Map.entry("FROZEN_DEEP_OCEAN", Biome.DEEP_FROZEN_OCEAN),
                Map.entry("GIANT_SPRUCE_TAIGA", Biome.OLD_GROWTH_SPRUCE_TAIGA),
                Map.entry("GIANT_SPRUCE_TAIGA_HILLS", Biome.OLD_GROWTH_SPRUCE_TAIGA),
                Map.entry("GIANT_TREE_TAIGA", Biome.OLD_GROWTH_SPRUCE_TAIGA),
                Map.entry("GIANT_TREE_TAIGA_HILLS", Biome.OLD_GROWTH_SPRUCE_TAIGA),
                Map.entry("GRAVELLY_MOUNTAINS", Biome.WINDSWEPT_GRAVELLY_HILLS),
                Map.entry("HELL", Biome.NETHER_WASTES),
                Map.entry("ICE_FLATS", Biome.SNOWY_PLAINS),
                Map.entry("ICE_MOUNTAINS", Biome.FROZEN_PEAKS),
                Map.entry("ICE_PLAINS", Biome.SNOWY_PLAINS),
                Map.entry("ICE_PLAINS_SPIKES", Biome.ICE_SPIKES),
                Map.entry("JUNGLE_EDGE_MOUNTAINS", Biome.SPARSE_JUNGLE),
                Map.entry("JUNGLE_HILLS", Biome.JUNGLE),
                Map.entry("JUNGLE_MOUNTAINS", Biome.JUNGLE),
                Map.entry("LUKEWARM_DEEP_OCEAN", Biome.DEEP_LUKEWARM_OCEAN),
                Map.entry("MEGA_SPRUCE_TAIGA", Biome.OLD_GROWTH_SPRUCE_TAIGA),
                Map.entry("MEGA_SPRUCE_TAIGA_HILLS", Biome.OLD_GROWTH_SPRUCE_TAIGA),
                Map.entry("MEGA_TAIGA", Biome.OLD_GROWTH_PINE_TAIGA),
                Map.entry("MEGA_TAIGA_HILLS", Biome.OLD_GROWTH_PINE_TAIGA),
                Map.entry("MESA", Biome.BADLANDS),
                Map.entry("MESA_BRYCE", Biome.ERODED_BADLANDS),
                Map.entry("MESA_CLEAR_ROCK", Biome.BADLANDS),
                Map.entry("MESA_PLATEAU", Biome.BADLANDS),
                Map.entry("MESA_PLATEAU_FOREST", Biome.WOODED_BADLANDS),
                Map.entry("MESA_PLATEAU_FOREST_MOUNTAINS", Biome.WOODED_BADLANDS),
                Map.entry("MESA_PLATEAU_MOUNTAINS", Biome.BADLANDS),
                Map.entry("MESA_ROCK", Biome.WOODED_BADLANDS),
                Map.entry("MODIFIED_BADLANDS_PLATEAU", Biome.BADLANDS),
                Map.entry("MODIFIED_GRAVELLY_MOUNTAINS", Biome.WINDSWEPT_GRAVELLY_HILLS),
                Map.entry("MODIFIED_JUNGLE", Biome.JUNGLE),
                Map.entry("MODIFIED_JUNGLE_EDGE", Biome.SPARSE_JUNGLE),
                Map.entry("MODIFIED_WOODED_BADLANDS_PLATEAU", Biome.WOODED_BADLANDS),
                Map.entry("MOUNTAINS", Biome.WINDSWEPT_HILLS),
                Map.entry("MOUNTAIN_EDGE", Biome.WINDSWEPT_HILLS),
                Map.entry("MUSHROOM_FIELD_SHORE", Biome.MUSHROOM_FIELDS),
                Map.entry("MUSHROOM_ISLAND", Biome.MUSHROOM_FIELDS),
                Map.entry("MUSHROOM_ISLAND_SHORE", Biome.MUSHROOM_FIELDS),
                Map.entry("MUSHROOM_SHORE", Biome.MUSHROOM_FIELDS),
                Map.entry("MUTATED_BIRCH_FOREST", Biome.OLD_GROWTH_BIRCH_FOREST),
                Map.entry("MUTATED_BIRCH_FOREST_HILLS", Biome.OLD_GROWTH_BIRCH_FOREST),
                Map.entry("MUTATED_DESERT", Biome.DESERT),
                Map.entry("MUTATED_EXTREME_HILLS", Biome.WINDSWEPT_GRAVELLY_HILLS),
                Map.entry("MUTATED_EXTREME_HILLS_WITH_TREES", Biome.WINDSWEPT_GRAVELLY_HILLS),
                Map.entry("MUTATED_FOREST", Biome.FLOWER_FOREST),
                Map.entry("MUTATED_ICE_FLATS", Biome.ICE_SPIKES),
                Map.entry("MUTATED_JUNGLE", Biome.JUNGLE),
                Map.entry("MUTATED_JUNGLE_EDGE", Biome.SPARSE_JUNGLE),
                Map.entry("MUTATED_MESA", Biome.ERODED_BADLANDS),
                Map.entry("MUTATED_MESA_CLEAR_ROCK", Biome.BADLANDS),
                Map.entry("MUTATED_MESA_ROCK", Biome.WOODED_BADLANDS),
                Map.entry("MUTATED_PLAINS", Biome.SUNFLOWER_PLAINS),
                Map.entry("MUTATED_REDWOOD_TAIGA", Biome.OLD_GROWTH_SPRUCE_TAIGA),
                Map.entry("MUTATED_REDWOOD_TAIGA_HILLS", Biome.OLD_GROWTH_SPRUCE_TAIGA),
                Map.entry("MUTATED_ROOFED_FOREST", Biome.DARK_FOREST),
                Map.entry("MUTATED_SAVANNA", Biome.WINDSWEPT_SAVANNA),
                Map.entry("MUTATED_SAVANNA_ROCK", Biome.WINDSWEPT_SAVANNA),
                Map.entry("MUTATED_SWAMPLAND", Biome.SWAMP),
                Map.entry("MUTATED_TAIGA", Biome.TAIGA),
                Map.entry("MUTATED_TAIGA_COLD", Biome.SNOWY_TAIGA),
                Map.entry("REDWOOD_TAIGA", Biome.OLD_GROWTH_PINE_TAIGA),
                Map.entry("REDWOOD_TAIGA_HILLS", Biome.OLD_GROWTH_PINE_TAIGA),
                Map.entry("ROOFED_FOREST", Biome.DARK_FOREST),
                Map.entry("ROOFED_FOREST_MOUNTAINS", Biome.DARK_FOREST),
                Map.entry("SAVANNA_MOUNTAINS", Biome.WINDSWEPT_SAVANNA),
                Map.entry("SAVANNA_PLATEAU", Biome.SAVANNA_PLATEAU),
                Map.entry("SAVANNA_PLATEAU_MOUNTAINS", Biome.WINDSWEPT_SAVANNA),
                Map.entry("SAVANNA_ROCK", Biome.SAVANNA_PLATEAU),
                Map.entry("SHATTERED_SAVANNA", Biome.WINDSWEPT_SAVANNA),
                Map.entry("SHATTERED_SAVANNA_PLATEAU", Biome.WINDSWEPT_SAVANNA),
                Map.entry("SKY", Biome.THE_END),
                Map.entry("SKY_ISLAND_BARREN", Biome.END_BARRENS),
                Map.entry("SKY_ISLAND_HIGH", Biome.END_HIGHLANDS),
                Map.entry("SKY_ISLAND_LOW", Biome.SMALL_END_ISLANDS),
                Map.entry("SKY_ISLAND_MEDIUM", Biome.END_MIDLANDS),
                Map.entry("SMALLER_EXTREME_HILLS", Biome.WINDSWEPT_HILLS),
                Map.entry("SMALL_MOUNTAINS", Biome.WINDSWEPT_HILLS),
                Map.entry("SNOWY_MOUNTAINS", Biome.FROZEN_PEAKS),
                Map.entry("SNOWY_TAIGA_HILLS", Biome.SNOWY_TAIGA),
                Map.entry("SNOWY_TAIGA_MOUNTAINS", Biome.SNOWY_SLOPES),
                Map.entry("SNOWY_TUNDRA", Biome.SNOWY_PLAINS),
                Map.entry("STONE_BEACH", Biome.STONY_SHORE),
                Map.entry("STONE_SHORE", Biome.STONY_SHORE),
                Map.entry("SUNFLOWER_PLAINS", Biome.SUNFLOWER_PLAINS),
                Map.entry("SWAMPLAND", Biome.SWAMP),
                Map.entry("SWAMPLAND_MOUNTAINS", Biome.SWAMP),
                Map.entry("SWAMP_HILLS", Biome.SWAMP),
                Map.entry("TAIGA_COLD", Biome.SNOWY_TAIGA),
                Map.entry("TAIGA_COLD_HILLS", Biome.SNOWY_TAIGA),
                Map.entry("TAIGA_MOUNTAINS", Biome.TAIGA),
                Map.entry("TALL_BIRCH_FOREST", Biome.OLD_GROWTH_BIRCH_FOREST),
                Map.entry("TALL_BIRCH_HILLS", Biome.BIRCH_FOREST),
                Map.entry("THE_VOID", Biome.THE_VOID),
                Map.entry("VOID", Biome.THE_VOID),
                Map.entry("WOODED_BADLANDS_PLATEAU", Biome.WOODED_BADLANDS),
                Map.entry("WOODED_HILLS", Biome.WINDSWEPT_FOREST),
                Map.entry("WOODED_MOUNTAINS", Biome.WINDSWEPT_FOREST),
                Map.entry("WARM_DEEP_OCEAN", Biome.WARM_OCEAN)
        );
    }
}
