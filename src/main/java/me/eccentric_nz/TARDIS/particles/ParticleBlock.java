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
package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.List;

public class ParticleBlock {

    public static final List<String> blocks = List.of(
            "STONE", "END_STONE", "NETHERRACK", "GRASS_BLOCK", "PODZOL", "MYCELIUM", "DIRT", "COARSE_DIRT",
            "ROOTED_DIRT", "MUD", "CLAY", "GRAVEL", "SAND", "SANDSTONE", "RED_SAND", "RED_SANDSTONE", "ICE", "PACKED_ICE",
            "BLUE_ICE", "SNOW_BLOCK", "MOSS_BLOCK", "DEEPSLATE", "GRANITE", "DIORITE", "ANDESITE", "CALCITE", "TUFF",
            "DRIPSTONE_BLOCK", "PRISMARINE", "MAGMA_BLOCK", "OBSIDIAN", "CRYING_OBSIDIAN", "CRIMSON_NYLIUM", "WARPED_NYLIUM",
            "SOUL_SAND", "SOUL_SOIL", "BONE_BLOCK", "BLACKSTONE", "BASALT", "SMOOTH_BASALT", "COAL_ORE", "IRON_ORE",
            "COPPER_ORE", "GOLD_ORE", "REDSTONE_ORE", "EMERALD_ORE", "LAPIS_ORE", "DIAMOND_ORE", "DEEPSLATE_COAL_ORE",
            "DEEPSLATE_IRON_ORE", "DEEPSLATE_COPPER_ORE", "DEEPSLATE_GOLD_ORE", "DEEPSLATE_REDSTONE_ORE", "DEEPSLATE_EMERALD_ORE",
            "DEEPSLATE_LAPIS_ORE", "DEEPSLATE_DIAMOND_ORE", "NETHER_GOLD_ORE", "NETHER_QUARTZ_ORE", "ANCIENT_DEBRIS",
            "RAW_IRON_BLOCK", "RAW_COPPER_BLOCK", "RAW_GOLD_BLOCK", "GLOWSTONE", "AMETHYST_BLOCK", "BUDDING_AMETHYST",
            "ACACIA_LOG", "BIRCH_LOG", "SPRUCE_LOG", "OAK_LOG", "DARK_OAK_LOG", "CHERRY_LOG", "JUNGLE_LOG", "MANGROVE_LOG",
            "MANGROVE_ROOTS", "MUDDY_MANGROVE_ROOTS", "MUSHROOM_STEM", "ACACIA_LEAVES", "AZALEA_LEAVES", "BIRCH_LEAVES",
            "SPRUCE_LEAVES", "OAK_LEAVES", "DARK_OAK_LEAVES", "CHERRY_LEAVES", "FLOWERING_AZALEA_LEAVES", "JUNGLE_LEAVES",
            "MANGROVE_LEAVES", "RED_MUSHROOM_BLOCK", "BROWN_MUSHROOM_BLOCK", "NETHER_WART_BLOCK", "WARPED_WART_BLOCK",
            "SHROOMLIGHT", "TERRACOTTA", "BLACK_TERRACOTTA", "BLUE_TERRACOTTA", "BROWN_TERRACOTTA", "CYAN_TERRACOTTA",
            "GRAY_TERRACOTTA", "LIGHT_GRAY_TERRACOTTA", "GREEN_TERRACOTTA", "MAGENTA_TERRACOTTA", "YELLOW_TERRACOTTA",
            "LIGHT_BLUE_TERRACOTTA", "ORANGE_TERRACOTTA", "CYAN_TERRACOTTA", "LIME_TERRACOTTA", "PURPLE_TERRACOTTA",
            "PINK_TERRACOTTA", "WHITE_TERRACOTTA", "SPONGE", "WET_SPONGE", "MELON", "PUMPKIN", "HAY_BLOCK", "BEE_NEST",
            "HONEYCOMB_BLOCK", "SLIME_BLOCK", "HONEY_BLOCK", "OCHRE_FROGLIGHT", "PEARLESCENT_FROGLIGHT", "VERDANT_FROGLIGHT", "SCULK");

    public static BlockData fromString(String str) {
        try {
            Material material = Material.valueOf(str);
            return material.createBlockData();
        } catch (IllegalArgumentException e) {
            return TARDISConstants.BLACK;
        }
    }
}
