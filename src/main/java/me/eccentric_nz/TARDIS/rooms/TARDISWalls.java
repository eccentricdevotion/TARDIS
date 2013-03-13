/*
 * Copyright (C) 2013 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * A TARDIS isn't just a vehicle for travelling in space and time. As a TARDIS
 * has no real constraints on the amount of space it can use, most TARDISes
 * contain extensive areas which can be used as living quarters or storage
 * space.
 *
 * @author eccentric_nz
 */
public class TARDISWalls {

    public HashMap<String, Integer[]> blocks;

    public TARDISWalls() {
        blocks = new HashMap<String, Integer[]>();
        blocks.put("STONE", new Integer[]{1, 0});
        blocks.put("COBBLESTONE", new Integer[]{4, 0});
        blocks.put("MOSSY_COBBLESTONE", new Integer[]{48, 0});
        blocks.put("ENDER_STONE", new Integer[]{121, 0});
        blocks.put("DIRT", new Integer[]{3, 0});
        blocks.put("OAK_WOOD", new Integer[]{5, 0});
        blocks.put("PINE_WOOD", new Integer[]{5, 1});
        blocks.put("SPRUCE_WOOD", new Integer[]{5, 1});
        blocks.put("BIRCH_WOOD", new Integer[]{5, 2});
        blocks.put("JUNGLE_WOOD", new Integer[]{5, 3});
        blocks.put("OAK_LOG", new Integer[]{17, 0});
        blocks.put("PINE_LOG", new Integer[]{17, 1});
        blocks.put("SPRUCE_LOG", new Integer[]{17, 1});
        blocks.put("BIRCH_LOG", new Integer[]{17, 2});
        blocks.put("JUNGLE_LOG", new Integer[]{17, 3});
        blocks.put("SANDSTONE", new Integer[]{24, 0});
        blocks.put("CHISELED_SANDSTONE", new Integer[]{24, 1});
        blocks.put("SMOOTH_SANDSTONE", new Integer[]{24, 2});
        blocks.put("WHITE_WOOL", new Integer[]{35, 0});
        blocks.put("ORANGE_WOOL", new Integer[]{35, 1});
        blocks.put("MAGENTA_WOOL", new Integer[]{35, 2});
        blocks.put("LIGHT_BLUE_WOOL", new Integer[]{35, 3});
        blocks.put("YELLOW_WOOL", new Integer[]{35, 4});
        blocks.put("LIGHT_GREEN_WOOL", new Integer[]{35, 5});
        blocks.put("PINK_WOOL", new Integer[]{35, 6});
        blocks.put("GREY_WOOL", new Integer[]{35, 7});
        blocks.put("LIGHT_GREY_WOOL", new Integer[]{35, 8});
        blocks.put("GRAY_WOOL", new Integer[]{35, 7});
        blocks.put("LIGHT_GRAY_WOOL", new Integer[]{35, 8});
        blocks.put("CYAN_WOOL", new Integer[]{35, 9});
        blocks.put("PURPLE_WOOL", new Integer[]{35, 10});
        blocks.put("BLUE_WOOL", new Integer[]{35, 11});
        blocks.put("BROWN_WOOL", new Integer[]{35, 12});
        blocks.put("GREEN_WOOL", new Integer[]{35, 13});
        blocks.put("RED_WOOL", new Integer[]{35, 14});
        blocks.put("BLACK_WOOL", new Integer[]{35, 15});
        blocks.put("BRICK", new Integer[]{45, 0});
        blocks.put("STONE_BRICK", new Integer[]{98, 0});
        blocks.put("SMOOTH_BRICK", new Integer[]{98, 0});
        blocks.put("MOSSY_BRICK", new Integer[]{98, 1});
        blocks.put("CRACKED_BRICK", new Integer[]{98, 2});
        blocks.put("CHISELED_BRICK", new Integer[]{98, 3});
        blocks.put("CHISELED_STONE", new Integer[]{98, 3});
        blocks.put("NETHER_BRICK", new Integer[]{112, 0});
        blocks.put("NETHERRACK", new Integer[]{87, 0});
        blocks.put("SOUL_SAND", new Integer[]{88, 0});
        blocks.put("HUGE_MUSHROOM_1", new Integer[]{99, 14});
        blocks.put("HUGE_MUSHROOM_2", new Integer[]{100, 14});
        if (TARDIS.plugin.getConfig().getBoolean("all_blocks")) {
            blocks.put("BEDROCK", new Integer[]{7, 0});
            blocks.put("COAL_ORE", new Integer[]{16, 0});
            blocks.put("DIAMOND_BLOCK", new Integer[]{57, 0});
            blocks.put("DIAMOND_ORE", new Integer[]{56, 0});
            blocks.put("EMERALD_BLOCK", new Integer[]{133, 0});
            blocks.put("EMERALD_ORE", new Integer[]{129, 0});
            blocks.put("GOLD_BLOCK", new Integer[]{41, 0});
            blocks.put("GOLD_ORE", new Integer[]{14, 0});
            blocks.put("IRON_BLOCK", new Integer[]{42, 0});
            blocks.put("IRON_ORE", new Integer[]{15, 0});
            blocks.put("JACK_O_LANTERN", new Integer[]{91, 0});
            blocks.put("LAPIS_BLOCK", new Integer[]{22, 0});
            blocks.put("LAPIS_LAZULI", new Integer[]{22, 0});
            blocks.put("MELON", new Integer[]{103, 0});
            blocks.put("OBSIDIAN", new Integer[]{49, 0});
            blocks.put("PUMPKIN", new Integer[]{86, 0});
            blocks.put("REDSTONE_ORE", new Integer[]{73, 0});
        }
    }
}
