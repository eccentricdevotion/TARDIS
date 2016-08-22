/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 *
 * @author eccentric_nz
 */
public class Smelter {

    private static final List<Vector> fuelVectors = new ArrayList<Vector>();
    private static final List<Vector> oreVectors = new ArrayList<Vector>();

    static {
        fuelVectors.add(new Vector(-6.0, 2.0, 4.0));
        fuelVectors.add(new Vector(-6.0, 2.0, 3.0));
        fuelVectors.add(new Vector(-6.0, 2.0, -3.0));
        fuelVectors.add(new Vector(-6.0, 2.0, -4.0));
        fuelVectors.add(new Vector(-4.0, 2.0, 6.0));
        fuelVectors.add(new Vector(-4.0, 2.0, -6.0));
        fuelVectors.add(new Vector(-3.0, 2.0, 6.0));
        fuelVectors.add(new Vector(-3.0, 2.0, -6.0));
        fuelVectors.add(new Vector(3.0, 2.0, 6.0));
        fuelVectors.add(new Vector(3.0, 2.0, -6.0));
        fuelVectors.add(new Vector(4.0, 2.0, 6.0));
        fuelVectors.add(new Vector(4.0, 2.0, -6.0));
        fuelVectors.add(new Vector(6.0, 2.0, 4.0));
        fuelVectors.add(new Vector(6.0, 2.0, 3.0));
        fuelVectors.add(new Vector(6.0, 2.0, -3.0));
        fuelVectors.add(new Vector(6.0, 2.0, -4.0));
        oreVectors.add(new Vector(-5.0, 3.0, 4.0));
        oreVectors.add(new Vector(-5.0, 3.0, 3.0));
        oreVectors.add(new Vector(-5.0, 3.0, -3.0));
        oreVectors.add(new Vector(-5.0, 3.0, -4.0));
        oreVectors.add(new Vector(-4.0, 3.0, 5.0));
        oreVectors.add(new Vector(-4.0, 3.0, -5.0));
        oreVectors.add(new Vector(-3.0, 3.0, 5.0));
        oreVectors.add(new Vector(-3.0, 3.0, -5.0));
        oreVectors.add(new Vector(3.0, 3.0, 5.0));
        oreVectors.add(new Vector(3.0, 3.0, -5.0));
        oreVectors.add(new Vector(4.0, 3.0, 5.0));
        oreVectors.add(new Vector(4.0, 3.0, -5.0));
        oreVectors.add(new Vector(5.0, 3.0, 4.0));
        oreVectors.add(new Vector(5.0, 3.0, 3.0));
        oreVectors.add(new Vector(5.0, 3.0, -3.0));
        oreVectors.add(new Vector(5.0, 3.0, -4.0));
    }

    public static boolean isSmeltable(Material material) {
        switch (material) {
            case CACTUS:
            case CHORUS_FRUIT:
            case CLAY:
            case CLAY_BALL:
            case COBBLESTONE:
            case GOLD_ORE:
            case IRON_ORE:
//            case LOG:
//            case LOG_2:
            case MUTTON:
            case NETHERRACK:
            case PORK:
            case POTATO_ITEM:
            case RABBIT:
            case RAW_BEEF:
            case RAW_CHICKEN:
            case RAW_FISH:
            case SAND:
            case SMOOTH_BRICK:
            case SPONGE:
                return true;
            default:
                return false;
        }
    }

    public static boolean isFuel(Material material) {
        switch (material) {
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_STAIRS:
            case BANNER:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_WOOD_STAIRS:
            case BLAZE_ROD:
            case BOOKSHELF:
            case CHEST:
            case COAL:
            case COAL_ORE:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_STAIRS:
            case DAYLIGHT_DETECTOR:
            case FENCE:
            case FENCE_GATE:
            case HUGE_MUSHROOM_1:
            case HUGE_MUSHROOM_2:
            case JUKEBOX:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_WOOD_STAIRS:
            case LAVA_BUCKET:
            case LOG:
            case LOG_2:
            case NOTE_BLOCK:
            case SAPLING:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_WOOD_STAIRS:
            case STICK:
            case TRAPPED_CHEST:
            case TRAP_DOOR:
            case WOOD:
            case WOOD_AXE:
            case WOOD_DOOR:
            case WOOD_HOE:
            case WOOD_PICKAXE:
            case WOOD_PLATE:
            case WOOD_SPADE:
            case WOOD_STAIRS:
            case WOOD_STEP:
            case WOOD_SWORD:
            case WORKBENCH:
                // available in Minecraft 1.11
//            case ACACIA_DOOR_ITEM:
//            case BIRCH_DOOR_ITEM:
//            case BOAT:
//            case BOAT_ACACIA:
//            case BOAT_BIRCH:
//            case BOAT_DARK_OAK:
//            case BOAT_JUNGLE:
//            case BOAT_SPRUCE:
//            case BOW:
//            case BOWL:
//            case CARPET:
//            case DARK_OAK_DOOR_ITEM:
//            case FISHING_ROD:
//            case JUNGLE_DOOR_ITEM:
//            case LADDER:
//            case SIGN:
//            case SPRUCE_DOOR_ITEM:
//            case WOOD_BUTTON:
//            case WOOL:
                return true;
            default:
                return false;
        }
    }

    public static List<Vector> getFuelVectors() {
        return fuelVectors;
    }

    public static List<Vector> getOreVectors() {
        return oreVectors;
    }
}
