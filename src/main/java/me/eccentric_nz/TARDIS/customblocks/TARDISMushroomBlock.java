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
package me.eccentric_nz.TARDIS.customblocks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.MultipleFacing;

import java.util.HashMap;
import java.util.List;

public class TARDISMushroomBlock {

    /**
     * Gets the toggled state from the current state's blockdata
     */
    public static final HashMap<String, TARDISDisplayItem> conversionMap = new HashMap<>() {
        {
            put("minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=false]", TARDISDisplayItem.BLUE_LAMP);
            put("minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=true]", TARDISDisplayItem.GREEN_LAMP);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=false]", TARDISDisplayItem.PURPLE_LAMP);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=true]", TARDISDisplayItem.RED_LAMP);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=false]", TARDISDisplayItem.BLUE_LAMP_ON);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=true]", TARDISDisplayItem.GREEN_LAMP_ON);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=false]", TARDISDisplayItem.PURPLE_LAMP_ON);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=true]", TARDISDisplayItem.RED_LAMP_ON);
            put("minecraft:mushroom_stem[down=false,east=false,north=false,south=false,up=false,west=true]", TARDISDisplayItem.HEAT_BLOCK);
        }
    };
    private static final List<String> vanillaBrown = List.of(
            "minecraft:brown_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=false]",
            "minecraft:brown_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=false]",
            "minecraft:brown_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=true]",
            "minecraft:brown_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=false]",
            "minecraft:brown_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=true]",
            "minecraft:brown_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=false]",
            "minecraft:brown_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=true]",
            "minecraft:brown_mushroom_block[down=false,east=true,north=false,south=false,up=true,west=false]",
            "minecraft:brown_mushroom_block[down=false,east=true,north=false,south=true,up=true,west=false]",
            "minecraft:brown_mushroom_block[down=false,east=true,north=true,south=false,up=true,west=false]",
            "minecraft:brown_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=true]"
    );
    private static final List<String> vanillaRed = List.of(
            "minecraft:red_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=false]",
            "minecraft:red_mushroom_block[down=false,east=false,north=false,south=false,up=false,west=true]",
            "minecraft:red_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=true]",
            "minecraft:red_mushroom_block[down=false,east=false,north=false,south=true,up=false,west=false]",
            "minecraft:red_mushroom_block[down=false,east=false,north=false,south=true,up=false,west=true]",
            "minecraft:red_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=false]",
            "minecraft:red_mushroom_block[down=false,east=false,north=false,south=true,up=true,west=true]",
            "minecraft:red_mushroom_block[down=false,east=false,north=true,south=false,up=false,west=false]",
            "minecraft:red_mushroom_block[down=false,east=false,north=true,south=false,up=false,west=true]",
            "minecraft:red_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=false]",
            "minecraft:red_mushroom_block[down=false,east=false,north=true,south=false,up=true,west=true]",
            "minecraft:red_mushroom_block[down=false,east=true,north=false,south=false,up=false,west=false]",
            "minecraft:red_mushroom_block[down=false,east=true,north=false,south=false,up=true,west=false]",
            "minecraft:red_mushroom_block[down=false,east=true,north=false,south=true,up=false,west=false]",
            "minecraft:red_mushroom_block[down=false,east=true,north=false,south=true,up=true,west=false]",
            "minecraft:red_mushroom_block[down=false,east=true,north=true,south=false,up=false,west=false]",
            "minecraft:red_mushroom_block[down=false,east=true,north=true,south=false,up=true,west=false]",
            "minecraft:red_mushroom_block[down=true,east=true,north=true,south=true,up=true,west=true]",
            "minecraft:red_mushroom_block[down=false,east=false,north=false,south=false,up=true,west=false]"
    );
    private static final List<String> vanillaStem = List.of(
            "minecraft:mushroom_stem[down=false,east=false,north=false,south=false,up=false,west=false]",
            "minecraft:mushroom_stem[down=false,east=true,north=true,south=true,up=false,west=true]",
            "minecraft:mushroom_stem[down=true,east=true,north=true,south=true,up=true,west=true]"
    );
    private static final HashMap<String, String> chemistryStemOn = new HashMap<>() {
        {
            // blue_lamp_on
            put("minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=false]", "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=false]");
            // red_lamp_on
            put("minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=true]", "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=true]");
            // purple_lamp_on
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=false]", "minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=false]");
            // green_lamp_on
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=true]", "minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=true]");
        }
    };
    private static final HashMap<String, String> chemistryStemOff = new HashMap<>() {
        {
            // blue_lamp
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=false]", "minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=false]");
            // red_lamp
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=true]", "minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=true]");
            // purple_lamp
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=false]", "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=false]");
            // green_lamp
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=true]", "minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=true]");
        }
    };

    public static boolean isVanillaBrownMushroomState(MultipleFacing multipleFacing) {
        return (vanillaBrown.contains(multipleFacing.getAsString()));
    }

    public static boolean isVanillaRedMushroomState(MultipleFacing multipleFacing) {
        return (vanillaRed.contains(multipleFacing.getAsString()));
    }

    public static boolean isVanillaMushroomStemState(MultipleFacing multipleFacing) {
        return (vanillaStem.contains(multipleFacing.getAsString()));
    }

    public static MultipleFacing getChemistryStemOff(MultipleFacing multipleFacing) {
        return (MultipleFacing) Bukkit.createBlockData(chemistryStemOn.get(multipleFacing.getAsString()));
    }

    public static boolean isChemistryStemOn(MultipleFacing multipleFacing) {
        return (chemistryStemOn.containsKey(multipleFacing.getAsString()));
    }

    public static MultipleFacing getChemistryStemOn(MultipleFacing multipleFacing) {
        return (MultipleFacing) Bukkit.createBlockData(chemistryStemOff.get(multipleFacing.getAsString()));
    }

    public static boolean isChemistryStemOff(MultipleFacing multipleFacing) {
        return (chemistryStemOff.containsKey(multipleFacing.getAsString()));
    }

    public static boolean isTardisMushroom(Block block) {
        Material material = block.getType();
        return (material == Material.BROWN_MUSHROOM_BLOCK || material == Material.RED_MUSHROOM_BLOCK || material == Material.MUSHROOM_STEM);
    }
}
