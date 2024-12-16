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

import me.eccentric_nz.TARDIS.custommodels.keys.*;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public enum TARDISDisplayItem {

    // seed blocks
    ANCIENT(SeedBlock.ANCIENT.getKey(), Material.SCULK),
    ARS(SeedBlock.ARS.getKey(), Material.QUARTZ_BLOCK),
    BIGGER(SeedBlock.BIGGER.getKey(), Material.GOLD_BLOCK),
    BONE(SeedBlock.BONE.getKey(), Material.WAXED_OXIDIZED_CUT_COPPER),
    BUDGET(SeedBlock.BUDGET.getKey(), Material.IRON_BLOCK),
    CAVE(SeedBlock.CAVE.getKey(), Material.DRIPSTONE_BLOCK),
    COPPER(SeedBlock.COPPER.getKey(), Material.COPPER_BLOCK),
    CORAL(SeedBlock.CORAL.getKey(), Material.FIRE_CORAL_BLOCK),
    CURSED(SeedBlock.CURSED.getKey(), Material.BLACK_CONCRETE),
    DELTA(SeedBlock.DELTA.getKey(), Material.CRYING_OBSIDIAN),
    DELUXE(SeedBlock.DELUXE.getKey(), Material.DIAMOND_BLOCK),
    DIVISION(SeedBlock.DIVISION.getKey(), Material.PINK_GLAZED_TERRACOTTA),
    ELEVENTH(SeedBlock.ELEVENTH.getKey(), Material.EMERALD_BLOCK),
    ENDER(SeedBlock.ENDER.getKey(), Material.PURPUR_BLOCK),
    EYE_STORAGE(Whoniverse.EYE_STORAGE.getKey(), Material.GRAY_SHULKER_BOX),
    FACTORY(SeedBlock.FACTORY.getKey(), Material.GRAY_CONCRETE),
    FIFTEENTH(SeedBlock.FIFTEENTH.getKey(), Material.OCHRE_FROGLIGHT),
    FUGITIVE(SeedBlock.FUGITIVE.getKey(), Material.POLISHED_DEEPSLATE),
    HOSPITAL(SeedBlock.HOSPITAL.getKey(), Material.WHITE_CONCRETE),
    MASTER(SeedBlock.MASTER.getKey(), Material.NETHER_BRICKS),
    MECHANICAL(SeedBlock.MECHANICAL.getKey(), Material.POLISHED_ANDESITE),
    ORIGINAL(SeedBlock.ORIGINAL.getKey(), Material.PACKED_MUD),
    PLANK(SeedBlock.PLANK.getKey(), Material.BOOKSHELF),
    PYRAMID(SeedBlock.PYRAMID.getKey(), Material.SANDSTONE_STAIRS),
    REDSTONE(SeedBlock.REDSTONE.getKey(), Material.REDSTONE_BLOCK),
    ROTOR(SeedBlock.ROTOR.getKey(), Material.HONEYCOMB_BLOCK),
    RUSTIC(SeedBlock.RUSTIC.getKey(), Material.COPPER_BULB),
    STEAMPUNK(SeedBlock.STEAMPUNK.getKey(), Material.COAL_BLOCK),
    TELEVISION(Whoniverse.TV.getKey(), Material.BROWN_STAINED_GLASS),
    THIRTEENTH(SeedBlock.THIRTEENTH.getKey(), Material.HORN_CORAL_BLOCK),
    TOM(SeedBlock.TOM.getKey(), Material.LAPIS_BLOCK),
    TWELFTH(SeedBlock.TWELFTH.getKey(), Material.PRISMARINE),
    WAR(SeedBlock.WAR.getKey(), Material.WHITE_TERRACOTTA),
    WEATHERED(SeedBlock.WEATHERED.getKey(), Material.WEATHERED_COPPER),
    SMALL(SeedBlock.SMALL.getKey(), Material.COBBLESTONE),
    MEDIUM(SeedBlock.MEDIUM.getKey(), Material.COBBLESTONE),
    TALL(SeedBlock.TALL.getKey(), Material.COBBLESTONE),
    LEGACY_BIGGER(SeedBlock.LEGACY_BIGGER.getKey(), Material.ORANGE_GLAZED_TERRACOTTA),
    LEGACY_DELUXE(SeedBlock.LEGACY_DELUXE.getKey(), Material.LIME_GLAZED_TERRACOTTA),
    LEGACY_ELEVENTH(SeedBlock.LEGACY_ELEVENTH.getKey(), Material.CYAN_GLAZED_TERRACOTTA),
    LEGACY_REDSTONE(SeedBlock.LEGACY_REDSTONE.getKey(), Material.RED_GLAZED_TERRACOTTA),
    CUSTOM(SeedBlock.CUSTOM.getKey(), Material.POLISHED_BLACKSTONE),
    // growing seed block
    GROW(SeedBlock.GROW.getKey(), Material.LIGHT_GRAY_TERRACOTTA, Material.NETHERITE_BLOCK),
    // tardis blocks
    ADVANCED_CONSOLE(Whoniverse.ADVANCED_CONSOLE.getKey(), Material.JUKEBOX),
    ARTRON_FURNACE(Whoniverse.ARTRON_FURNACE.getKey(), Material.FURNACE),
    ARTRON_FURNACE_LIT(Whoniverse.ARTRON_FURNACE_LIT.getKey(), Material.FURNACE),
    BLUE_BOX(Wool.BLUE_BOX.getKey(), Material.BLUE_WOOL, Material.BLUE_DYE),
    COG(Wool.COG.getKey(), Material.GRAY_WOOL, Material.GRAY_DYE),
    DISK_STORAGE(Whoniverse.DISK_STORAGE.getKey(), Material.NOTE_BLOCK),
    //    HANDBRAKE(1001, Material.LEVER, Material.LEVER),
    HEXAGON(Wool.HEXAGON.getKey(), Material.ORANGE_WOOL, Material.ORANGE_DYE),
    ROUNDEL(Wool.ROUNDEL.getKey(), Material.WHITE_WOOL, Material.WHITE_DYE),
    ROUNDEL_OFFSET(Wool.ROUNDEL_OFFSET.getKey(), Material.WHITE_WOOL, Material.LIGHT_GRAY_DYE),
    PANDORICA(Whoniverse.PANDORICA.getKey(), Material.BLACK_CONCRETE),
    SIEGE_CUBE(Whoniverse.SIEGE_CUBE.getKey(), Material.CYAN_CONCRETE),
    THE_MOMENT(Wool.THE_MOMENT.getKey(), Material.BROWN_WOOL, Material.REDSTONE_BLOCK),
    UNTEMPERED_SCHISM(Schism.UNTEMPERED_SCHISM.getKey(), Material.ANCIENT_DEBRIS),
    DOOR(TardisDoorVariant.TARDIS_DOOR_CLOSED.getKey(), Material.IRON_DOOR, Material.IRON_DOOR),
    DOOR_OPEN(TardisDoorVariant.TARDIS_DOOR_OPEN.getKey(), Material.IRON_DOOR),
    DOOR_BOTH_OPEN(TardisDoorVariant.TARDIS_DOOR_EXTRA.getKey(), Material.IRON_DOOR),
    BONE_DOOR(BoneDoorVariant.BONE_DOOR_CLOSED.getKey(), Material.IRON_DOOR, Material.BIRCH_DOOR),
    BONE_DOOR_OPEN(BoneDoorVariant.BONE_DOOR_4.getKey(), Material.BIRCH_DOOR),
    CLASSIC_DOOR(ClassicDoorVariant.CLASSIC_DOOR_CLOSED.getKey(), Material.IRON_DOOR, Material.CHERRY_DOOR),
    CLASSIC_DOOR_OPEN(ClassicDoorVariant.CLASSIC_DOOR_OPEN.getKey(), Material.CHERRY_DOOR),
    CUSTOM_DOOR(null, Material.IRON_DOOR),
    SONIC_GENERATOR(SonicItem.SONIC_GENERATOR.getKey(), Material.FLOWER_POT),
    //    THROTTLE(1001, Material.REPEATER, Material.REPEATER),
    // chemistry lamps off
    BLUE_LAMP(LampVariant.BLUE_LAMP.getKey(), Material.REDSTONE_LAMP),
    GREEN_LAMP(LampVariant.GREEN_LAMP.getKey(), Material.REDSTONE_LAMP),
    PURPLE_LAMP(LampVariant.PURPLE_LAMP.getKey(), Material.REDSTONE_LAMP),
    RED_LAMP(LampVariant.RED_LAMP.getKey(), Material.REDSTONE_LAMP),
    // chemistry lamps on
    BLUE_LAMP_ON(LanternVariant.BLUE_LAMP_ON.getKey(), Material.SEA_LANTERN),
    GREEN_LAMP_ON(LanternVariant.GREEN_LAMP_ON.getKey(), Material.SEA_LANTERN),
    PURPLE_LAMP_ON(LanternVariant.PURPLE_LAMP_ON.getKey(), Material.SEA_LANTERN),
    RED_LAMP_ON(LanternVariant.RED_LAMP_ON.getKey(), Material.SEA_LANTERN),
    // chemistry gui blocks
    COMPOUND(ChemistryEquipment.COMPOUND.getKey(), Material.ORANGE_CONCRETE),
    CONSTRUCTOR(ChemistryEquipment.CONSTRUCTOR.getKey(), Material.LIGHT_BLUE_CONCRETE),
    CREATIVE(ChemistryEquipment.CREATIVE.getKey(), Material.LIGHT_GRAY_CONCRETE),
    LAB(ChemistryEquipment.LAB.getKey(), Material.YELLOW_CONCRETE),
    PRODUCT(ChemistryEquipment.PRODUCT.getKey(), Material.LIME_CONCRETE),
    REDUCER(ChemistryEquipment.REDUCER.getKey(), Material.MAGENTA_CONCRETE),
    HEAT_BLOCK(ChemistryEquipment.HEAT_BLOCK.getKey(), Material.RED_CONCRETE),
    // lights off
    LIGHT_BULB(LightVariant.BULB.getKey(), Material.WAXED_COPPER_BULB, Material.COPPER_BULB),
    LIGHT_CLASSIC(Wool.CLASSIC.getKey(), Material.GRAY_WOOL, Material.TORCH),
    LIGHT_CLASSIC_OFFSET(Wool.CLASSIC_OFFSET.getKey(), Material.GRAY_WOOL, Material.SOUL_TORCH),
    LIGHT_TENTH(Wool.TENTH.getKey(), Material.GRAY_WOOL, Material.ORANGE_DYE),
    LIGHT_ELEVENTH(Wool.ELEVENTH.getKey(), Material.GRAY_WOOL, Material.YELLOW_DYE),
    LIGHT_TWELFTH(Wool.TWELFTH.getKey(), Material.GRAY_WOOL, Material.CYAN_DYE),
    LIGHT_THIRTEENTH(Wool.THIRTEENTH.getKey(), Material.GRAY_WOOL, Material.BLUE_DYE),
    LIGHT_LAMP(null, Material.REDSTONE_LAMP),
    LIGHT_LANTERN(null, Material.GRAY_WOOL),
    LIGHT_VARIABLE(LightVariant.OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_BLUE(LightVariant.BLUE_OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_GREEN(LightVariant.GREEN_OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_ORANGE(LightVariant.ORANGE_OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_PINK(LightVariant.PINK_OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_PURPLE(LightVariant.PURPLE_OFF.getKey(), Material.GLASS),
    LIGHT_VARIABLE_YELLOW(LightVariant.YELLOW_OFF.getKey(), Material.GLASS),
    // lights on
    LIGHT_BULB_ON(LampVariant.BULB_ON.getKey(), Material.REDSTONE_LAMP),
    LIGHT_CLASSIC_ON(LanternVariant.CLASSIC_ON.getKey(), Material.SEA_LANTERN),
    LIGHT_CLASSIC_OFFSET_ON(LanternVariant.CLASSIC_OFFSET_ON.getKey(), Material.SEA_LANTERN),
    LIGHT_TENTH_ON(LampVariant.TENTH_ON.getKey(), Material.REDSTONE_LAMP),
    LIGHT_ELEVENTH_ON(LampVariant.ELEVENTH_ON.getKey(), Material.REDSTONE_LAMP),
    LIGHT_TWELFTH_ON(LanternVariant.TWELFTH_ON.getKey(), Material.SEA_LANTERN),
    LIGHT_THIRTEENTH_ON(LanternVariant.THIRTEENTH_ON.getKey(), Material.SEA_LANTERN),
    LIGHT_LAMP_ON(LampVariant.LAMP_ON.getKey(), Material.REDSTONE_LAMP),
    LIGHT_LANTERN_ON(null, Material.SEA_LANTERN),
    LIGHT_VARIABLE_ON(LightVariant.VARIABLE.getKey(), Material.GLASS),
    LIGHT_VARIABLE_BLUE_ON(LightVariant.BLUE.getKey(), Material.GLASS),
    LIGHT_VARIABLE_GREEN_ON(LightVariant.GREEN.getKey(), Material.GLASS),
    LIGHT_VARIABLE_ORANGE_ON(LightVariant.ORANGE.getKey(), Material.GLASS),
    LIGHT_VARIABLE_PINK_ON(LightVariant.PINK.getKey(), Material.GLASS),
    LIGHT_VARIABLE_PURPLE_ON(LightVariant.PURPLE.getKey(), Material.GLASS),
    LIGHT_VARIABLE_YELLOW_ON(LightVariant.YELLOW.getKey(), Material.GLASS),
    // lights cloister
    LIGHT_BULB_CLOISTER(LampVariant.BULB_CLOISTER.getKey(), Material.REDSTONE_LAMP),
    LIGHT_CLASSIC_CLOISTER(LanternVariant.CLASSIC_CLOISTER.getKey(), Material.SEA_LANTERN),
    LIGHT_CLASSIC_OFFSET_CLOISTER(LanternVariant.CLASSIC_OFFSET_CLOISTER.getKey(), Material.SEA_LANTERN),
    LIGHT_TENTH_CLOISTER(LampVariant.TENTH_CLOISTER.getKey(), Material.REDSTONE_LAMP),
    LIGHT_ELEVENTH_CLOISTER(LampVariant.ELEVENTH_CLOISTER.getKey(), Material.REDSTONE_LAMP),
    LIGHT_TWELFTH_CLOISTER(LanternVariant.TWELFTH_CLOISTER.getKey(), Material.SEA_LANTERN),
    LIGHT_THIRTEENTH_CLOISTER(LanternVariant.THIRTEENTH_CLOISTER.getKey(), Material.SEA_LANTERN),
    LIGHT_LAMP_CLOISTER(null, Material.REDSTONE_LAMP),
    LIGHT_LANTERN_CLOISTER(null, Material.SEA_LANTERN),
    LIGHT_VARIABLE_CLOISTER(LightVariant.CLOISTER.getKey(), Material.GLASS),
    // console lamp
    CONSOLE_LAMP(null, Material.GLASS, Material.REDSTONE_LAMP),
    // dummy
    NONE(null, null);

    private static final HashMap<String, TARDISDisplayItem> BY_NAME = new HashMap<>();
    private static final HashMap<Integer, TARDISDisplayItem> BY_MUSHROOM_STEM = new HashMap<>() {
        {
            put(1, BLUE_LAMP);
            put(2, RED_LAMP);
            put(3, PURPLE_LAMP);
            put(4, GREEN_LAMP);
            put(10000001, BLUE_LAMP_ON);
            put(10000002, RED_LAMP_ON);
            put(10000003, PURPLE_LAMP_ON);
            put(10000004, GREEN_LAMP_ON);
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
            BY_NAME.put(tdi.toString().toLowerCase(Locale.ROOT), tdi);
        }
    }

    private final Material material;
    private final Material craftMaterial;
    private final NamespacedKey customModel;

    TARDISDisplayItem(NamespacedKey customModel, Material item, Material craftMaterial) {
        this.customModel = customModel;
        this.material = item;
        this.craftMaterial = craftMaterial;
    }

    TARDISDisplayItem(NamespacedKey customModel, Material item) {
        this.customModel = customModel;
        this.material = item;
        this.craftMaterial = null;
    }

    public static TARDISDisplayItem getByModel(NamespacedKey key) {
        for (TARDISDisplayItem tdi : values()) {
            if (tdi.getCustomModel().equals(key)) {
                return tdi;
            }
        }
        return null;
    }

    public static TARDISDisplayItem getByItemDisplay(ItemDisplay display) {
        ItemStack is = display.getItemStack();
        if (is != null) {
            ItemMeta im = is.getItemMeta();
            if (im.hasItemModel()) {
                return getByModel(im.getItemModel());
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
        return this.toString().toLowerCase(Locale.ROOT);
    }

    public String getDisplayName() {
        return TARDISStringUtils.capitalise(this.getName());
    }

    public NamespacedKey getCustomModel() {
        return customModel;
    }

    public Material getMaterial() {
        return material;
    }

    public Material getCraftMaterial() {
        return craftMaterial;
    }

    public boolean isLight() {
        switch (this) {
            case LIGHT_BULB, LIGHT_CLASSIC, LIGHT_CLASSIC_OFFSET, LIGHT_TENTH, LIGHT_ELEVENTH, LIGHT_TWELFTH,
                 LIGHT_THIRTEENTH, LIGHT_LAMP, LIGHT_LANTERN, LIGHT_BULB_ON, LIGHT_CLASSIC_ON, LIGHT_CLASSIC_OFFSET_ON,
                 LIGHT_TENTH_ON, LIGHT_ELEVENTH_ON, LIGHT_TWELFTH_ON, LIGHT_THIRTEENTH_ON, LIGHT_LAMP_ON,
                 LIGHT_LANTERN_ON, BLUE_LAMP, GREEN_LAMP, PURPLE_LAMP, RED_LAMP, UNTEMPERED_SCHISM, LIGHT_VARIABLE,
                 LIGHT_VARIABLE_ON, LIGHT_VARIABLE_BLUE_ON, LIGHT_VARIABLE_GREEN_ON, LIGHT_VARIABLE_ORANGE_ON,
                 LIGHT_VARIABLE_PINK_ON, LIGHT_VARIABLE_PURPLE_ON, LIGHT_VARIABLE_YELLOW_ON, BLUE_LAMP_ON,
                 GREEN_LAMP_ON, PURPLE_LAMP_ON, RED_LAMP_ON -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isLit() {
        switch (this) {
            case LIGHT_BULB_ON, LIGHT_CLASSIC_ON, LIGHT_CLASSIC_OFFSET_ON, LIGHT_TENTH_ON, LIGHT_ELEVENTH_ON,
                 LIGHT_TWELFTH_ON, LIGHT_THIRTEENTH_ON, LIGHT_LAMP_ON, LIGHT_LANTERN_ON, UNTEMPERED_SCHISM,
                 LIGHT_VARIABLE_ON, LIGHT_VARIABLE_BLUE_ON, LIGHT_VARIABLE_GREEN_ON, LIGHT_VARIABLE_ORANGE_ON,
                 LIGHT_VARIABLE_PINK_ON, LIGHT_VARIABLE_PURPLE_ON, LIGHT_VARIABLE_YELLOW_ON, BLUE_LAMP_ON,
                 GREEN_LAMP_ON, PURPLE_LAMP_ON, RED_LAMP_ON, CONSOLE_LAMP -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isVariable() {
        switch (this) {
            case LIGHT_VARIABLE_ON, LIGHT_VARIABLE_BLUE_ON, LIGHT_VARIABLE_GREEN_ON, LIGHT_VARIABLE_ORANGE_ON,
                 LIGHT_VARIABLE_PINK_ON, LIGHT_VARIABLE_PURPLE_ON, LIGHT_VARIABLE_YELLOW_ON, LIGHT_VARIABLE,
                 LIGHT_VARIABLE_BLUE, LIGHT_VARIABLE_GREEN, LIGHT_VARIABLE_ORANGE, LIGHT_VARIABLE_PINK,
                 LIGHT_VARIABLE_PURPLE, LIGHT_VARIABLE_YELLOW, LIGHT_VARIABLE_CLOISTER -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
