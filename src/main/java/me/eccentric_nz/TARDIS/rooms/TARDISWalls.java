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
package me.eccentric_nz.TARDIS.rooms;

import java.util.HashMap;
import java.util.LinkedHashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;

/**
 * A TARDIS isn't just a vehicle for travelling in space and time. As a TARDIS
 * has no real constraints on the amount of space it can use, most TARDISes
 * contain extensive areas which can be used as living quarters or storage
 * space.
 *
 * @author eccentric_nz
 */
public class TARDISWalls {

    public HashMap<String, Pair> blocks;

    public TARDISWalls() {
        blocks = new LinkedHashMap<String, Pair>();
        blocks.put("STONE", new Pair(Material.STONE, (byte) 0));
        blocks.put("COBBLESTONE", new Pair(Material.COBBLESTONE, (byte) 0));
        blocks.put("MOSSY_COBBLESTONE", new Pair(Material.MOSSY_COBBLESTONE, (byte) 0));
        blocks.put("ENDER_STONE", new Pair(Material.ENDER_STONE, (byte) 0));
        blocks.put("DIRT", new Pair(Material.DIRT, (byte) 0));
        blocks.put("COARSE_DIRT", new Pair(Material.DIRT, (byte) 1));
        blocks.put("PODZOL", new Pair(Material.DIRT, (byte) 2));
        blocks.put("OAK_WOOD", new Pair(Material.WOOD, (byte) 0));
        blocks.put("PINE_WOOD", new Pair(Material.WOOD, (byte) 1));
        blocks.put("SPRUCE_WOOD", new Pair(Material.WOOD, (byte) 1));
        blocks.put("BIRCH_WOOD", new Pair(Material.WOOD, (byte) 2));
        blocks.put("JUNGLE_WOOD", new Pair(Material.WOOD, (byte) 3));
        blocks.put("ACACIA_WOOD", new Pair(Material.WOOD, (byte) 4));
        blocks.put("DARK_OAK_WOOD", new Pair(Material.WOOD, (byte) 5));
        blocks.put("OAK_LOG", new Pair(Material.LOG, (byte) 0));
        blocks.put("PINE_LOG", new Pair(Material.LOG, (byte) 1));
        blocks.put("SPRUCE_LOG", new Pair(Material.LOG, (byte) 1));
        blocks.put("BIRCH_LOG", new Pair(Material.LOG, (byte) 2));
        blocks.put("JUNGLE_LOG", new Pair(Material.LOG, (byte) 3));
        blocks.put("ACACIA_LOG", new Pair(Material.LOG_2, (byte) 0));
        blocks.put("DARK_OAK_LOG", new Pair(Material.LOG_2, (byte) 1));
        blocks.put("SANDSTONE", new Pair(Material.SANDSTONE, (byte) 0));
        blocks.put("CHISELED_SANDSTONE", new Pair(Material.SANDSTONE, (byte) 1));
        blocks.put("SMOOTH_SANDSTONE", new Pair(Material.SANDSTONE, (byte) 2));
        blocks.put("WHITE_WOOL", new Pair(Material.WOOL, (byte) 0));
        blocks.put("ORANGE_WOOL", new Pair(Material.WOOL, (byte) 1));
        blocks.put("MAGENTA_WOOL", new Pair(Material.WOOL, (byte) 2));
        blocks.put("LIGHT_BLUE_WOOL", new Pair(Material.WOOL, (byte) 3));
        blocks.put("YELLOW_WOOL", new Pair(Material.WOOL, (byte) 4));
        blocks.put("LIGHT_GREEN_WOOL", new Pair(Material.WOOL, (byte) 5));
        blocks.put("PINK_WOOL", new Pair(Material.WOOL, (byte) 6));
        blocks.put("GREY_WOOL", new Pair(Material.WOOL, (byte) 7));
        blocks.put("LIGHT_GREY_WOOL", new Pair(Material.WOOL, (byte) 8));
        blocks.put("GRAY_WOOL", new Pair(Material.WOOL, (byte) 7));
        blocks.put("LIGHT_GRAY_WOOL", new Pair(Material.WOOL, (byte) 8));
        blocks.put("CYAN_WOOL", new Pair(Material.WOOL, (byte) 9));
        blocks.put("PURPLE_WOOL", new Pair(Material.WOOL, (byte) 10));
        blocks.put("BLUE_WOOL", new Pair(Material.WOOL, (byte) 11));
        blocks.put("BROWN_WOOL", new Pair(Material.WOOL, (byte) 12));
        blocks.put("GREEN_WOOL", new Pair(Material.WOOL, (byte) 13));
        blocks.put("RED_WOOL", new Pair(Material.WOOL, (byte) 14));
        blocks.put("BLACK_WOOL", new Pair(Material.WOOL, (byte) 15));
        blocks.put("BRICK", new Pair(Material.BRICK, (byte) 0));
        blocks.put("CLAY", new Pair(Material.CLAY, (byte) 0));
        blocks.put("HARD_CLAY", new Pair(Material.HARD_CLAY, (byte) 0));
        blocks.put("WHITE_CLAY", new Pair(Material.STAINED_CLAY, (byte) 0));
        blocks.put("ORANGE_CLAY", new Pair(Material.STAINED_CLAY, (byte) 1));
        blocks.put("MAGENTA_CLAY", new Pair(Material.STAINED_CLAY, (byte) 2));
        blocks.put("LIGHT_BLUE_CLAY", new Pair(Material.STAINED_CLAY, (byte) 3));
        blocks.put("YELLOW_CLAY", new Pair(Material.STAINED_CLAY, (byte) 4));
        blocks.put("LIGHT_GREEN_CLAY", new Pair(Material.STAINED_CLAY, (byte) 5));
        blocks.put("PINK_CLAY", new Pair(Material.STAINED_CLAY, (byte) 6));
        blocks.put("GREY_CLAY", new Pair(Material.STAINED_CLAY, (byte) 7));
        blocks.put("LIGHT_GREY_CLAY", new Pair(Material.STAINED_CLAY, (byte) 8));
        blocks.put("GRAY_CLAY", new Pair(Material.STAINED_CLAY, (byte) 7));
        blocks.put("LIGHT_GRAY_CLAY", new Pair(Material.STAINED_CLAY, (byte) 8));
        blocks.put("CYAN_CLAY", new Pair(Material.STAINED_CLAY, (byte) 9));
        blocks.put("PURPLE_CLAY", new Pair(Material.STAINED_CLAY, (byte) 10));
        blocks.put("BLUE_CLAY", new Pair(Material.STAINED_CLAY, (byte) 11));
        blocks.put("BROWN_CLAY", new Pair(Material.STAINED_CLAY, (byte) 12));
        blocks.put("GREEN_CLAY", new Pair(Material.STAINED_CLAY, (byte) 13));
        blocks.put("RED_CLAY", new Pair(Material.STAINED_CLAY, (byte) 14));
        blocks.put("BLACK_CLAY", new Pair(Material.STAINED_CLAY, (byte) 15));
        blocks.put("STONE_BRICK", new Pair(Material.SMOOTH_BRICK, (byte) 0));
        blocks.put("SMOOTH_BRICK", new Pair(Material.SMOOTH_BRICK, (byte) 0));
        blocks.put("MOSSY_BRICK", new Pair(Material.SMOOTH_BRICK, (byte) 1));
        blocks.put("CRACKED_BRICK", new Pair(Material.SMOOTH_BRICK, (byte) 2));
        blocks.put("CHISELED_BRICK", new Pair(Material.SMOOTH_BRICK, (byte) 3));
        blocks.put("CHISELED_STONE", new Pair(Material.SMOOTH_BRICK, (byte) 3));
        blocks.put("NETHER_BRICK", new Pair(Material.NETHER_BRICK, (byte) 0));
        blocks.put("RED_NETHER_BRICK", new Pair(Material.RED_NETHER_BRICK, (byte) 0));
        blocks.put("NETHERRACK", new Pair(Material.NETHERRACK, (byte) 0));
        blocks.put("SOUL_SAND", new Pair(Material.SOUL_SAND, (byte) 0));
        blocks.put("HUGE_MUSHROOM_1", new Pair(Material.HUGE_MUSHROOM_1, (byte) 14));
        blocks.put("HUGE_MUSHROOM_2", new Pair(Material.HUGE_MUSHROOM_2, (byte) 14));
        blocks.put("HUGE_MUSHROOM_STEM", new Pair(Material.HUGE_MUSHROOM_2, (byte) 15));
        blocks.put("QUARTZ", new Pair(Material.QUARTZ_BLOCK, (byte) 0));
        blocks.put("CHISELED_QUARTZ", new Pair(Material.QUARTZ_BLOCK, (byte) 1));
        blocks.put("QUARTZ_PILLAR", new Pair(Material.QUARTZ_BLOCK, (byte) 2));
        blocks.put("HAY", new Pair(Material.HAY_BLOCK, (byte) 0));
        blocks.put("PACKED_ICE", new Pair(Material.PACKED_ICE, (byte) 0));
        blocks.put("PRISMARINE", new Pair(Material.PRISMARINE, (byte) 0));
        blocks.put("PRISMARINE_BRICKS", new Pair(Material.PRISMARINE, (byte) 1));
        blocks.put("DARK_PRISMARINE", new Pair(Material.PRISMARINE, (byte) 2));
        blocks.put("RED_SANDSTONE", new Pair(Material.RED_SANDSTONE, (byte) 0));
        blocks.put("CHISELED_RED_SANDSTONE", new Pair(Material.RED_SANDSTONE, (byte) 1));
        blocks.put("SMOOTH_RED_SANDSTONE", new Pair(Material.RED_SANDSTONE, (byte) 2));
        blocks.put("GRANITE", new Pair(Material.STONE, (byte) 1));
        blocks.put("POLISHED_GRANITE", new Pair(Material.STONE, (byte) 2));
        blocks.put("DIORITE", new Pair(Material.STONE, (byte) 3));
        blocks.put("POLISHED_DIORITE", new Pair(Material.STONE, (byte) 4));
        blocks.put("ANDESITE", new Pair(Material.STONE, (byte) 5));
        blocks.put("POLISHED_ANDESITE", new Pair(Material.STONE, (byte) 6));
        blocks.put("END_STONE_BRICK", new Pair(Material.END_BRICKS, (byte) 0));
        blocks.put("PURPUR", new Pair(Material.PURPUR_BLOCK, (byte) 10));
        blocks.put("PURPUR_PILLAR", new Pair(Material.PURPUR_PILLAR, (byte) 10));
        blocks.put("BONE_BLOCK", new Pair(Material.BONE_BLOCK, (byte) 0));
        blocks.put("NETHER_WART_BLOCK", new Pair(Material.NETHER_WART_BLOCK, (byte) 0));
        if (TARDIS.plugin.getConfig().getBoolean("allow.all_blocks")) {
            blocks.put("BEDROCK", new Pair(Material.BEDROCK, (byte) 0));
            blocks.put("COAL_ORE", new Pair(Material.COAL_ORE, (byte) 0));
            blocks.put("DIAMOND_BLOCK", new Pair(Material.DIAMOND_BLOCK, (byte) 0));
            blocks.put("DIAMOND_ORE", new Pair(Material.DIAMOND_ORE, (byte) 0));
            blocks.put("EMERALD_BLOCK", new Pair(Material.EMERALD_BLOCK, (byte) 0));
            blocks.put("EMERALD_ORE", new Pair(Material.EMERALD_ORE, (byte) 0));
            blocks.put("QUARTZ_ORE", new Pair(Material.QUARTZ_ORE, (byte) 0));
            blocks.put("GOLD_BLOCK", new Pair(Material.GOLD_BLOCK, (byte) 0));
            blocks.put("GOLD_ORE", new Pair(Material.GOLD_ORE, (byte) 0));
            blocks.put("IRON_BLOCK", new Pair(Material.IRON_BLOCK, (byte) 0));
            blocks.put("IRON_ORE", new Pair(Material.IRON_ORE, (byte) 0));
            blocks.put("JACK_O_LANTERN", new Pair(Material.JACK_O_LANTERN, (byte) 0));
            blocks.put("LAPIS_BLOCK", new Pair(Material.LAPIS_BLOCK, (byte) 0));
            blocks.put("LAPIS_LAZULI", new Pair(Material.LAPIS_BLOCK, (byte) 0));
            blocks.put("MELON", new Pair(Material.MELON, (byte) 0));
            blocks.put("OBSIDIAN", new Pair(Material.OBSIDIAN, (byte) 0));
            blocks.put("PUMPKIN", new Pair(Material.PUMPKIN, (byte) 0));
            blocks.put("REDSTONE_BLOCK", new Pair(Material.REDSTONE_BLOCK, (byte) 0));
            blocks.put("REDSTONE_ORE", new Pair(Material.REDSTONE_ORE, (byte) 0));
            blocks.put("SEA_LANTERN", new Pair(Material.SEA_LANTERN, (byte) 0));
        }
    }

    public static class Pair {

        private final Material type;
        private final Byte data;

        public Pair(Material type, Byte data) {
            this.type = type;
            this.data = data;
        }

        public Material getType() {
            return type;
        }

        public Byte getData() {
            return data;
        }
    }
}
