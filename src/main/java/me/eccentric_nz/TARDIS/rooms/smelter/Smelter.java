/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.rooms.smelter;

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

    static boolean isSmeltable(Material material) {
        return switch (material) {
            case ACACIA_LOG, ACACIA_WOOD,
                    BASALT, BEEF, BIRCH_LOG, BIRCH_WOOD, BLACK_TERRACOTTA, BLUE_TERRACOTTA, BROWN_TERRACOTTA,
                    CACTUS, CHICKEN, CHORUS_FRUIT, CLAY, CLAY_BALL, COBBLED_DEEPSLATE, COBBLESTONE, COD, COPPER_ORE, CYAN_TERRACOTTA,
                    DARK_OAK_LOG, DARK_OAK_WOOD, DEEPSLATE_COPPER_ORE, DEEPSLATE_GOLD_ORE, DEEPSLATE_IRON_ORE,
                    GOLD_ORE, GRAY_TERRACOTTA, GREEN_TERRACOTTA,
                    IRON_ORE,
                    JUNGLE_LOG, JUNGLE_WOOD,
                    KELP,
                    LIGHT_BLUE_TERRACOTTA, LIGHT_GRAY_TERRACOTTA, LIME_TERRACOTTA,
                    MAGENTA_TERRACOTTA, MUTTON,
                    NETHERRACK,
                    OAK_LOG, OAK_WOOD, ORANGE_TERRACOTTA,
                    PINK_TERRACOTTA, PORKCHOP, POTATO, PURPLE_TERRACOTTA,
                    QUARTZ_BLOCK,
                    RABBIT, RAW_COPPER, RAW_GOLD, RAW_IRON, RED_SANDSTONE, RED_TERRACOTTA,
                    SALMON, SAND, SANDSTONE, SEA_PICKLE, SPONGE, SPRUCE_LOG, SPRUCE_WOOD, STONE, STONE_BRICKS,
                    STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD, STRIPPED_DARK_OAK_LOG,
                    STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD,
                    STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD,
                    WET_SPONGE, WHITE_TERRACOTTA,
                    YELLOW_TERRACOTTA -> true;
            default -> false;
        };
    }

    public static List<Vector> getFuelVectors() {
        return FUEL_VECTORS;
    }

    public static List<Vector> getOreVectors() {
        return ORE_VECTORS;
    }
}
