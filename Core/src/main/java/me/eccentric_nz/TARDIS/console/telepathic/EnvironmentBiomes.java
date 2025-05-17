/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class EnvironmentBiomes {

    public static final List<Biome> END = List.of(Biome.THE_END, Biome.END_BARRENS, Biome.END_MIDLANDS, Biome.END_HIGHLANDS, Biome.SMALL_END_ISLANDS);
    public static final List<Biome> NETHER = List.of(Biome.NETHER_WASTES, Biome.BASALT_DELTAS, Biome.CRIMSON_FOREST, Biome.SOUL_SAND_VALLEY, Biome.WARPED_FOREST);
    public static final List<Biome> OVERWORLD = new ArrayList<>();
    public static final HashMap<String, Material> BIOME_BLOCKS = new HashMap<>() {{
        put("eye_of_harmony", Material.MAGMA_BLOCK);
        put("gallifrey_badlands", Material.TERRACOTTA);
        put("skaro_desert", Material.CYAN_TERRACOTTA);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.BADLANDS), Material.RED_SAND);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.BAMBOO_JUNGLE), Material.BAMBOO_BLOCK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.BASALT_DELTAS), Material.BASALT);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.BEACH), Material.SAND);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.BIRCH_FOREST), Material.BIRCH_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.CHERRY_GROVE), Material.CHERRY_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.COLD_OCEAN), Material.SALMON);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.CRIMSON_FOREST), Material.CRIMSON_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.DARK_FOREST), Material.DARK_OAK_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.DEEP_COLD_OCEAN), Material.SEA_PICKLE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.DEEP_DARK), Material.SCULK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.DEEP_FROZEN_OCEAN), Material.SEAGRASS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.DEEP_LUKEWARM_OCEAN), Material.TUBE_CORAL_BLOCK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.DEEP_OCEAN), Material.PRISMARINE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.DESERT), Material.SANDSTONE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.DRIPSTONE_CAVES), Material.DRIPSTONE_BLOCK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.END_BARRENS), Material.END_STONE_BRICKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.END_HIGHLANDS), Material.CHORUS_PLANT);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.END_MIDLANDS), Material.PURPUR_BLOCK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.ERODED_BADLANDS), Material.YELLOW_TERRACOTTA);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.FLOWER_FOREST), Material.ORANGE_TULIP);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.FOREST), Material.OAK_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.FROZEN_OCEAN), Material.BLUE_ICE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.FROZEN_PEAKS), Material.POWDER_SNOW_BUCKET);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.FROZEN_RIVER), Material.ICE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.GROVE), Material.STRIPPED_SPRUCE_LOG);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.ICE_SPIKES), Material.PACKED_ICE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.JAGGED_PEAKS), Material.GOAT_HORN);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.JUNGLE), Material.JUNGLE_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.LUKEWARM_OCEAN), Material.FIRE_CORAL_BLOCK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.LUSH_CAVES), Material.MOSS_BLOCK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.MANGROVE_SWAMP), Material.MANGROVE_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.MEADOW), Material.RED_TULIP);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.MUSHROOM_FIELDS), Material.MYCELIUM);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.NETHER_WASTES), Material.NETHERRACK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.OCEAN), Material.KELP);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.OLD_GROWTH_BIRCH_FOREST), Material.STRIPPED_BIRCH_WOOD);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.OLD_GROWTH_PINE_TAIGA), Material.SPRUCE_LOG);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.OLD_GROWTH_SPRUCE_TAIGA), Material.STRIPPED_SPRUCE_WOOD);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.PALE_GARDEN), Material.PALE_OAK_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.PLAINS), Material.GRASS_BLOCK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.RIVER), Material.FISHING_ROD);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SAVANNA), Material.ACACIA_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SAVANNA_PLATEAU), Material.ACACIA_LEAVES);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SMALL_END_ISLANDS), Material.END_STONE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SNOWY_BEACH), Material.SMOOTH_SANDSTONE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SNOWY_PLAINS), Material.SNOW_BLOCK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SNOWY_SLOPES), Material.SNOWBALL);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SNOWY_TAIGA), Material.SPRUCE_LEAVES);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SOUL_SAND_VALLEY), Material.SOUL_SAND);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SPARSE_JUNGLE), Material.COCOA_BEANS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.STONY_PEAKS), Material.CALCITE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.STONY_SHORE), Material.STONE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SUNFLOWER_PLAINS), Material.SUNFLOWER);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.SWAMP), Material.LILY_PAD);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.TAIGA), Material.SPRUCE_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.THE_END), Material.OBSIDIAN);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.THE_VOID), Material.BARRIER);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.WARM_OCEAN), Material.BRAIN_CORAL_BLOCK);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.WARPED_FOREST), Material.WARPED_PLANKS);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.WINDSWEPT_FOREST), Material.OAK_LEAVES);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.WINDSWEPT_GRAVELLY_HILLS), Material.GRAVEL);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.WINDSWEPT_HILLS), Material.EMERALD_ORE);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.WINDSWEPT_SAVANNA), Material.ACACIA_LOG);
        put(TARDIS.plugin.getFromRegistry().getKeysKey(Biome.WOODED_BADLANDS), Material.COARSE_DIRT);
    }};

    static {
        for (Biome biome : Registry.BIOME) {
            if (!END.contains(biome) && !NETHER.contains(biome) && biome != Biome.THE_VOID) {
                OVERWORLD.add(biome);
            }
        }
        OVERWORLD.sort(Comparator.comparing(b -> TARDIS.plugin.getFromRegistry().getKeysKey(b)));
    }
}
