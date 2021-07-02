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
package me.eccentric_nz.TARDIS.enumeration;

import org.bukkit.block.Biome;

import java.util.Map;

/**
 * @author eccentric_nz
 */
public class TardisOldBiomeLookup {

    public final static Map<String, Biome> OLD_BIOME_LOOKUP;

    static {
        //noinspection RedundantTypeArguments (explicit type arguments speedup compilation and analysis time)
        OLD_BIOME_LOOKUP = Map.<String, Biome>ofEntries(
                Map.entry("BEACHES", Biome.BEACH),
                Map.entry("BIRCH_FOREST_HILLS_MOUNTAINS", Biome.BIRCH_FOREST_HILLS),
                Map.entry("BIRCH_FOREST_MOUNTAINS", Biome.TALL_BIRCH_HILLS),
                Map.entry("COLD_BEACH", Biome.SNOWY_BEACH),
                Map.entry("COLD_DEEP_OCEAN", Biome.DEEP_COLD_OCEAN),
                Map.entry("COLD_TAIGA_HILLS", Biome.SNOWY_TAIGA_HILLS),
                Map.entry("COLD_TAIGA_MOUNTAINS", Biome.SNOWY_TAIGA_MOUNTAINS),
                Map.entry("COLD_TAIGA", Biome.SNOWY_TAIGA),
                Map.entry("DESERT_MOUNTAINS", Biome.DESERT_HILLS),
                Map.entry("EXTREME_HILLS_MOUNTAINS", Biome.MOUNTAINS),
                Map.entry("EXTREME_HILLS_PLUS_MOUNTAINS", Biome.MODIFIED_GRAVELLY_MOUNTAINS),
                Map.entry("EXTREME_HILLS_PLUS", Biome.GRAVELLY_MOUNTAINS),
                Map.entry("EXTREME_HILLS_WITH_TREES", Biome.WOODED_MOUNTAINS),
                Map.entry("EXTREME_HILLS", Biome.MOUNTAINS),
                Map.entry("FLOWER_FOREST", Biome.FLOWER_FOREST),
                Map.entry("FOREST_HILLS", Biome.WOODED_HILLS),
                Map.entry("FROZEN_DEEP_OCEAN", Biome.DEEP_FROZEN_OCEAN),
                Map.entry("HELL", Biome.NETHER_WASTES),
                Map.entry("ICE_FLATS", Biome.SNOWY_TUNDRA),
                Map.entry("ICE_MOUNTAINS", Biome.SNOWY_MOUNTAINS),
                Map.entry("ICE_PLAINS_SPIKES", Biome.ICE_SPIKES),
                Map.entry("ICE_PLAINS", Biome.SNOWY_TUNDRA),
                Map.entry("JUNGLE_EDGE_MOUNTAINS", Biome.MODIFIED_JUNGLE_EDGE),
                Map.entry("JUNGLE_MOUNTAINS", Biome.JUNGLE_HILLS),
                Map.entry("LUKEWARM_DEEP_OCEAN", Biome.DEEP_LUKEWARM_OCEAN),
                Map.entry("MEGA_SPRUCE_TAIGA_HILLS", Biome.GIANT_SPRUCE_TAIGA_HILLS),
                Map.entry("MEGA_SPRUCE_TAIGA", Biome.GIANT_SPRUCE_TAIGA),
                Map.entry("MEGA_TAIGA_HILLS", Biome.GIANT_TREE_TAIGA_HILLS),
                Map.entry("MEGA_TAIGA", Biome.GIANT_TREE_TAIGA),
                Map.entry("MESA_BRYCE", Biome.ERODED_BADLANDS),
                Map.entry("MESA_CLEAR_ROCK", Biome.BADLANDS_PLATEAU),
                Map.entry("MESA_PLATEAU_FOREST_MOUNTAINS", Biome.MODIFIED_WOODED_BADLANDS_PLATEAU),
                Map.entry("MESA_PLATEAU_FOREST", Biome.WOODED_BADLANDS_PLATEAU),
                Map.entry("MESA_PLATEAU_MOUNTAINS", Biome.MODIFIED_BADLANDS_PLATEAU),
                Map.entry("MESA_PLATEAU", Biome.BADLANDS_PLATEAU),
                Map.entry("MESA_ROCK", Biome.WOODED_BADLANDS_PLATEAU),
                Map.entry("MESA", Biome.BADLANDS),
                Map.entry("MUSHROOM_ISLAND_SHORE", Biome.MUSHROOM_FIELD_SHORE),
                Map.entry("MUSHROOM_ISLAND", Biome.MUSHROOM_FIELDS),
                Map.entry("MUSHROOM_SHORE", Biome.MUSHROOM_FIELD_SHORE),
                Map.entry("MUTATED_BIRCH_FOREST_HILLS", Biome.TALL_BIRCH_HILLS),
                Map.entry("MUTATED_BIRCH_FOREST", Biome.TALL_BIRCH_FOREST),
                Map.entry("MUTATED_DESERT", Biome.DESERT_LAKES),
                Map.entry("MUTATED_EXTREME_HILLS_WITH_TREES", Biome.MODIFIED_GRAVELLY_MOUNTAINS),
                Map.entry("MUTATED_EXTREME_HILLS", Biome.GRAVELLY_MOUNTAINS),
                Map.entry("MUTATED_FOREST", Biome.FLOWER_FOREST),
                Map.entry("MUTATED_ICE_FLATS", Biome.ICE_SPIKES),
                Map.entry("MUTATED_JUNGLE_EDGE", Biome.MODIFIED_JUNGLE_EDGE),
                Map.entry("MUTATED_JUNGLE", Biome.MODIFIED_JUNGLE),
                Map.entry("MUTATED_MESA_CLEAR_ROCK", Biome.MODIFIED_BADLANDS_PLATEAU),
                Map.entry("MUTATED_MESA_ROCK", Biome.MODIFIED_WOODED_BADLANDS_PLATEAU),
                Map.entry("MUTATED_MESA", Biome.ERODED_BADLANDS),
                Map.entry("MUTATED_PLAINS", Biome.SUNFLOWER_PLAINS),
                Map.entry("MUTATED_REDWOOD_TAIGA_HILLS", Biome.GIANT_SPRUCE_TAIGA_HILLS),
                Map.entry("MUTATED_REDWOOD_TAIGA", Biome.GIANT_SPRUCE_TAIGA),
                Map.entry("MUTATED_ROOFED_FOREST", Biome.DARK_FOREST_HILLS),
                Map.entry("MUTATED_SAVANNA_ROCK", Biome.SHATTERED_SAVANNA_PLATEAU),
                Map.entry("MUTATED_SAVANNA", Biome.SHATTERED_SAVANNA),
                Map.entry("MUTATED_SWAMPLAND", Biome.SWAMP_HILLS),
                Map.entry("MUTATED_TAIGA_COLD", Biome.SNOWY_TAIGA_MOUNTAINS),
                Map.entry("MUTATED_TAIGA", Biome.TAIGA_MOUNTAINS),
                Map.entry("REDWOOD_TAIGA_HILLS", Biome.GIANT_TREE_TAIGA_HILLS),
                Map.entry("REDWOOD_TAIGA", Biome.GIANT_TREE_TAIGA),
                Map.entry("ROOFED_FOREST_MOUNTAINS", Biome.DARK_FOREST_HILLS),
                Map.entry("ROOFED_FOREST", Biome.DARK_FOREST),
                Map.entry("SAVANNA_MOUNTAINS", Biome.SHATTERED_SAVANNA),
                Map.entry("SAVANNA_PLATEAU_MOUNTAINS", Biome.SHATTERED_SAVANNA_PLATEAU),
                Map.entry("SAVANNA_PLATEAU", Biome.SAVANNA_PLATEAU),
                Map.entry("SAVANNA_ROCK", Biome.SAVANNA_PLATEAU),
                Map.entry("SKY_ISLAND_BARREN", Biome.END_BARRENS),
                Map.entry("SKY_ISLAND_HIGH", Biome.END_HIGHLANDS),
                Map.entry("SKY_ISLAND_LOW", Biome.SMALL_END_ISLANDS),
                Map.entry("SKY_ISLAND_MEDIUM", Biome.END_MIDLANDS),
                Map.entry("SKY", Biome.THE_END),
                Map.entry("SMALL_MOUNTAINS", Biome.MOUNTAIN_EDGE),
                Map.entry("SMALLER_EXTREME_HILLS", Biome.MOUNTAIN_EDGE),
                Map.entry("STONE_BEACH", Biome.STONE_SHORE),
                Map.entry("SUNFLOWER_PLAINS", Biome.SUNFLOWER_PLAINS),
                Map.entry("SWAMPLAND_MOUNTAINS", Biome.SWAMP_HILLS),
                Map.entry("SWAMPLAND", Biome.SWAMP),
                Map.entry("TAIGA_COLD_HILLS", Biome.SNOWY_TAIGA_HILLS),
                Map.entry("TAIGA_COLD", Biome.SNOWY_TAIGA),
                Map.entry("TAIGA_MOUNTAINS", Biome.TAIGA_MOUNTAINS),
                Map.entry("VOID", Biome.THE_VOID),
                Map.entry("WARM_DEEP_OCEAN", Biome.DEEP_WARM_OCEAN)
        );
    }
}
