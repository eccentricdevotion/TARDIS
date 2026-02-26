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
package me.eccentric_nz.TARDIS.customblocks;

import org.bukkit.block.data.MultipleFacing;

import java.util.HashMap;

public class TARDISMushroomBlock {

    /**
     * Gets the toggled state from the current state's blockdata
     */
    public static final HashMap<String, TARDISDisplayItem> conversionMap = new HashMap<>() {
        {
            put("minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=false]", TARDISChemistryDisplayItem.BLUE_LAMP);
            put("minecraft:mushroom_stem[down=true,east=false,north=false,south=true,up=true,west=true]", TARDISChemistryDisplayItem.GREEN_LAMP);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=false]", TARDISChemistryDisplayItem.PURPLE_LAMP);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=false,west=true]", TARDISChemistryDisplayItem.RED_LAMP);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=false]", TARDISChemistryDisplayItem.BLUE_LAMP_ON);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=false,up=true,west=true]", TARDISChemistryDisplayItem.GREEN_LAMP_ON);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=false]", TARDISChemistryDisplayItem.PURPLE_LAMP_ON);
            put("minecraft:mushroom_stem[down=true,east=false,north=true,south=true,up=false,west=true]", TARDISChemistryDisplayItem.RED_LAMP_ON);
            put("minecraft:mushroom_stem[down=false,east=false,north=false,south=false,up=false,west=true]", TARDISChemistryDisplayItem.HEAT_BLOCK);
        }
    };
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

    public static boolean isChemistryStemOn(MultipleFacing multipleFacing) {
        return (chemistryStemOn.containsKey(multipleFacing.getAsString()));
    }

    public static boolean isChemistryStemOff(MultipleFacing multipleFacing) {
        return (chemistryStemOff.containsKey(multipleFacing.getAsString()));
    }
}
