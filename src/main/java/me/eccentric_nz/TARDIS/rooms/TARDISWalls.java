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

    public HashMap<String, int[]> blocks;

    public TARDISWalls() {
        blocks = new HashMap<String, int[]>();
        blocks.put("STONE", new int[]{1, 0});
        blocks.put("COBBLESTONE", new int[]{4, 0});
        blocks.put("MOSSY_COBBLESTONE", new int[]{48, 0});
        blocks.put("ENDER_STONE", new int[]{121, 0});
        blocks.put("DIRT", new int[]{3, 0});
        blocks.put("OAK_WOOD", new int[]{5, 0});
        blocks.put("PINE_WOOD", new int[]{5, 1});
        blocks.put("SPRUCE_WOOD", new int[]{5, 1});
        blocks.put("BIRCH_WOOD", new int[]{5, 2});
        blocks.put("JUNGLE_WOOD", new int[]{5, 3});
        blocks.put("OAK_LOG", new int[]{17, 0});
        blocks.put("PINE_LOG", new int[]{17, 1});
        blocks.put("SPRUCE_LOG", new int[]{17, 1});
        blocks.put("BIRCH_LOG", new int[]{17, 2});
        blocks.put("JUNGLE_LOG", new int[]{17, 3});
        blocks.put("SANDSTONE", new int[]{24, 0});
        blocks.put("CHISELED_SANDSTONE", new int[]{24, 1});
        blocks.put("SMOOTH_SANDSTONE", new int[]{24, 2});
        blocks.put("WHITE_WOOL", new int[]{35, 0});
        blocks.put("ORANGE_WOOL", new int[]{35, 1});
        blocks.put("MAGENTA_WOOL", new int[]{35, 2});
        blocks.put("LIGHT_BLUE_WOOL", new int[]{35, 3});
        blocks.put("YELLOW_WOOL", new int[]{35, 4});
        blocks.put("LIGHT_GREEN_WOOL", new int[]{35, 5});
        blocks.put("PINK_WOOL", new int[]{35, 6});
        blocks.put("GREY_WOOL", new int[]{35, 7});
        blocks.put("LIGHT_GREY_WOOL", new int[]{35, 8});
        blocks.put("GRAY_WOOL", new int[]{35, 7});
        blocks.put("LIGHT_GRAY_WOOL", new int[]{35, 8});
        blocks.put("CYAN_WOOL", new int[]{35, 9});
        blocks.put("PURPLE_WOOL", new int[]{35, 10});
        blocks.put("BLUE_WOOL", new int[]{35, 11});
        blocks.put("BROWN_WOOL", new int[]{35, 12});
        blocks.put("GREEN_WOOL", new int[]{35, 13});
        blocks.put("RED_WOOL", new int[]{35, 14});
        blocks.put("BLACK_WOOL", new int[]{35, 15});
        blocks.put("BRICK", new int[]{45, 0});
        blocks.put("CLAY", new int[]{82, 0});
        blocks.put("HARD_CLAY", new int[]{172, 0});
        blocks.put("WHITE_CLAY", new int[]{159, 0});
        blocks.put("ORANGE_CLAY", new int[]{159, 1});
        blocks.put("MAGENTA_CLAY", new int[]{159, 2});
        blocks.put("LIGHT_BLUE_CLAY", new int[]{159, 3});
        blocks.put("YELLOW_CLAY", new int[]{159, 4});
        blocks.put("LIGHT_GREEN_CLAY", new int[]{159, 5});
        blocks.put("PINK_CLAY", new int[]{159, 6});
        blocks.put("GREY_CLAY", new int[]{159, 7});
        blocks.put("LIGHT_GREY_CLAY", new int[]{159, 8});
        blocks.put("GRAY_CLAY", new int[]{159, 7});
        blocks.put("LIGHT_GRAY_CLAY", new int[]{159, 8});
        blocks.put("CYAN_CLAY", new int[]{159, 9});
        blocks.put("PURPLE_CLAY", new int[]{159, 10});
        blocks.put("BLUE_CLAY", new int[]{159, 11});
        blocks.put("BROWN_CLAY", new int[]{159, 12});
        blocks.put("GREEN_CLAY", new int[]{159, 13});
        blocks.put("RED_CLAY", new int[]{159, 14});
        blocks.put("BLACK_CLAY", new int[]{159, 15});
        blocks.put("STONE_BRICK", new int[]{98, 0});
        blocks.put("SMOOTH_BRICK", new int[]{98, 0});
        blocks.put("MOSSY_BRICK", new int[]{98, 1});
        blocks.put("CRACKED_BRICK", new int[]{98, 2});
        blocks.put("CHISELED_BRICK", new int[]{98, 3});
        blocks.put("CHISELED_STONE", new int[]{98, 3});
        blocks.put("NETHER_BRICK", new int[]{112, 0});
        blocks.put("NETHERRACK", new int[]{87, 0});
        blocks.put("SOUL_SAND", new int[]{88, 0});
        blocks.put("HUGE_MUSHROOM_1", new int[]{99, 14});
        blocks.put("HUGE_MUSHROOM_2", new int[]{100, 14});
        blocks.put("HUGE_MUSHROOM_STEM", new int[]{100, 15});
        blocks.put("QUARTZ", new int[]{155, 0});
        blocks.put("CHISELED_QUARTZ", new int[]{155, 1});
        blocks.put("QUARTZ_PILLAR", new int[]{155, 2});
        blocks.put("HAY", new int[]{170, 0});
        if (TARDIS.plugin.getConfig().getBoolean("all_blocks")) {
            blocks.put("BEDROCK", new int[]{7, 0});
            blocks.put("COAL_ORE", new int[]{16, 0});
            blocks.put("DIAMOND_BLOCK", new int[]{57, 0});
            blocks.put("DIAMOND_ORE", new int[]{56, 0});
            blocks.put("EMERALD_BLOCK", new int[]{133, 0});
            blocks.put("EMERALD_ORE", new int[]{129, 0});
            blocks.put("QUARTZ_ORE", new int[]{153, 0});
            blocks.put("GOLD_BLOCK", new int[]{41, 0});
            blocks.put("GOLD_ORE", new int[]{14, 0});
            blocks.put("IRON_BLOCK", new int[]{42, 0});
            blocks.put("IRON_ORE", new int[]{15, 0});
            blocks.put("JACK_O_LANTERN", new int[]{91, 0});
            blocks.put("LAPIS_BLOCK", new int[]{22, 0});
            blocks.put("LAPIS_LAZULI", new int[]{22, 0});
            blocks.put("MELON", new int[]{103, 0});
            blocks.put("OBSIDIAN", new int[]{49, 0});
            blocks.put("PUMPKIN", new int[]{86, 0});
            blocks.put("REDSTONE_BLOCK", new int[]{152, 0});
            blocks.put("REDSTONE_ORE", new int[]{73, 0});
        }
    }
}
