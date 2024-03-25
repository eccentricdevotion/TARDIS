/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 *
 * @author eccentric_nz
 */
public enum TARDISDisplayItem {

    // seed blocks
    ANCIENT(Material.SCULK),
    ARS(Material.QUARTZ_BLOCK),
    BIGGER(Material.GOLD_BLOCK),
    BUDGET(Material.IRON_BLOCK),
    CAVE(Material.DRIPSTONE_BLOCK),
    COPPER(10001, Material.COPPER_BLOCK, null),
    CORAL(10001, Material.FIRE_CORAL_BLOCK, null),
    CURSED(Material.BLACK_CONCRETE),
    CUSTOM_DOOR(-1, Material.IRON_DOOR, null),
    DELTA(Material.CRYING_OBSIDIAN),
    DELUXE(Material.DIAMOND_BLOCK),
    DIVISION(Material.PINK_GLAZED_TERRACOTTA),
    ELEVENTH(Material.EMERALD_BLOCK),
    ENDER(Material.PURPUR_BLOCK),
    FACTORY(10001, Material.GRAY_CONCRETE, null),
    FIFTEENTH(Material.OCHRE_FROGLIGHT),
    FUGITIVE(Material.POLISHED_DEEPSLATE),
    HOSPITAL(Material.WHITE_CONCRETE),
    MASTER(Material.NETHER_BRICKS),
    MECHANICAL(Material.POLISHED_ANDESITE),
    ORIGINAL(Material.PACKED_MUD),
    PLANK(Material.BOOKSHELF),
    PYRAMID(Material.SANDSTONE_STAIRS),
    REDSTONE(Material.REDSTONE_BLOCK),
    ROTOR(Material.HONEYCOMB_BLOCK),
    STEAMPUNK(Material.COAL_BLOCK),
    THIRTEENTH(10001, Material.HORN_CORAL_BLOCK, null),
    TOM(Material.LAPIS_BLOCK),
    TWELFTH(Material.PRISMARINE),
    WAR(Material.WHITE_TERRACOTTA),
    WEATHERED(Material.WEATHERED_COPPER),
    SMALL(10001, Material.COBBLESTONE, null),
    MEDIUM(10002, Material.COBBLESTONE, null),
    TALL(10003, Material.COBBLESTONE, null),
    LEGACY_BIGGER(Material.ORANGE_GLAZED_TERRACOTTA),
    LEGACY_DELUXE(Material.LIME_GLAZED_TERRACOTTA),
    LEGACY_ELEVENTH(Material.CYAN_GLAZED_TERRACOTTA),
    LEGACY_REDSTONE(Material.RED_GLAZED_TERRACOTTA),
    CUSTOM(10001, Material.POLISHED_BLACKSTONE, null),
    // growing seed block
    GROW(10001, Material.LIGHT_GRAY_TERRACOTTA, Material.NETHERITE_BLOCK),
    // tardis blocks
    ADVANCED_CONSOLE(10001, Material.JUKEBOX, null),
    ARTRON_FURNACE(10000001, Material.FURNACE, null),
    ARTRON_FURNACE_LIT(10000002, Material.FURNACE, null),
    BLUE_BOX(10001, Material.BLUE_WOOL, Material.BLUE_DYE),
    COG(10001, Material.GRAY_WOOL, Material.GRAY_DYE),
    DISK_STORAGE(10001, Material.NOTE_BLOCK, null),
//    HANDBRAKE(1001, Material.LEVER, Material.LEVER),
    HEXAGON(10001, Material.ORANGE_WOOL, Material.ORANGE_DYE),
    ROUNDEL(10001, Material.WHITE_WOOL, Material.WHITE_DYE),
    ROUNDEL_OFFSET(10002, Material.WHITE_WOOL, Material.LIGHT_GRAY_DYE),
    PANDORICA(10001, Material.BLACK_CONCRETE, null),
    SIEGE_CUBE(10001, Material.CYAN_CONCRETE, null),
    THE_MOMENT(10001, Material.BROWN_WOOL, Material.REDSTONE_BLOCK),
    DOOR(10001, Material.IRON_DOOR, Material.IRON_DOOR),
    DOOR_OPEN(10002, Material.IRON_DOOR, null),
    DOOR_BOTH_OPEN(10003, Material.IRON_DOOR, null),
    CLASSIC_DOOR(10000, Material.IRON_DOOR, Material.CHERRY_DOOR),
    CLASSIC_DOOR_OPEN(10006, Material.CHERRY_DOOR, null),
    SONIC_GENERATOR(10000001, Material.FLOWER_POT, null),
//    THROTTLE(1001, Material.REPEATER, Material.REPEATER),
    // chemistry lamps off
    BLUE_LAMP(10001, Material.REDSTONE_LAMP, null),
    GREEN_LAMP(10002, Material.REDSTONE_LAMP, null),
    PURPLE_LAMP(10003, Material.REDSTONE_LAMP, null),
    RED_LAMP(10004, Material.REDSTONE_LAMP, null),
    // chemistry lamps on
    BLUE_LAMP_ON(10001, Material.SEA_LANTERN, null),
    GREEN_LAMP_ON(10002, Material.SEA_LANTERN, null),
    PURPLE_LAMP_ON(10003, Material.SEA_LANTERN, null),
    RED_LAMP_ON(10004, Material.SEA_LANTERN, null),
    // chemistry gui blocks
    COMPOUND(10001, Material.ORANGE_CONCRETE, null),
    CONSTRUCTOR(10001, Material.LIGHT_BLUE_CONCRETE, null),
    CREATIVE(10001, Material.LIGHT_GRAY_CONCRETE, null),
    LAB(10001, Material.YELLOW_CONCRETE, null),
    PRODUCT(10001, Material.LIME_CONCRETE, null),
    REDUCER(10001, Material.MAGENTA_CONCRETE, null),
    HEAT_BLOCK(10001, Material.RED_CONCRETE, null),
    // lights off
    LIGHT_CLASSIC(10005, Material.GRAY_WOOL, Material.TORCH),
    LIGHT_CLASSIC_OFFSET(10010, Material.GRAY_WOOL, Material.SOUL_TORCH),
    LIGHT_TENTH(10006, Material.GRAY_WOOL, Material.ORANGE_DYE),
    LIGHT_ELEVENTH(10007, Material.GRAY_WOOL, Material.YELLOW_DYE),
    LIGHT_TWELFTH(10008, Material.GRAY_WOOL, Material.CYAN_DYE),
    LIGHT_THIRTEENTH(10009, Material.GRAY_WOOL, Material.BLUE_DYE),
    LIGHT_LAMP(-1, Material.REDSTONE_LAMP, null),
    LIGHT_LANTERN(-1, Material.GRAY_WOOL, null),
    // lights on
    LIGHT_CLASSIC_ON(10005, Material.SEA_LANTERN, null),
    LIGHT_CLASSIC_OFFSET_ON(10010, Material.SEA_LANTERN, null),
    LIGHT_TENTH_ON(10006, Material.REDSTONE_LAMP, null),
    LIGHT_ELEVENTH_ON(10007, Material.REDSTONE_LAMP, null),
    LIGHT_TWELFTH_ON(10008, Material.SEA_LANTERN, null),
    LIGHT_THIRTEENTH_ON(10009, Material.SEA_LANTERN, null),
    LIGHT_LAMP_ON(10005, Material.REDSTONE_LAMP, null),
    LIGHT_LANTERN_ON(-1, Material.SEA_LANTERN, null),
    // lights cloister
    // lights on
    LIGHT_CLASSIC_CLOISTER(20005, Material.SEA_LANTERN, null),
    LIGHT_CLASSIC_OFFSET_CLOISTER(20010, Material.SEA_LANTERN, null),
    LIGHT_TENTH_CLOISTER(20006, Material.REDSTONE_LAMP, null),
    LIGHT_ELEVENTH_CLOISTER(20007, Material.REDSTONE_LAMP, null),
    LIGHT_TWELFTH_CLOISTER(20008, Material.SEA_LANTERN, null),
    LIGHT_THIRTEENTH_CLOISTER(20009, Material.SEA_LANTERN, null),
    LIGHT_LAMP_CLOISTER(-1, Material.REDSTONE_LAMP, null),
    LIGHT_LANTERN_CLOISTER(-1, Material.SEA_LANTERN, null),
    // console sides
    CONSOLE_1(1001, Material.AMETHYST_SHARD, null),
    CONSOLE_2(1002, Material.AMETHYST_SHARD, null),
    CONSOLE_3(1003, Material.AMETHYST_SHARD, null),
    CONSOLE_4(1004, Material.AMETHYST_SHARD, null),
    CONSOLE_5(1005, Material.AMETHYST_SHARD, null),
    CONSOLE_6(1006, Material.AMETHYST_SHARD, null),
    // dummy
    NONE(-1, null, null);

    private static final HashMap<String, TARDISDisplayItem> BY_NAME = new HashMap<>();
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

    static {
        for (TARDISDisplayItem tdi : values()) {
            BY_NAME.put(tdi.toString().toLowerCase(), tdi);
        }
    }

    private final Material material;
    private final Material craftMaterial;
    private final int customModelData;

    TARDISDisplayItem(Material item) {
        this.customModelData = 1;
        this.material = item;
        this.craftMaterial = null;
    }

    TARDISDisplayItem(int customModelData, Material item, Material craftMaterial) {
        this.customModelData = customModelData;
        this.material = item;
        this.craftMaterial = craftMaterial;
    }

    public static TARDISDisplayItem getByMaterialAndData(Material m, int cmd) {
        for (TARDISDisplayItem tdi : values()) {
            if (tdi == TARDISDisplayItem.CLASSIC_DOOR) {
                if (tdi.getCraftMaterial() == m && tdi.getCustomModelData() == cmd) {
                    return tdi;
                }
            } else {
                if (tdi.getMaterial() == m && tdi.getCustomModelData() == cmd) {
                    return tdi;
                }
            }
        }
        return null;
    }

    public static TARDISDisplayItem getByItemDisplay(ItemDisplay display) {
        ItemStack is = display.getItemStack();
        if (is != null) {
            ItemMeta im = is.getItemMeta();
            if (im.hasCustomModelData()) {
                return getByMaterialAndData(is.getType(), im.getCustomModelData());
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

    public Material getCraftMaterial() {
        return craftMaterial;
    }

    public boolean isLight() {
        switch (this) {
            case LIGHT_CLASSIC, LIGHT_CLASSIC_OFFSET, LIGHT_TENTH, LIGHT_ELEVENTH, LIGHT_TWELFTH, LIGHT_THIRTEENTH, LIGHT_LAMP, LIGHT_LANTERN,
                    LIGHT_CLASSIC_ON, LIGHT_CLASSIC_OFFSET_ON, LIGHT_TENTH_ON, LIGHT_ELEVENTH_ON, LIGHT_TWELFTH_ON, LIGHT_THIRTEENTH_ON, LIGHT_LAMP_ON, LIGHT_LANTERN_ON
                    -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isLit() {
        switch (this) {
            case LIGHT_CLASSIC_ON, LIGHT_CLASSIC_OFFSET_ON, LIGHT_TENTH_ON, LIGHT_ELEVENTH_ON, LIGHT_TWELFTH_ON, LIGHT_THIRTEENTH_ON, LIGHT_LAMP_ON, LIGHT_LANTERN_ON -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
