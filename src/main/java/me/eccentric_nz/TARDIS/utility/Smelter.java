/*
 * Copyright (C) 2018 eccentric_nz
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

import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class Smelter {

    private static final List<Vector> FUEL_VECTORS = new ArrayList<>();
    private static final List<Vector> ORE_VECTORS = new ArrayList<>();

    static {
        FUEL_VECTORS.add(new Vector(-6.0, 3.0, 4.0));
        FUEL_VECTORS.add(new Vector(-6.0, 3.0, 3.0));
        FUEL_VECTORS.add(new Vector(-6.0, 3.0, -3.0));
        FUEL_VECTORS.add(new Vector(-6.0, 3.0, -4.0));
        FUEL_VECTORS.add(new Vector(-4.0, 3.0, 6.0));
        FUEL_VECTORS.add(new Vector(-4.0, 3.0, -6.0));
        FUEL_VECTORS.add(new Vector(-3.0, 3.0, 6.0));
        FUEL_VECTORS.add(new Vector(-3.0, 3.0, -6.0));
        FUEL_VECTORS.add(new Vector(3.0, 3.0, 6.0));
        FUEL_VECTORS.add(new Vector(3.0, 3.0, -6.0));
        FUEL_VECTORS.add(new Vector(4.0, 3.0, 6.0));
        FUEL_VECTORS.add(new Vector(4.0, 3.0, -6.0));
        FUEL_VECTORS.add(new Vector(6.0, 3.0, 4.0));
        FUEL_VECTORS.add(new Vector(6.0, 3.0, 3.0));
        FUEL_VECTORS.add(new Vector(6.0, 3.0, -3.0));
        FUEL_VECTORS.add(new Vector(6.0, 3.0, -4.0));
        ORE_VECTORS.add(new Vector(-5.0, 4.0, 4.0));
        ORE_VECTORS.add(new Vector(-5.0, 4.0, 3.0));
        ORE_VECTORS.add(new Vector(-5.0, 4.0, -3.0));
        ORE_VECTORS.add(new Vector(-5.0, 4.0, -4.0));
        ORE_VECTORS.add(new Vector(-4.0, 4.0, 5.0));
        ORE_VECTORS.add(new Vector(-4.0, 4.0, -5.0));
        ORE_VECTORS.add(new Vector(-3.0, 4.0, 5.0));
        ORE_VECTORS.add(new Vector(-3.0, 4.0, -5.0));
        ORE_VECTORS.add(new Vector(3.0, 4.0, 5.0));
        ORE_VECTORS.add(new Vector(3.0, 4.0, -5.0));
        ORE_VECTORS.add(new Vector(4.0, 4.0, 5.0));
        ORE_VECTORS.add(new Vector(4.0, 4.0, -5.0));
        ORE_VECTORS.add(new Vector(5.0, 4.0, 4.0));
        ORE_VECTORS.add(new Vector(5.0, 4.0, 3.0));
        ORE_VECTORS.add(new Vector(5.0, 4.0, -3.0));
        ORE_VECTORS.add(new Vector(5.0, 4.0, -4.0));
    }

    public static boolean isSmeltable(Material material) {
        switch (material) {
            case ACACIA_BARK:
            case ACACIA_LOG:
            case BEEF:
            case BIRCH_BARK:
            case BIRCH_LOG:
            case BLACK_TERRACOTTA:
            case BLUE_TERRACOTTA:
            case BROWN_TERRACOTTA:
            case CACTUS:
            case CHICKEN:
            case CHORUS_FRUIT:
            case CLAY:
            case CLAY_BALL:
            case COBBLESTONE:
            case COD:
            case CYAN_TERRACOTTA:
            case DARK_OAK_BARK:
            case DARK_OAK_LOG:
            case GOLD_ORE:
            case GRAY_TERRACOTTA:
            case GREEN_TERRACOTTA:
            case IRON_ORE:
            case JUNGLE_BARK:
            case JUNGLE_LOG:
            case LIGHT_BLUE_TERRACOTTA:
            case LIGHT_GRAY_TERRACOTTA:
            case LIME_TERRACOTTA:
            case MAGENTA_TERRACOTTA:
            case MUTTON:
            case NETHERRACK:
            case OAK_BARK:
            case OAK_LOG:
            case ORANGE_TERRACOTTA:
            case PINK_TERRACOTTA:
            case PORKCHOP:
            case POTATO:
            case PURPLE_TERRACOTTA:
            case RABBIT:
            case RED_TERRACOTTA:
            case SALMON:
            case SAND:
            case SPONGE:
            case SPRUCE_BARK:
            case SPRUCE_LOG:
            case STONE_BRICKS:
            case WET_SPONGE:
            case WHITE_TERRACOTTA:
            case YELLOW_TERRACOTTA:
                return true;
            default:
                return false;
        }
    }

    public static boolean isFuel(Material material) {
        switch (material) {
            case ACACIA_BARK:
            case ACACIA_BOAT:
            case ACACIA_BUTTON:
            case ACACIA_DOOR:
            case ACACIA_FENCE:
            case ACACIA_FENCE_GATE:
            case ACACIA_PLANKS:
            case ACACIA_PRESSURE_PLATE:
            case ACACIA_SAPLING:
            case ACACIA_SLAB:
            case ACACIA_STAIRS:
            case ACACIA_TRAPDOOR:
            case BIRCH_BARK:
            case BIRCH_BOAT:
            case BIRCH_BUTTON:
            case BIRCH_DOOR:
            case BIRCH_FENCE:
            case BIRCH_FENCE_GATE:
            case BIRCH_PLANKS:
            case BIRCH_PRESSURE_PLATE:
            case BIRCH_SAPLING:
            case BIRCH_SLAB:
            case BIRCH_STAIRS:
            case BIRCH_TRAPDOOR:
            case BLACK_BANNER:
            case BLACK_CARPET:
            case BLACK_WOOL:
            case BLAZE_ROD:
            case BLUE_BANNER:
            case BLUE_CARPET:
            case BLUE_WOOL:
            case BOOKSHELF:
            case BOW:
            case BOWL:
            case BROWN_BANNER:
            case BROWN_CARPET:
            case BROWN_MUSHROOM_BLOCK:
            case BROWN_WOOL:
            case CHARCOAL:
            case CHEST:
            case COAL:
            case COAL_BLOCK:
            case CRAFTING_TABLE:
            case CYAN_BANNER:
            case CYAN_CARPET:
            case CYAN_WOOL:
            case DARK_OAK_BARK:
            case DARK_OAK_BOAT:
            case DARK_OAK_BUTTON:
            case DARK_OAK_DOOR:
            case DARK_OAK_FENCE:
            case DARK_OAK_FENCE_GATE:
            case DARK_OAK_PLANKS:
            case DARK_OAK_PRESSURE_PLATE:
            case DARK_OAK_SAPLING:
            case DARK_OAK_SLAB:
            case DARK_OAK_STAIRS:
            case DARK_OAK_TRAPDOOR:
            case DAYLIGHT_DETECTOR:
            case FISHING_ROD:
            case GRAY_BANNER:
            case GRAY_CARPET:
            case GRAY_WOOL:
            case GREEN_BANNER:
            case GREEN_CARPET:
            case GREEN_WOOL:
            case JUKEBOX:
            case JUNGLE_BARK:
            case JUNGLE_BOAT:
            case JUNGLE_BUTTON:
            case JUNGLE_DOOR:
            case JUNGLE_FENCE:
            case JUNGLE_FENCE_GATE:
            case JUNGLE_PLANKS:
            case JUNGLE_PRESSURE_PLATE:
            case JUNGLE_SAPLING:
            case JUNGLE_SLAB:
            case JUNGLE_STAIRS:
            case JUNGLE_TRAPDOOR:
            case LADDER:
            case LAVA_BUCKET:
            case LIGHT_BLUE_BANNER:
            case LIGHT_BLUE_CARPET:
            case LIGHT_BLUE_WOOL:
            case LIGHT_GRAY_BANNER:
            case LIGHT_GRAY_CARPET:
            case LIGHT_GRAY_WOOL:
            case LIME_BANNER:
            case LIME_CARPET:
            case LIME_WOOL:
            case MAGENTA_BANNER:
            case MAGENTA_CARPET:
            case MAGENTA_WOOL:
            case MUSHROOM_STEM:
            case NOTE_BLOCK:
            case OAK_BARK:
            case OAK_BOAT:
            case OAK_BUTTON:
            case OAK_DOOR:
            case OAK_FENCE:
            case OAK_FENCE_GATE:
            case OAK_PLANKS:
            case OAK_PRESSURE_PLATE:
            case OAK_SAPLING:
            case OAK_SLAB:
            case OAK_STAIRS:
            case OAK_TRAPDOOR:
            case ORANGE_BANNER:
            case ORANGE_CARPET:
            case ORANGE_WOOL:
            case PINK_BANNER:
            case PINK_CARPET:
            case PINK_WOOL:
            case PURPLE_BANNER:
            case PURPLE_CARPET:
            case PURPLE_WOOL:
            case RED_BANNER:
            case RED_CARPET:
            case RED_MUSHROOM_BLOCK:
            case RED_WOOL:
            case SIGN:
            case SPRUCE_BARK:
            case SPRUCE_BOAT:
            case SPRUCE_BUTTON:
            case SPRUCE_DOOR:
            case SPRUCE_FENCE:
            case SPRUCE_FENCE_GATE:
            case SPRUCE_PLANKS:
            case SPRUCE_PRESSURE_PLATE:
            case SPRUCE_SAPLING:
            case SPRUCE_SLAB:
            case SPRUCE_STAIRS:
            case SPRUCE_TRAPDOOR:
            case STICK:
            case TRAPPED_CHEST:
            case WHITE_BANNER:
            case WHITE_CARPET:
            case WHITE_WOOL:
            case WOODEN_AXE:
            case WOODEN_HOE:
            case WOODEN_PICKAXE:
            case WOODEN_SHOVEL:
            case WOODEN_SWORD:
            case YELLOW_BANNER:
            case YELLOW_CARPET:
            case YELLOW_WOOL:
                return true;
            default:
                return false;
        }
    }

    public static List<Vector> getFuelVectors() {
        return FUEL_VECTORS;
    }

    public static List<Vector> getOreVectors() {
        return ORE_VECTORS;
    }
}
