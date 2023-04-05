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
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

/**
 *
 * @author eccentric_nz
 */
public enum TARDISDisplayItem {

    ANCIENT(Material.SCULK),
    ARS(Material.QUARTZ_BLOCK),
    BIGGER(Material.GOLD_BLOCK),
    BUDGET(Material.IRON_BLOCK),
    CAVE(Material.DRIPSTONE_BLOCK),
    COPPER(10001, Material.COPPER_BLOCK),
    CORAL(10001, Material.FIRE_CORAL_BLOCK),
    DELTA(Material.CRYING_OBSIDIAN),
    DELUXE(Material.DIAMOND_BLOCK),
    DIVISION(Material.PINK_GLAZED_TERRACOTTA),
    ELEVENTH(Material.EMERALD_BLOCK),
    ENDER(Material.PURPUR_BLOCK),
    FACTORY(10001, Material.GRAY_CONCRETE),
    FUGITIVE(Material.POLISHED_DEEPSLATE),
    MASTER(Material.NETHER_BRICKS),
    MECHANICAL(Material.POLISHED_ANDESITE),
    ORIGINAL(Material.PACKED_MUD),
    PLANK(Material.OAK_PLANKS),
    PYRAMID(Material.SANDSTONE),
    REDSTONE(Material.REDSTONE_BLOCK),
    ROTOR(Material.HONEYCOMB_BLOCK),
    STEAMPUNK(Material.COAL_BLOCK),
    THIRTEENTH(10001, Material.HORN_CORAL_BLOCK),
    TOM(Material.LAPIS_BLOCK),
    TWELFTH(Material.PRISMARINE),
    WAR(Material.WHITE_TERRACOTTA),
    WEATHERED(Material.WEATHERED_COPPER),
    SMALL(10001, Material.COBBLESTONE),
    MEDIUM(10002, Material.COBBLESTONE),
    TALL(10003, Material.COBBLESTONE),
    LEGACY_BIGGER(Material.ORANGE_GLAZED_TERRACOTTA),
    LEGACY_BUDGET(Material.LIGHT_GRAY_GLAZED_TERRACOTTA),
    LEGACY_DELUXE(Material.LIME_GLAZED_TERRACOTTA),
    LEGACY_ELEVENTH(Material.CYAN_GLAZED_TERRACOTTA),
    LEGACY_REDSTONE(Material.RED_GLAZED_TERRACOTTA),
    CUSTOM(10001, Material.POLISHED_BLACKSTONE),
    GROW(10001, Material.LIGHT_GRAY_TERRACOTTA),
    ADVANCED_CONSOLE(10001, Material.JUKEBOX),
    BLUE_BOX(10001, Material.BLUE_WOOL),
    COG(10001, Material.GRAY_WOOL),
    DISK_STORAGE(10001, Material.NOTE_BLOCK),
    HEXAGON(10001, Material.ORANGE_WOOL),
    ROUNDEL(10001, Material.WHITE_WOOL),
    ROUNDEL_OFFSET(10002, Material.WHITE_WOOL),
    BLUE_LAMP(10001, Material.REDSTONE_LAMP, true),
    GREEN_LAMP(10002, Material.REDSTONE_LAMP, true),
    PURPLE_LAMP(10003, Material.REDSTONE_LAMP, true),
    RED_LAMP(10004, Material.REDSTONE_LAMP, true),
    BLUE_LAMP_ON(10001, Material.SEA_LANTERN, true, true),
    GREEN_LAMP_ON(10002, Material.SEA_LANTERN, true, true),
    PURPLE_LAMP_ON(10003, Material.SEA_LANTERN, true, true),
    RED_LAMP_ON(10004, Material.SEA_LANTERN, true, true),
    PANDORICA(10001, Material.BLACK_CONCRETE),
    COMPOUND(10001, Material.ORANGE_CONCRETE),
    CONSTRUCTOR(10001, Material.LIGHT_BLUE_CONCRETE),
    CREATIVE(10001, Material.LIGHT_GRAY_CONCRETE),
    HEAT_BLOCK(10001, Material.RED_CONCRETE),
    LAB(10001, Material.YELLOW_CONCRETE),
    PRODUCT(10001, Material.LIME_CONCRETE),
    REDUCER(10001, Material.MAGENTA_CONCRETE),
    SIEGE_CUBE(10001, Material.CYAN_CONCRETE),
    THE_MOMENT(10001, Material.BROWN_WOOL),
    LIGHT_CLASSIC(10005, Material.SEA_LANTERN, true),
    LIGHT_TENTH(10006, Material.SEA_LANTERN, true),
    LIGHT_ELEVENTH_1(10007, Material.SEA_LANTERN, true),
    LIGHT_ELEVENTH_2(10008, Material.SEA_LANTERN, true),
    LIGHT_TWELFTH(10009, Material.SEA_LANTERN, true),
    LIGHT_THIRTEENTH(10010, Material.SEA_LANTERN, true),
    LIGHT_LAMP(-1, Material.REDSTONE_LAMP, true),
    LIGHT_LANTERN(10011, Material.SEA_LANTERN, true),
    LIGHT_WOOL(-1, Material.BLACK_WOOL, true),
    LIGHT_CLASSIC_ON(10005, Material.SEA_LANTERN, true, true),
    LIGHT_TENTH_ON(10006, Material.SEA_LANTERN, true, true),
    LIGHT_ELEVENTH_1_ON(10007, Material.SEA_LANTERN, true, true),
    LIGHT_ELEVENTH_2_ON(10008, Material.SEA_LANTERN, true, true),
    LIGHT_TWELFTH_ON(10009, Material.SEA_LANTERN, true, true),
    LIGHT_THIRTEENTH_ON(10010, Material.SEA_LANTERN, true, true),
    LIGHT_LAMP_ON(10005, Material.REDSTONE_LAMP, true, true),
    LIGHT_LANTERN_ON(-1, Material.SEA_LANTERN, true, true);

    private int customModelData;
    private final Material material;
    private final boolean light;
    private final boolean lit;

    private TARDISDisplayItem(Material item) {
        this.customModelData = 1;
        this.material = item;
        this.light = false;
        this.lit = false;
    }

    private TARDISDisplayItem(int customModelData, Material item) {
        this.customModelData = customModelData;
        this.material = item;
        this.light = false;
        this.lit = false;
    }

    private TARDISDisplayItem(int customModelData, Material item, boolean light) {
        this.customModelData = customModelData;
        this.material = item;
        this.light = light;
        this.lit = false;
    }

    private TARDISDisplayItem(int customModelData, Material item, boolean light, boolean lit) {
        this.customModelData = customModelData;
        this.material = item;
        this.light = light;
        this.lit = lit;
    }

    private static final HashMap<Integer, TARDISDisplayItem> BY_DATA = new HashMap<>();
    private static final HashMap<String, TARDISDisplayItem> BY_NAME = new HashMap<>();

    static {
        for (TARDISDisplayItem tdi : values()) {
            BY_NAME.put(tdi.toString().toLowerCase(), tdi);
            BY_DATA.put(tdi.customModelData, tdi);
        }
    }

    public String getName() {
        return this.toString().toLowerCase();
    }

    public String getDisplayName() {
        return TARDISStringUtils.capitalise(this.getName());
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public Material getMaterial() {
        return material;
    }

    private static final HashMap<Integer, TARDISDisplayItem> BY_MUSHROOM_STEM = new HashMap<>() {
        {
            put(1, BLUE_LAMP);
            put(10000001, BLUE_LAMP_ON);
            put(10000002, RED_LAMP_ON);
            put(10000003, PURPLE_LAMP_ON);
            put(10000004, GREEN_LAMP_ON);
            put(2, RED_LAMP);
            put(3, PURPLE_LAMP);
            put(4, GREEN_LAMP);
            put(46, HEXAGON);
            put(47, ROUNDEL);
            put(48, ROUNDEL_OFFSET);
            put(49, COG);
            put(5, HEAT_BLOCK);
            put(50, ADVANCED_CONSOLE);
            put(51, DISK_STORAGE);
            put(54, BLUE_BOX);
        }
    };

    private static final HashMap<Integer, TARDISDisplayItem> BY_RED_MUSHROOM = new HashMap<>() {
        {
            put(40, CREATIVE);
            put(41, COMPOUND);
            put(42, REDUCER);
            put(43, CONSTRUCTOR);
            put(44, LAB);
            put(45, PRODUCT);
        }
    };

    public boolean isLight() {
        return light;
    }

    public boolean isLit() {
        return lit;
    }

    public static TARDISDisplayItem getByMaterialAndData(Material m, int cmd) {
        for (TARDISDisplayItem tdi : values()) {
            if (tdi.getMaterial() == m && tdi.getCustomModelData() == cmd) {
                return tdi;
            }
        }
        return null;
    }

    public static HashMap<String, TARDISDisplayItem> getBY_NAME() {
        return BY_NAME;
    }

    public static HashMap<Integer, TARDISDisplayItem> getBY_MUSHROOM_STEM() {
        return BY_MUSHROOM_STEM;
    }

    public static HashMap<Integer, TARDISDisplayItem> getBY_RED_MUSHROOM() {
        return BY_RED_MUSHROOM;
    }
}
