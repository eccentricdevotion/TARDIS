/*
 * Copyright (C) 2019 eccentric_nz
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

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISOldBiomeLookup {

    public final static HashMap<String, Biome> OLD_BIOME_LOOKUP = new HashMap<>();

    static {
        OLD_BIOME_LOOKUP.put("BEACH", Biome.BEACH);
        OLD_BIOME_LOOKUP.put("BIRCH_FOREST_HILLS_MOUNTAINS", Biome.BIRCH_FOREST_HILLS);
        OLD_BIOME_LOOKUP.put("BIRCH_FOREST_MOUNTAINS", Biome.TALL_BIRCH_HILLS);
        OLD_BIOME_LOOKUP.put("COLD_TAIGA", Biome.SNOWY_TAIGA);
        OLD_BIOME_LOOKUP.put("COLD_TAIGA_HILLS", Biome.SNOWY_TAIGA_HILLS);
        OLD_BIOME_LOOKUP.put("COLD_TAIGA_MOUNTAINS", Biome.SNOWY_TAIGA_MOUNTAINS);
        OLD_BIOME_LOOKUP.put("DESERT_MOUNTAINS", Biome.DESERT_HILLS);
        OLD_BIOME_LOOKUP.put("EXTREME_HILLS_MOUNTAINS", Biome.MOUNTAINS);
        OLD_BIOME_LOOKUP.put("EXTREME_HILLS_PLUS", Biome.GRAVELLY_MOUNTAINS);
        OLD_BIOME_LOOKUP.put("EXTREME_HILLS_PLUS_MOUNTAINS", Biome.MODIFIED_GRAVELLY_MOUNTAINS);
        OLD_BIOME_LOOKUP.put("FLOWER_FOREST", Biome.FLOWER_FOREST);
        OLD_BIOME_LOOKUP.put("ICE_PLAINS", Biome.SNOWY_TUNDRA);
        OLD_BIOME_LOOKUP.put("ICE_PLAINS_SPIKES", Biome.ICE_SPIKES);
        OLD_BIOME_LOOKUP.put("JUNGLE_EDGE_MOUNTAINS", Biome.MODIFIED_JUNGLE_EDGE);
        OLD_BIOME_LOOKUP.put("JUNGLE_MOUNTAINS", Biome.JUNGLE_HILLS);
        OLD_BIOME_LOOKUP.put("MEGA_SPRUCE_TAIGA", Biome.GIANT_SPRUCE_TAIGA);
        OLD_BIOME_LOOKUP.put("MEGA_SPRUCE_TAIGA_HILLS", Biome.GIANT_SPRUCE_TAIGA_HILLS);
        OLD_BIOME_LOOKUP.put("MEGA_TAIGA", Biome.GIANT_TREE_TAIGA);
        OLD_BIOME_LOOKUP.put("MEGA_TAIGA_HILLS", Biome.GIANT_TREE_TAIGA_HILLS);
        OLD_BIOME_LOOKUP.put("MESA_BRYCE", Biome.ERODED_BADLANDS);
        OLD_BIOME_LOOKUP.put("MESA_PLATEAU", Biome.BADLANDS_PLATEAU);
        OLD_BIOME_LOOKUP.put("MESA_PLATEAU_FOREST", Biome.WOODED_BADLANDS_PLATEAU);
        OLD_BIOME_LOOKUP.put("MESA_PLATEAU_FOREST_MOUNTAINS", Biome.MODIFIED_WOODED_BADLANDS_PLATEAU);
        OLD_BIOME_LOOKUP.put("MESA_PLATEAU_MOUNTAINS", Biome.MODIFIED_BADLANDS_PLATEAU);
        OLD_BIOME_LOOKUP.put("MUSHROOM_SHORE", Biome.MUSHROOM_FIELD_SHORE);
        OLD_BIOME_LOOKUP.put("ROOFED_FOREST_MOUNTAINS", Biome.DARK_FOREST_HILLS);
        OLD_BIOME_LOOKUP.put("SAVANNA_MOUNTAINS", Biome.SHATTERED_SAVANNA);
        OLD_BIOME_LOOKUP.put("SAVANNA_PLATEAU", Biome.SAVANNA_PLATEAU);
        OLD_BIOME_LOOKUP.put("SAVANNA_PLATEAU_MOUNTAINS", Biome.SHATTERED_SAVANNA_PLATEAU);
        OLD_BIOME_LOOKUP.put("SMALL_MOUNTAINS", Biome.MOUNTAIN_EDGE);
        OLD_BIOME_LOOKUP.put("SUNFLOWER_PLAINS", Biome.SUNFLOWER_PLAINS);
        OLD_BIOME_LOOKUP.put("SWAMPLAND_MOUNTAINS", Biome.SWAMP_HILLS);
        OLD_BIOME_LOOKUP.put("TAIGA_MOUNTAINS", Biome.TAIGA_MOUNTAINS);
    }
}
