/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.customblocks;

import java.util.HashMap;
import org.bukkit.block.data.BlockData;

public class TARDISMushroomBlockData {

    public static final String BROWN_MUSHROOM_DATA_ALL = "minecraft:brown_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=true]";
    public static final String RED_MUSHROOM_DATA_ALL = "minecraft:red_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=true]";
    public static final String MUSHROOM_STEM_DATA_ALL = "minecraft:mushroom_stem[down=true,east=true,north=true,south=true,up=true,west=true]";

    public static final HashMap<Integer, String> BROWN_MUSHROOM_DATA = new HashMap<>() {
        {
            // the_moment
            put(1, "minecraft:brown_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=true]");
            // siege_cube
            put(2, "minecraft:brown_mushroom_block[down=false,east=false,north=false,south=true,up=false,west=false]");
        }
    };

    public static final HashMap<Integer, String> RED_MUSHROOM_DATA = new HashMap<>() {
        {
            // mechanical seed block
            put(10, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=false,up=false,west=true]");
            // fugitive seed block
            put(11, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=false,up=true,west=true]");
            // ancient seed block
            put(12, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=true,up=false,west=false]");
            // division seed block
            put(13, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=true,up=false,west=true]");
            // ars seed block
            put(14, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=true,up=true,west=false]");
            // bigger seed block
            put(15, "minecraft:red_mushroom_block[down=false,east=true,north=true,south=true,up=true,west=true]");
            // budget seed block
            put(16, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=false,up=false,west=false]");
            // coral seed block
            put(17, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=false,up=false,west=true]");
            // deluxe seed block
            put(18, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=false,up=true,west=false]");
            // eleventh seed block
            put(19, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=false,up=true,west=true]");
            // ender seed block
            put(20, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=true,up=false,west=false]");
            // plank seed block
            put(21, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=true,up=false,west=true]");
            // pyramid seed block
            put(22, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=true,up=true,west=false]");
            // redstone seed block
            put(23, "minecraft:red_mushroom_block[down=true,east=false,north=false,south=true,up=true,west=true]");
            // steampunk seed block
            put(24, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=false,up=false,west=false]");
            // thirteenth seed block
            put(25, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=false,up=false,west=true]");
            // factory seed block
            put(26, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=false,up=true,west=false]");
            // tom seed block
            put(27, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=false,up=true,west=true]");
            // twelfth seed block
            put(28, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=true,up=false,west=false]");
            // war seed block
            put(29, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=true,up=false,west=true]");
            // small seed block
            put(30, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=true,up=true,west=false]");
            // medium seed block
            put(31, "minecraft:red_mushroom_block[down=true,east=false,north=true,south=true,up=true,west=true]");
            // tall seed block
            put(32, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=false,up=false,west=false]");
            // legacy_bigger seed block
            put(33, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=false,up=false,west=true]");
            // legacy_budget seed block
            put(34, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=false,up=true,west=false]");
            // legacy_deluxe seed block
            put(35, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=false,up=true,west=true]");
            // legacy_eleventh seed block
            put(36, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=true,up=false,west=false]");
            // legacy_redstone seed block
            put(37, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=true,up=false,west=true]");
            // pandorica
            put(38, "minecraft:red_mushroom_block[down=true,east=true,north=false,south=true,up=true,west=false]");
            // master seed block
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

    public static final HashMap<Integer, String> MUSHROOM_STEM_DATA = new HashMap<>() {
        {
            // heat_block
            put(5, "minecraft:mushroom_stem[down=false,east=false,north=false,south=false,up=false,west=true]");
            // blue_lamp_on
            put(10000001, "minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=false]");
            // green_lamp_on
            put(10000004, "minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=true]");
            // purple_lamp_on
            put(10000003, "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=false]");
            // red_lamp_on
            put(10000002, "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=true]");
            // blue_lamp
            put(1, "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=false]");
            // green_lamp
            put(4, "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=true]");
            // purple_lamp
            put(3, "minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=false]");
            // red_lamp
            put(2, "minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=true]");
            // copper seed block
            put(42, "minecraft:mushroom_stem[down=false,east=false,north=false,south=true,up=false,west=false]");
            // delta seed block
            put(43, "minecraft:mushroom_stem[down=false,east=false,north=false,south=false,up=true,west=true]");
            // rotor seed block
            put(44, "minecraft:mushroom_stem[down=false,east=false,north=true,south=true,up=false,west=true]");
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
            // advanced_console
            put(50, "minecraft:mushroom_stem[down=true,east=true,north=false,south=false,up=true,west=true]");
            // disk_storage
            put(51, "minecraft:mushroom_stem[down=true,east=true,north=false,south=true,up=false,west=false]");
            // lamp_off
            put(52, "minecraft:mushroom_stem[down=true,east=true,north=false,south=true,up=false,west=true]");
            // lantern_off
            put(53, "minecraft:mushroom_stem[down=true,east=true,north=false,south=true,up=true,west=false]");
            // blue_box
            put(54, "minecraft:mushroom_stem[down=true,east=true,north=false,south=true,up=true,west=true]");
            // animated seed grow
            put(55, "minecraft:mushroom_stem[down=false,east=false,north=false,south=false,up=true,west=false]");
            // cave seed block
            put(56, "minecraft:mushroom_stem[down=false,east=false,north=false,south=true,up=false,west=true]");
            // weathered seed block
            put(57, "minecraft:mushroom_stem[down=false,east=false,north=false,south=true,up=true,west=false]");
            // original seed block
            put(58, "minecraft:mushroom_stem[down=false,east=false,north=false,south=true,up=true,west=true]");
        }
    };

    static TARDISDisplayItem getTARDISBlock(BlockData data) {
        String s = data.getAsString();
        switch (data.getMaterial()) {
            case MUSHROOM_STEM -> {
                for (HashMap.Entry<Integer, String> stem : MUSHROOM_STEM_DATA.entrySet()) {
                    if (s.equals(stem.getValue())) {
                        return TARDISDisplayItem.getBY_MUSHROOM_STEM().get(stem.getKey());
                    }
                }
            }
            case BROWN_MUSHROOM_BLOCK -> {
                if (s.equals("minecraft:brown_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=true]")) {
                    return TARDISDisplayItem.THE_MOMENT;
                }
                if (s.equals("minecraft:brown_mushroom_block[down=false,east=false,north=false,south=true,up=false,west=false]")) {
                    return TARDISDisplayItem.SIEGE_CUBE;
                }
            }
            default -> {
                // RED_MUSHROOM_BLOCK
                for (HashMap.Entry<Integer, String> red : RED_MUSHROOM_DATA.entrySet()) {
                    if (s.equals(red.getValue())) {
                        return TARDISDisplayItem.getBY_RED_MUSHROOM().get(red.getKey());
                    }
                }
            }

        }
        return null;
    }
}
