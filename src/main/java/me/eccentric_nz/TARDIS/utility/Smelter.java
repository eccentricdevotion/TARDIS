/*
 * Copyright (C) 2019 eccentric_nz
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
            case ACACIA_WOOD:
            case ACACIA_LOG:
            case BEEF:
            case BIRCH_WOOD:
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
            case DARK_OAK_WOOD:
            case DARK_OAK_LOG:
            case GOLD_ORE:
            case GRAY_TERRACOTTA:
            case GREEN_TERRACOTTA:
            case IRON_ORE:
            case JUNGLE_WOOD:
            case JUNGLE_LOG:
            case KELP:
            case LIGHT_BLUE_TERRACOTTA:
            case LIGHT_GRAY_TERRACOTTA:
            case LIME_TERRACOTTA:
            case MAGENTA_TERRACOTTA:
            case MUTTON:
            case NETHERRACK:
            case OAK_WOOD:
            case OAK_LOG:
            case ORANGE_TERRACOTTA:
            case PINK_TERRACOTTA:
            case PORKCHOP:
            case POTATO:
            case PURPLE_TERRACOTTA:
            case QUARTZ_BLOCK:
            case RABBIT:
            case RED_SANDSTONE:
            case RED_TERRACOTTA:
            case SALMON:
            case SAND:
            case SANDSTONE:
            case SEA_PICKLE:
            case SPONGE:
            case SPRUCE_WOOD:
            case SPRUCE_LOG:
            case STONE:
            case STONE_BRICKS:
            case STRIPPED_ACACIA_LOG:
            case STRIPPED_ACACIA_WOOD:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_BIRCH_WOOD:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_DARK_OAK_WOOD:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_JUNGLE_WOOD:
            case STRIPPED_OAK_LOG:
            case STRIPPED_OAK_WOOD:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_SPRUCE_WOOD:
            case WET_SPONGE:
            case WHITE_TERRACOTTA:
            case YELLOW_TERRACOTTA:
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
