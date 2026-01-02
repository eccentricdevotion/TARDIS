/*
 * Copyright (C) 2026 eccentric_nz
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

import org.bukkit.Material;
import org.bukkit.block.Biome;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eccentric_nz
 */
public class BiomeLookup {

    public static final Map<Material, Biome> MATERIALS = new HashMap<>();

    static {
        MATERIALS.put(Material.ACACIA_LEAVES, Biome.WINDSWEPT_SAVANNA);
        MATERIALS.put(Material.ACACIA_LOG, Biome.SAVANNA);
        MATERIALS.put(Material.BAMBOO_BLOCK, Biome.BAMBOO_JUNGLE);
        MATERIALS.put(Material.BIRCH_LOG, Biome.BIRCH_FOREST);
        MATERIALS.put(Material.BLUE_CONCRETE, Biome.RIVER);
        MATERIALS.put(Material.BLUE_ICE, Biome.FROZEN_RIVER);
        MATERIALS.put(Material.CACTUS, Biome.DESERT);
        MATERIALS.put(Material.CHERRY_LOG, Biome.CHERRY_GROVE);
        MATERIALS.put(Material.COARSE_DIRT, Biome.WOODED_BADLANDS);
        MATERIALS.put(Material.COBBLESTONE, Biome.STONY_SHORE);
        MATERIALS.put(Material.DARK_OAK_LOG, Biome.DARK_FOREST);
        MATERIALS.put(Material.DEAD_BRAIN_CORAL_BLOCK, Biome.DEEP_COLD_OCEAN);
        MATERIALS.put(Material.DEAD_BUBBLE_CORAL_BLOCK, Biome.DEEP_FROZEN_OCEAN);
        MATERIALS.put(Material.DEAD_FIRE_CORAL_BLOCK, Biome.DEEP_LUKEWARM_OCEAN);
        MATERIALS.put(Material.DEAD_HORN_CORAL_BLOCK, Biome.DEEP_OCEAN);
        MATERIALS.put(Material.DIRT, Biome.WINDSWEPT_HILLS);
        MATERIALS.put(Material.SHORT_GRASS, Biome.PLAINS);
        MATERIALS.put(Material.GRAVEL, Biome.WINDSWEPT_GRAVELLY_HILLS);
        MATERIALS.put(Material.GRAY_GLAZED_TERRACOTTA, Biome.JAGGED_PEAKS);
        MATERIALS.put(Material.ICE, Biome.FROZEN_OCEAN);
        MATERIALS.put(Material.JUNGLE_LOG, Biome.JUNGLE);
        MATERIALS.put(Material.KELP, Biome.LUKEWARM_OCEAN);
        MATERIALS.put(Material.LILY_PAD, Biome.SWAMP);
        MATERIALS.put(Material.MANGROVE_LOG, Biome.MANGROVE_SWAMP);
        MATERIALS.put(Material.MYCELIUM, Biome.MUSHROOM_FIELDS);
        MATERIALS.put(Material.OAK_LOG, Biome.FOREST);
        MATERIALS.put(Material.OAK_SAPLING, Biome.WINDSWEPT_FOREST);
        MATERIALS.put(Material.ORANGE_TERRACOTTA, Biome.ERODED_BADLANDS);
        MATERIALS.put(Material.OXEYE_DAISY, Biome.SNOWY_BEACH);
        MATERIALS.put(Material.PACKED_ICE, Biome.ICE_SPIKES);
        MATERIALS.put(Material.POPPY, Biome.MEADOW);
        MATERIALS.put(Material.SALMON, Biome.COLD_OCEAN);
        MATERIALS.put(Material.SAND, Biome.BEACH);
        MATERIALS.put(Material.SEAGRASS, Biome.OCEAN);
        MATERIALS.put(Material.SEA_PICKLE, Biome.WARM_OCEAN);
        MATERIALS.put(Material.SNOWBALL, Biome.SNOWY_SLOPES);
        MATERIALS.put(Material.SNOW_BLOCK, Biome.SNOWY_TAIGA);
        MATERIALS.put(Material.SPRUCE_LEAVES, Biome.OLD_GROWTH_PINE_TAIGA);
        MATERIALS.put(Material.SPRUCE_LOG, Biome.TAIGA);
        MATERIALS.put(Material.SPRUCE_SAPLING, Biome.GROVE);
        MATERIALS.put(Material.STONE, Biome.STONY_PEAKS);
        MATERIALS.put(Material.STRIPPED_ACACIA_LOG, Biome.SAVANNA_PLATEAU);
        MATERIALS.put(Material.STRIPPED_BAMBOO_BLOCK, Biome.SPARSE_JUNGLE);
        MATERIALS.put(Material.STRIPPED_BIRCH_LOG, Biome.OLD_GROWTH_BIRCH_FOREST);
        MATERIALS.put(Material.STRIPPED_SPRUCE_LOG, Biome.OLD_GROWTH_SPRUCE_TAIGA);
        MATERIALS.put(Material.SUNFLOWER, Biome.SUNFLOWER_PLAINS);
        MATERIALS.put(Material.TERRACOTTA, Biome.BADLANDS);
        MATERIALS.put(Material.WHITE_GLAZED_TERRACOTTA, Biome.FROZEN_PEAKS);
        MATERIALS.put(Material.WHITE_TERRACOTTA, Biome.SNOWY_PLAINS);
        MATERIALS.put(Material.WHITE_TULIP, Biome.FLOWER_FOREST);
        // CAVES
//        MATERIALS.put(Material.DRIPSTONE_BLOCK, Biome.DRIPSTONE_CAVES);
//        MATERIALS.put(Material.MOSS_BLOCK, Biome.LUSH_CAVES);
//        MATERIALS.put(Material.SCULK, Biome.DEEP_DARK);
        // NETHER
//        MATERIALS.put(Material.BASALT, Biome.BASALT_DELTAS);
//        MATERIALS.put(Material.CRIMSON_HYPHAE, Biome.CRIMSON_FOREST);
//        MATERIALS.put(Material.NETHERRACK, Biome.NETHER_WASTES);
//        MATERIALS.put(Material.SOUL_SAND, Biome.SOUL_SAND_VALLEY);
//        MATERIALS.put(Material.WARPED_HYPHAE, Biome.WARPED_FOREST);
        // THE_END
//        MATERIALS.put(Material.CHORUS_FLOWER, Biome.END_HIGHLANDS);
//        MATERIALS.put(Material.CHORUS_FRUIT, Biome.END_BARRENS);
//        MATERIALS.put(Material.END_STONE, Biome.THE_END);
//        MATERIALS.put(Material.END_STONE_BRICKS, Biome.END_MIDLANDS);
//        MATERIALS.put(Material.YELLOW_GLAZED_TERRACOTTA, Biome.SMALL_END_ISLANDS);
        // VOID
//        MATERIALS.put(Material.PHANTOM_MEMBRANE, Biome.THE_VOID);
    }
}
