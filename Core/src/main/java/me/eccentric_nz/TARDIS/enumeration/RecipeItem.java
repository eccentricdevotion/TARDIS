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
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;

public enum RecipeItem {
    // shaped recipes start here
    ACID_BATTERY(10000001, RecipeCategory.PLANETS),
    ARTRON_CAPACITOR(10000003, RecipeCategory.ITEMS),
    ARTRON_CAPACITOR_STORAGE(1, RecipeCategory.ITEMS),
    ARTRON_STORAGE_CELL(10000001, RecipeCategory.ITEMS),
    AUTHORISED_CONTROL_DISK(10000001, RecipeCategory.STORAGE_DISKS),
    BIO_SCANNER_CIRCUIT(10001969, RecipeCategory.SONIC_CIRCUITS),
    BLANK_STORAGE_DISK(10000001, RecipeCategory.STORAGE_DISKS),
    BRUSH_CIRCUIT(10001987, RecipeCategory.SONIC_CIRCUITS),
    CONSOLE_LAMP(-1, RecipeCategory.CUSTOM_BLOCKS),
    CONSOLE_LAMP_SWITCH(9000, RecipeCategory.MISC),
    CONVERSION_CIRCUIT(10001988, RecipeCategory.SONIC_CIRCUITS),
    CUSTARD_CREAM(10000002, RecipeCategory.FOOD),
    DIAMOND_DISRUPTOR_CIRCUIT(10001971, RecipeCategory.SONIC_CIRCUITS),
    ELIXIR_OF_LIFE(2,RecipeCategory.ACCESSORIES),
    EMERALD_ENVIRONMENT_CIRCUIT(10001972, RecipeCategory.SONIC_CIRCUITS),
    EXTERIOR_LAMP_LEVEL_SWITCH(1000, RecipeCategory.MISC),
    FISH_FINGER(10000001, RecipeCategory.FOOD),
    FOB_WATCH(10000001, RecipeCategory.ACCESSORIES),
    HANDLES(10000001, RecipeCategory.ACCESSORIES),
    IGNITE_CIRCUIT(10001982, RecipeCategory.SONIC_CIRCUITS),
    INTERIOR_LIGHT_LEVEL_SWITCH(3000, RecipeCategory.MISC),
    JAMMY_DODGER(10000001, RecipeCategory.FOOD),
    KNOCKBACK_CIRCUIT(10001986, RecipeCategory.SONIC_CIRCUITS),
    MONITOR_FRAME(2, RecipeCategory.MISC),
    PAINTER_CIRCUIT(10001979, RecipeCategory.SONIC_CIRCUITS),
    PAPER_BAG(10000001, RecipeCategory.FOOD),
    PERCEPTION_CIRCUIT(10001978, RecipeCategory.ITEM_CIRCUITS),
    PERCEPTION_FILTER(14, RecipeCategory.ITEMS),
    PICKUP_ARROWS_CIRCUIT(10001984, RecipeCategory.SONIC_CIRCUITS),
    REDSTONE_ACTIVATOR_CIRCUIT(10001970, RecipeCategory.SONIC_CIRCUITS),
    RIFT_CIRCUIT(10001983, RecipeCategory.PLANETS),
    RIFT_MANIPULATOR(10000001, RecipeCategory.PLANETS),
    RUST_PLAGUE_SWORD(10000001, RecipeCategory.PLANETS),
    SERVER_ADMIN_CIRCUIT(10001968, RecipeCategory.SONIC_CIRCUITS),
    SONIC_DOCK(1000, RecipeCategory.SONIC_CIRCUITS),
    SONIC_GENERATOR(10000001, RecipeCategory.ITEM_CIRCUITS),
    SONIC_OSCILLATOR(10001967, RecipeCategory.ITEM_CIRCUITS),
    SONIC_SCREWDRIVER(10000011, RecipeCategory.ITEMS),
    STATTENHEIM_REMOTE(10000001, RecipeCategory.ITEMS),
    TARDIS_ARS_CIRCUIT(10001973, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_ARTRON_FURNACE(10000001, RecipeCategory.ITEMS),
    TARDIS_BIOME_READER(10000001, RecipeCategory.ITEMS),
    TARDIS_CHAMELEON_CIRCUIT(10001966, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_COMMUNICATOR(10000040, RecipeCategory.ACCESSORIES),
    TARDIS_INPUT_CIRCUIT(10001976, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_INVISIBILITY_CIRCUIT(10001981, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_KEY(1, RecipeCategory.ITEMS),
    TARDIS_LOCATOR(10000001, RecipeCategory.ITEMS),
    TARDIS_LOCATOR_CIRCUIT(10001965, RecipeCategory.ITEM_CIRCUITS),
    TARDIS_MATERIALISATION_CIRCUIT(10001964, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_MEMORY_CIRCUIT(10001975, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_MONITOR(5, RecipeCategory.MISC),
    TARDIS_RANDOMISER_CIRCUIT(10001980, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_REMOTE_KEY(15, RecipeCategory.ITEMS),
    TARDIS_SCANNER_CIRCUIT(10001977, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_SPACE_HELMET(5, RecipeCategory.ACCESSORIES),
    TARDIS_STATTENHEIM_CIRCUIT(10001963, RecipeCategory.ITEM_CIRCUITS),
    TARDIS_TELEPATHIC_CIRCUIT(10001962, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_TELEVISION(1, RecipeCategory.ITEMS),
    TARDIS_TEMPORAL_CIRCUIT(10001974, RecipeCategory.CONSOLE_CIRCUITS),
    // rotors
    TIME_ROTOR_CONSOLE(10000100, RecipeCategory.ROTORS),
    TIME_ROTOR_RUSTIC(10000101, RecipeCategory.ROTORS),
    TIME_ROTOR_DELTA(10000006, RecipeCategory.ROTORS),
    TIME_ROTOR_EARLY(10000002, RecipeCategory.ROTORS),
    TIME_ENGINE(10000007, RecipeCategory.ROTORS),
    TIME_ROTOR_ENGINE(10000008, RecipeCategory.ROTORS),
    TIME_ROTOR_HOSPITAL(10000009, RecipeCategory.ROTORS),
    TIME_ROTOR_TENTH(10000003, RecipeCategory.ROTORS),
    TIME_ROTOR_ELEVENTH(10000004, RecipeCategory.ROTORS),
    TIME_ROTOR_TWELFTH(10000005, RecipeCategory.ROTORS),
    // consoles
    LIGHT_GRAY_CONSOLE(1001, RecipeCategory.CONSOLES),
    GRAY_CONSOLE(1002, RecipeCategory.CONSOLES),
    WHITE_CONSOLE(1003, RecipeCategory.CONSOLES),
    BLACK_CONSOLE(1004, RecipeCategory.CONSOLES),
    RED_CONSOLE(1005, RecipeCategory.CONSOLES),
    ORANGE_CONSOLE(1006, RecipeCategory.CONSOLES),
    YELLOW_CONSOLE(1007, RecipeCategory.CONSOLES),
    LIME_CONSOLE(1008, RecipeCategory.CONSOLES),
    GREEN_CONSOLE(1009, RecipeCategory.CONSOLES),
    CYAN_CONSOLE(1010, RecipeCategory.CONSOLES),
    LIGHT_BLUE_CONSOLE(1011, RecipeCategory.CONSOLES),
    BLUE_CONSOLE(1012, RecipeCategory.CONSOLES),
    PURPLE_CONSOLE(1013, RecipeCategory.CONSOLES),
    MAGENTA_CONSOLE(1014, RecipeCategory.CONSOLES),
    PINK_CONSOLE(1015, RecipeCategory.CONSOLES),
    BROWN_CONSOLE(1016, RecipeCategory.CONSOLES),
    RUSTIC_CONSOLE(1017, RecipeCategory.CONSOLES),
    // bow ties
    WHITE_BOW_TIE(10000023),
    ORANGE_BOW_TIE(10000024),
    MAGENTA_BOW_TIE(10000025),
    LIGHT_BLUE_BOW_TIE(10000026),
    YELLOW_BOW_TIE(10000027),
    LIME_BOW_TIE(10000028),
    PINK_BOW_TIE(10000029),
    GREY_BOW_TIE(10000030),
    LIGHT_GREY_BOW_TIE(10000031),
    CYAN_BOW_TIE(10000032),
    PURPLE_BOW_TIE(10000033),
    BLUE_BOW_TIE(10000034),
    BROWN_BOW_TIE(10000035),
    GREEN_BOW_TIE(10000036),
    RED_BOW_TIE(10000037, RecipeCategory.ACCESSORIES),
    BLACK_BOW_TIE(10000038),
    THREE_D_GLASSES(10000039, RecipeCategory.ACCESSORIES),
    // module recipes
    VORTEX_MANIPULATOR(10000002, RecipeCategory.ACCESSORIES),
    SONIC_BLASTER(10000002, RecipeCategory.ACCESSORIES),
    BLASTER_BATTERY(10000002, RecipeCategory.MISC),
    LANDING_PAD(10000001, RecipeCategory.ACCESSORIES),
    JUDOON_AMMUNITION(13, RecipeCategory.MISC),
    K9(1, RecipeCategory.MISC),
    // custom block recipes start here
    UNTEMPERED_SCHISM(1, RecipeCategory.ITEMS),
    GROW(10001, RecipeCategory.CUSTOM_BLOCKS),
    BLUE_BOX(10001, RecipeCategory.CUSTOM_BLOCKS),
    COG(10001, RecipeCategory.CUSTOM_BLOCKS),
    HEXAGON(10001, RecipeCategory.CUSTOM_BLOCKS),
    ROUNDEL(10001, RecipeCategory.CUSTOM_BLOCKS),
    ROUNDEL_OFFSET(10002, RecipeCategory.CUSTOM_BLOCKS),
    THE_MOMENT(10001, RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_BULB(10008, RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_CLASSIC(10005, RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_CLASSIC_OFFSET(10010, RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_TENTH(10006, RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_ELEVENTH(10007, RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_TWELFTH(10008, RecipeCategory.CUSTOM_BLOCKS),
    LIGHT_THIRTEENTH(10009, RecipeCategory.CUSTOM_BLOCKS),
    DOOR(10001, RecipeCategory.CUSTOM_BLOCKS),
    BONE_DOOR(10004, RecipeCategory.CUSTOM_BLOCKS),
    CLASSIC_DOOR(10004, RecipeCategory.CUSTOM_BLOCKS),
//    HANDBRAKE(1001, RecipeCategory.CUSTOM_BLOCKS),
//    THROTTLE(1001, RecipeCategory.CUSTOM_BLOCKS),
    // unshaped recipes start here
    BIOME_STORAGE_DISK(10000001, RecipeCategory.STORAGE_DISKS),
    BOWL_OF_CUSTARD(10000001, RecipeCategory.FOOD),
    PLAYER_STORAGE_DISK(10000001, RecipeCategory.STORAGE_DISKS),
    PRESET_STORAGE_DISK(10000001, RecipeCategory.STORAGE_DISKS),
    SAVE_STORAGE_DISK(10000001, RecipeCategory.STORAGE_DISKS),
    TARDIS_SCHEMATIC_WAND(10000001, RecipeCategory.MISC),
    // jelly babies
    VANILLA_JELLY_BABY(10000001),
    ORANGE_JELLY_BABY(10000002, RecipeCategory.FOOD),
    WATERMELON_JELLY_BABY(10000003),
    BUBBLEGUM_JELLY_BABY(10000004),
    LEMON_JELLY_BABY(10000005),
    LIME_JELLY_BABY(10000006),
    STRAWBERRY_JELLY_BABY(10000007),
    EARL_GREY_JELLY_BABY(10000008),
    VODKA_JELLY_BABY(10000009),
    ISLAND_PUNCH_JELLY_BABY(10000010),
    GRAPE_JELLY_BABY(10000011),
    BLUEBERRY_JELLY_BABY(10000012),
    CAPPUCCINO_JELLY_BABY(10000013),
    APPLE_JELLY_BABY(10000014),
    RASPBERRY_JELLY_BABY(10000015),
    LICORICE_JELLY_BABY(10000016),
    // sonic upgrades
    ADMIN_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    BIO_SCANNER_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    BRUSH_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    REDSTONE_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    DIAMOND_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    EMERALD_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    PAINTER_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    IGNITE_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    PICKUP_ARROWS_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    KNOCKBACK_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    CONVERSION_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    // planet items
    ACID_BUCKET(1, RecipeCategory.UNCRAFTABLE),
    RUST_BUCKET(1, RecipeCategory.UNCRAFTABLE),
    // chemistry
    ATOMIC_ELEMENTS(10001, RecipeCategory.CHEMISTRY),
    CHEMICAL_COMPOUNDS(10001, RecipeCategory.CHEMISTRY),
    LAB_TABLE(10001, RecipeCategory.CHEMISTRY),
    PRODUCT_CRAFTING(10001, RecipeCategory.CHEMISTRY),
    MATERIAL_REDUCER(10001, RecipeCategory.CHEMISTRY),
    ELEMENT_CONSTRUCTOR(10001, RecipeCategory.CHEMISTRY),
    BLUE_LAMP(10001, RecipeCategory.CHEMISTRY),
    GREEN_LAMP(10002, RecipeCategory.CHEMISTRY),
    PURPLE_LAMP(10003, RecipeCategory.CHEMISTRY),
    RED_LAMP(10004, RecipeCategory.CHEMISTRY),
    HEAT_BLOCK(10001, RecipeCategory.CHEMISTRY),
    BALLOON(10000019, RecipeCategory.CHEMISTRY),
    BLEACH(1, RecipeCategory.CHEMISTRY),
    GLOW_STICK(10000005, RecipeCategory.CHEMISTRY),
    ICE_BOMB(3, RecipeCategory.CHEMISTRY),
    SPARKLER(10000035, RecipeCategory.CHEMISTRY),
    SUPER_FERTILISER(4, RecipeCategory.CHEMISTRY),
    // microscope
    COMPUTER_MONITOR(10000, RecipeCategory.MICROSCOPE),
    ELECTRON_MICROSCOPE(10000, RecipeCategory.MICROSCOPE),
    FILING_CABINET(10000, RecipeCategory.MICROSCOPE),
    MICROSCOPE(10000, RecipeCategory.MICROSCOPE),
    SLIDE_RACK(10000, RecipeCategory.MICROSCOPE),
    TELESCOPE(10000, RecipeCategory.MICROSCOPE),
    // not found
    NOT_FOUND(-1, RecipeCategory.UNCRAFTABLE);

    private final int customModelData;
    private final RecipeCategory category;

    RecipeItem(int customModelData, RecipeCategory category) {
        this.customModelData = customModelData;
        this.category = category;
    }

    RecipeItem(int customModelData) {
        this.customModelData = customModelData;
        category = RecipeCategory.UNUSED;
    }

    public static RecipeItem getByName(String name) {
        String processed = TARDISStringUtils.toEnumUppercase(name);
        try {
            return RecipeItem.valueOf(processed);
        } catch (IllegalArgumentException e) {
            return NOT_FOUND;
        }
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public RecipeCategory getCategory() {
        return category;
    }

    public String toRecipeString() {
        return switch (this) {
            case THREE_D_GLASSES -> "3-D Glasses";
            case BIO_SCANNER_CIRCUIT -> "Bio-scanner Circuit";
            case BIO_SCANNER_UPGRADE -> "Bio-scanner Upgrade";
            case BOWL_OF_CUSTARD -> "Bowl of Custard";
            case ELIXIR_OF_LIFE -> "Elixir of Life";
            case TARDIS_ARS_CIRCUIT -> "TARDIS ARS Circuit";
            default -> TARDISStringUtils.capitalise(toString()).replace("Tardis", "TARDIS");
        };
    }

    public String toTabCompletionString() {
        String recipe = toRecipeString();
        if (this == THREE_D_GLASSES) {
            return "3-d-glasses";
        } else if (recipe.startsWith("TARDIS")) {
            return TARDISStringUtils.toLowercaseDashed(recipe).replace("tardis-", "");
        } else if (recipe.endsWith("Baby")) {
            return "jelly-baby";
        } else if (recipe.endsWith("Tie")) {
            return "bow-tie";
        } else {
            return TARDISStringUtils.toLowercaseDashed(recipe);
        }
    }
}
