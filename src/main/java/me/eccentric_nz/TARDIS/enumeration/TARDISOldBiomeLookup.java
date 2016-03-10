/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.HashMap;
import org.bukkit.block.Biome;

/**
 *
 * @author eccentric_nz
 */
public class TARDISOldBiomeLookup {

    public final static HashMap<String, Biome> OLD_BIOME_LOOKUP = new HashMap<String, Biome>();

    static {
        OLD_BIOME_LOOKUP.put("BEACH", Biome.BEACHES);
        OLD_BIOME_LOOKUP.put("BIRCH_FOREST_HILLS_MOUNTAINS", Biome.MUTATED_BIRCH_FOREST);
        OLD_BIOME_LOOKUP.put("BIRCH_FOREST_MOUNTAINS", Biome.MUTATED_BIRCH_FOREST_HILLS);
        OLD_BIOME_LOOKUP.put("COLD_TAIGA", Biome.TAIGA_COLD);
        OLD_BIOME_LOOKUP.put("COLD_TAIGA_HILLS", Biome.TAIGA_COLD_HILLS);
        OLD_BIOME_LOOKUP.put("COLD_TAIGA_MOUNTAINS", Biome.MUTATED_TAIGA_COLD);
        OLD_BIOME_LOOKUP.put("DESERT_MOUNTAINS", Biome.MUTATED_DESERT);
        OLD_BIOME_LOOKUP.put("EXTREME_HILLS_MOUNTAINS", Biome.MUTATED_EXTREME_HILLS);
        OLD_BIOME_LOOKUP.put("EXTREME_HILLS_PLUS", Biome.EXTREME_HILLS_WITH_TREES);
        OLD_BIOME_LOOKUP.put("EXTREME_HILLS_PLUS_MOUNTAINS", Biome.MUTATED_EXTREME_HILLS_WITH_TREES);
        OLD_BIOME_LOOKUP.put("FLOWER_FOREST", Biome.MUTATED_FOREST);
        OLD_BIOME_LOOKUP.put("ICE_PLAINS", Biome.ICE_FLATS);
        OLD_BIOME_LOOKUP.put("ICE_PLAINS_SPIKES", Biome.MUTATED_ICE_FLATS);
        OLD_BIOME_LOOKUP.put("JUNGLE_EDGE_MOUNTAINS", Biome.MUTATED_JUNGLE_EDGE);
        OLD_BIOME_LOOKUP.put("JUNGLE_MOUNTAINS", Biome.MUTATED_JUNGLE);
        OLD_BIOME_LOOKUP.put("MEGA_SPRUCE_TAIGA", Biome.MUTATED_REDWOOD_TAIGA);
        OLD_BIOME_LOOKUP.put("MEGA_SPRUCE_TAIGA_HILLS", Biome.MUTATED_REDWOOD_TAIGA_HILLS);
        OLD_BIOME_LOOKUP.put("MEGA_TAIGA", Biome.REDWOOD_TAIGA);
        OLD_BIOME_LOOKUP.put("MEGA_TAIGA_HILLS", Biome.REDWOOD_TAIGA_HILLS);
        OLD_BIOME_LOOKUP.put("MESA_BRYCE", Biome.MUTATED_MESA);
        OLD_BIOME_LOOKUP.put("MESA_PLATEAU", Biome.MESA_ROCK);
        OLD_BIOME_LOOKUP.put("MESA_PLATEAU_FOREST", Biome.MESA_CLEAR_ROCK);
        OLD_BIOME_LOOKUP.put("MESA_PLATEAU_FOREST_MOUNTAINS", Biome.MUTATED_MESA_CLEAR_ROCK);
        OLD_BIOME_LOOKUP.put("MESA_PLATEAU_MOUNTAINS", Biome.MUTATED_MESA_ROCK);
        OLD_BIOME_LOOKUP.put("MUSHROOM_SHORE", Biome.MUSHROOM_ISLAND_SHORE);
        OLD_BIOME_LOOKUP.put("ROOFED_FOREST_MOUNTAINS", Biome.MUTATED_ROOFED_FOREST);
        OLD_BIOME_LOOKUP.put("SAVANNA_MOUNTAINS", Biome.MUTATED_SAVANNA);
        OLD_BIOME_LOOKUP.put("SAVANNA_PLATEAU", Biome.SAVANNA_ROCK);
        OLD_BIOME_LOOKUP.put("SAVANNA_PLATEAU_MOUNTAINS", Biome.MUTATED_SAVANNA_ROCK);
        OLD_BIOME_LOOKUP.put("SMALL_MOUNTAINS", Biome.SMALLER_EXTREME_HILLS);
        OLD_BIOME_LOOKUP.put("SUNFLOWER_PLAINS", Biome.MUTATED_PLAINS);
        OLD_BIOME_LOOKUP.put("SWAMPLAND_MOUNTAINS", Biome.MUTATED_SWAMPLAND);
        OLD_BIOME_LOOKUP.put("TAIGA_MOUNTAINS", Biome.MUTATED_TAIGA);
    }
}
