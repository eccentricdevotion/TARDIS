/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.custommodeldata;

import java.util.HashMap;

public class TARDISMushroomBlockData {

    public static final String BROWN_MUSHROOM_DATA_ALL = "minecraft:brown_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=true]";
    public static final String RED_MUSHROOM_DATA_ALL = "minecraft:red_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=true]";
    public static final String MUSHROOM_STEM_DATA_ALL = "minecraft:mushroom_stem[down=true,east=true,north=true,south=true,up=true,west=true]";

    public static final HashMap<Integer, String> BROWN_MUSHROOM_DATA = new HashMap<Integer, String>() {
        {
            // the_moment
            put(1, "minecraft:brown_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=true]");
            // siege_cube
            put(2, "minecraft:brown_mushroom_block[down=false,east=false,north=false,south=true,up=false,west=false]");
            // tardis_blue
            put(3, "minecraft:brown_mushroom_block[down=false,east=false,north=false,south=true,up=false,west=true]");
            // tardis_white
            put(4, "minecraft:brown_mushroom_block[down=false,east=false,north=true,south=false,up=false,west=false]");
            // tardis_orange
            put(5, "minecraft:brown_mushroom_block[down=false,east=false,north=true,south=false,up=false,west=true]");
            // tardis_magenta
            put(6, "minecraft:brown_mushroom_block[down=false,east=false,north=true,south=true,up=false,west=false]");
            // tardis_light_blue
            put(7, "minecraft:brown_mushroom_block[down=false,east=false,north=true,south=true,up=false,west=true]");
            // tardis_yellow
            put(8, "minecraft:brown_mushroom_block[down=false,east=false,north=true,south=true,up=true,west=false]");
            // tardis_lime
            put(9, "minecraft:brown_mushroom_block[down=false,east=false,north=true,south=true,up=true,west=true]");
            // tardis_pink
            put(10, "minecraft:brown_mushroom_block[down=false,east=true,north=false,south=false,up=false,west=false]");
            // tardis_gray
            put(11, "minecraft:brown_mushroom_block[down=false,east=true,north=false,south=false,up=false,west=true]");
            // tardis_light_gray
            put(12, "minecraft:brown_mushroom_block[down=false,east=true,north=false,south=false,up=true,west=true]");
            // tardis_cyan
            put(13, "minecraft:brown_mushroom_block[down=false,east=true,north=false,south=true,up=false,west=false]");
            // tardis_purple
            put(14, "minecraft:brown_mushroom_block[down=false,east=true,north=false,south=true,up=false,west=true]");
            // tardis_brown
            put(15, "minecraft:brown_mushroom_block[down=false,east=true,north=false,south=true,up=true,west=true]");
            // tardis_green
            put(16, "minecraft:brown_mushroom_block[down=false,east=true,north=true,south=false,up=false,west=false]");
            // tardis_red
            put(17, "minecraft:brown_mushroom_block[down=false,east=true,north=true,south=false,up=false,west=true]");
            // tardis_black
            put(18, "minecraft:brown_mushroom_block[down=false,east=true,north=true,south=false,up=true,west=true]");
            // tardis_blue_south
            put(19, "minecraft:brown_mushroom_block[down=false,east=true,north=true,south=true,up=false,west=false]");
            // tardis_white_south
            put(20, "minecraft:brown_mushroom_block[down=false,east=true,north=true,south=true,up=false,west=true]");
            // tardis_orange_south
            put(21, "minecraft:brown_mushroom_block[down=false,east=true,north=true,south=true,up=true,west=false]");
            // tardis_magenta_south
            put(22, "minecraft:brown_mushroom_block[down=false,east=true,north=true,south=true,up=true,west=true]");
            // tardis_light_blue_south
            put(23, "minecraft:brown_mushroom_block[down=true,east=false,north=false,south=false,up=false,west=false]");
            // tardis_yellow_south
            put(24, "minecraft:brown_mushroom_block[down=true,east=false,north=false,south=false,up=false,west=true]");
            // tardis_lime_south
            put(25, "minecraft:brown_mushroom_block[down=true,east=false,north=false,south=false,up=true,west=false]");
            // tardis_pink_south
            put(26, "minecraft:brown_mushroom_block[down=true,east=false,north=false,south=false,up=true,west=true]");
            // tardis_gray_south
            put(27, "minecraft:brown_mushroom_block[down=true,east=false,north=false,south=true,up=false,west=false]");
            // tardis_light_gray_south
            put(28, "minecraft:brown_mushroom_block[down=true,east=false,north=false,south=true,up=false,west=true]");
            // tardis_cyan_south
            put(29, "minecraft:brown_mushroom_block[down=true,east=false,north=false,south=true,up=true,west=false]");
            // tardis_purple_south
            put(30, "minecraft:brown_mushroom_block[down=true,east=false,north=false,south=true,up=true,west=true]");
            // tardis_brown_south
            put(31, "minecraft:brown_mushroom_block[down=true,east=false,north=true,south=false,up=false,west=false]");
            // tardis_green_south
            put(32, "minecraft:brown_mushroom_block[down=true,east=false,north=true,south=false,up=false,west=true]");
            // tardis_red_south
            put(33, "minecraft:brown_mushroom_block[down=true,east=false,north=true,south=false,up=true,west=false]");
            // tardis_black_south
            put(34, "minecraft:brown_mushroom_block[down=true,east=false,north=true,south=false,up=true,west=true]");
            // tardis_blue_west
            put(35, "minecraft:brown_mushroom_block[down=true,east=false,north=true,south=true,up=false,west=false]");
            // tardis_white_west
            put(36, "minecraft:brown_mushroom_block[down=true,east=false,north=true,south=true,up=false,west=true]");
            // tardis_orange_west
            put(37, "minecraft:brown_mushroom_block[down=true,east=false,north=true,south=true,up=true,west=false]");
            // tardis_magenta_west
            put(38, "minecraft:brown_mushroom_block[down=true,east=false,north=true,south=true,up=true,west=true]");
            // tardis_light_blue_west
            put(39, "minecraft:brown_mushroom_block[down=true,east=true,north=false,south=false,up=false,west=false]");
            // tardis_yellow_west
            put(40, "minecraft:brown_mushroom_block[down=true,east=true,north=false,south=false,up=false,west=true]");
            // tardis_lime_west
            put(41, "minecraft:brown_mushroom_block[down=true,east=true,north=false,south=false,up=true,west=false]");
            // tardis_pink_west
            put(42, "minecraft:brown_mushroom_block[down=true,east=true,north=false,south=false,up=true,west=true]");
            // tardis_gray_west
            put(43, "minecraft:brown_mushroom_block[down=true,east=true,north=false,south=true,up=false,west=false]");
            // tardis_light_gray_west
            put(44, "minecraft:brown_mushroom_block[down=true,east=true,north=false,south=true,up=false,west=true]");
            // tardis_cyan_west
            put(45, "minecraft:brown_mushroom_block[down=true,east=true,north=false,south=true,up=true,west=false]");
            // tardis_purple_west
            put(46, "minecraft:brown_mushroom_block[down=true,east=true,north=false,south=true,up=true,west=true]");
            // tardis_brown_west
            put(47, "minecraft:brown_mushroom_block[down=true,east=true,north=true,south=false,up=false,west=false]");
            // tardis_green_west
            put(48, "minecraft:brown_mushroom_block[down=true,east=true,north=true,south=false,up=false,west=true]");
            // tardis_red_west
            put(49, "minecraft:brown_mushroom_block[down=true,east=true,north=true,south=false,up=true,west=false]");
            // tardis_black_west
            put(50, "minecraft:brown_mushroom_block[down=true,east=true,north=true,south=false,up=true,west=true]");
            // tardis_blue_north
            put(51, "minecraft:brown_mushroom_block[down=true,east=true,north=true,south=true,up=false,west=false]");
            // tardis_white_north
            put(52, "minecraft:brown_mushroom_block[down=true,east=true,north=true,south=true,up=false,west=true]");
            // tardis_orange_north
            put(53, "minecraft:brown_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=false]");
        }
    };

    public static final HashMap<Integer, String> RED_MUSHROOM_DATA = new HashMap<Integer, String>() {
        {
            // tardis_magenta_north
            put(1, "minecraft:red_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=false]");
            // tardis_light_blue_north
            put(2, "minecraft:red_mushroom_block[down=false,east=false,north=true,south=true,up=false,west=false]");
            // tardis_yellow_north
            put(3, "minecraft:red_mushroom_block[down=false,east=false,north=true,south=true,up=false,west=true]");
            // tardis_lime_north
            put(4, "minecraft:red_mushroom_block[down=false,east=false,north=true,south=true,up=true,west=false]");
            // tardis_pink_north
            put(5, "minecraft:red_mushroom_block[down=false,east=false,north=true,south=true,up=true,west=true]");
            // tardis_gray_north
            put(6, "minecraft:red_mushroom_block[down=false,east=true,north=false,south=false,up=false,west=true]");
            // tardis_light_gray_north
            put(7, "minecraft:red_mushroom_block[down=false,east=true,north=false,south=false,up=true,west=true]");
            // tardis_cyan_north
            put(8, "minecraft:red_mushroom_block[down=false,east=true,north=false,south=true,up=false,west=true]");
            // tardis_purple_north
            put(9, "minecraft:red_mushroom_block[down=false,east=true,north=false,south=true,up=true,west=true]");
            // tardis_brown_north
            put(10, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=false,up=false,west=true]");
            // tardis_green_north
            put(11, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=false,up=true,west=true]");
            // tardis_red_north
            put(12, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=true,up=false,west=false]");
            // tardis_black_north
            put(13, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=true,up=false,west=true]");
            // ars
            put(14, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=true,up=true,west=false]");
            // bigger
            put(15, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=true,up=true,west=true]");
            // budget
            put(16, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=false,up=false,west=false]");
            // coral
            put(17, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=false,up=false,west=true]");
            // deluxe
            put(18, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=false,up=true,west=false]");
            // eleventh
            put(19, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=false,up=true,west=true]");
            // ender
            put(20, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=true,up=false,west=false]");
            // plank
            put(21, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=true,up=false,west=true]");
            // pyramid
            put(22, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=true,up=true,west=false]");
            // redstone
            put(23, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=true,up=true,west=true]");
            // steampunk
            put(24, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=false,up=false,west=false]");
            // thirteenth
            put(25, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=false,up=false,west=true]");
            // factory
            put(26, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=false,up=true,west=false]");
            // tom
            put(27, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=false,up=true,west=true]");
            // twelfth
            put(28, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=true,up=false,west=false]");
            // war
            put(29, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=true,up=false,west=true]");
            // small
            put(30, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=true,up=true,west=false]");
            // medium
            put(31, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=true,up=true,west=true]");
            // tall
            put(32, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=false,up=false,west=false]");
            // legacy_bigger
            put(33, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=false,up=false,west=true]");
            // legacy_budget
            put(34, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=false,up=true,west=false]");
            // legacy_deluxe
            put(35, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=false,up=true,west=true]");
            // legacy_eleventh
            put(36, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=true,up=false,west=false]");
            // legacy_redstone
            put(37, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=true,up=false,west=true]");
            // pandorica
            put(38, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=true,up=true,west=false]");
            // master
            put(39, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=true,up=true,west=true]");
            // creative (atomic elements)
            put(40, "minecraft:red_mushroom_block[down=true,east=true,north=true,south=false,up=false,west=false]");
            // chemical compounds
            put(41, "minecraft:red_mushroom_block[down=true,east=true,north=true,south=false,up=false,west=true]");
            // material reducer (chemistry)
            put(42, "minecraft:red_mushroom_block[down=true,east=true,north=true,south=false,up=true,west=false]");
            // element constructor (chemistry)
            put(43, "minecraft:red_mushroom_block[down=true,east=true,north=true,south=false,up=true,west=true]");
            // lab table (chemistry)
            put(44, "minecraft:red_mushroom_block[down=true,east=true,north=true,south=true,up=false,west=false]");
            // product crafting (chemistry)
            put(45, "minecraft:red_mushroom_block[down=true,east=true,north=true,south=true,up=false,west=true]");
        }
    };

    public static final HashMap<Integer, String> MUSHROOM_STEM_DATA = new HashMap<Integer, String>() {
        {
            // heat_block
            put(5, "minecraft:mushroom_stem[down=false,east=false,north=false,south=false,up=false,west=true]");
            // blue_lamp_on
            put(20000001, "minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=false]");
            // green_lamp_on
            put(20000004, "minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=true]");
            // purple_lamp_on
            put(20000003, "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=false]");
            // red_lamp_on
            put(20000002, "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=true]");
            // blue_lamp
            put(10000001, "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=false]");
            // green_lamp
            put(10000004, "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=true]");
            // purple_lamp
            put(10000003, "minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=false]");
            // red_lamp
            put(10000002, "minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=true]");
            // custom seed block
            put(45, "minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=true,west=false]");
            // orange hexagon
            put(46, "minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=true,west=true]");
            // white roundel
            put(47, "minecraft:mushroom_stem[down=true,east=true,north=false,south=false,up=false,west=false]");
            // white roundel offset
            put(48, "minecraft:mushroom_stem[down=true,east=true,north=false,south=false,up=false,west=true]");
            // cog
            put(49, "minecraft:mushroom_stem[down=true,east=true,north=false,south=false,up=true,west=false]");
        }
    };
}
