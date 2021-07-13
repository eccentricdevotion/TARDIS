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
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;

public enum RecipeItem {
    // shaped recipes start here
    ACID_BATTERY(10000001, RecipeCategory.MISC),
    ARTRON_STORAGE_CELL(10000001, RecipeCategory.ITEMS),
    AUTHORISED_CONTROL_DISK(10000001, RecipeCategory.STORAGE_DISKS),
    BIO_SCANNER_CIRCUIT(10001969, RecipeCategory.SONIC_CIRCUITS),
    BLANK_STORAGE_DISK(10000001, RecipeCategory.STORAGE_DISKS),
    CUSTARD_CREAM(10000002, RecipeCategory.FOOD),
    DIAMOND_DISRUPTOR_CIRCUIT(10001971, RecipeCategory.SONIC_CIRCUITS),
    EMERALD_ENVIRONMENT_CIRCUIT(10001972, RecipeCategory.SONIC_CIRCUITS),
    FISH_FINGER(10000001, RecipeCategory.FOOD),
    FOB_WATCH(10000001, RecipeCategory.ACCESSORIES),
    HANDLES(10000001, RecipeCategory.ACCESSORIES),
    IGNITE_CIRCUIT(10001982, RecipeCategory.SONIC_CIRCUITS),
    JAMMY_DODGER(10000001, RecipeCategory.FOOD),
    KNOCKBACK_CIRCUIT(10001986, RecipeCategory.SONIC_CIRCUITS),
    PAINTER_CIRCUIT(10001979, RecipeCategory.SONIC_CIRCUITS),
    PAPER_BAG(10000001, RecipeCategory.FOOD),
    PERCEPTION_CIRCUIT(10001978, RecipeCategory.ITEM_CIRCUITS),
    PERCEPTION_FILTER(14, RecipeCategory.ITEMS),
    PICKUP_ARROWS_CIRCUIT(10001984, RecipeCategory.SONIC_CIRCUITS),
    REDSTONE_ACTIVATOR_CIRCUIT(10001970, RecipeCategory.SONIC_CIRCUITS),
    RIFT_CIRCUIT(10001983, RecipeCategory.ITEM_CIRCUITS),
    RIFT_MANIPULATOR(10000001, RecipeCategory.ACCESSORIES),
    RUST_PLAGUE_SWORD(10000001, RecipeCategory.MISC),
    SERVER_ADMIN_CIRCUIT(10001968, RecipeCategory.SONIC_CIRCUITS),
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
    TARDIS_RANDOMISER_CIRCUIT(10001980, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_REMOTE_KEY(15, RecipeCategory.ITEMS),
    TARDIS_SCANNER_CIRCUIT(10001977, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_STATTENHEIM_CIRCUIT(10001963, RecipeCategory.ITEM_CIRCUITS),
    TARDIS_TELEPATHIC_CIRCUIT(10000001, RecipeCategory.CONSOLE_CIRCUITS),
    TARDIS_TEMPORAL_CIRCUIT(10001974, RecipeCategory.CONSOLE_CIRCUITS),
    TIME_ROTOR_EARLY(10000002, RecipeCategory.ROTORS),
    TIME_ROTOR_TENTH(10000003, RecipeCategory.ROTORS),
    TIME_ROTOR_ELEVENTH(10000004, RecipeCategory.ROTORS),
    TIME_ROTOR_TWELFTH(10000005, RecipeCategory.ROTORS),
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
    VORTEX_MANIPULATOR(10000002, RecipeCategory.ACCESSORIES),
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
    REDSTONE_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    DIAMOND_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    EMERALD_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    PAINTER_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    IGNITE_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    PICKUP_ARROWS_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    KNOCKBACK_UPGRADE(10000010, RecipeCategory.SONIC_UPGRADES),
    // planet items
    ACID_BUCKET(1, RecipeCategory.UNCRAFTABLE),
    RUST_BUCKET(1, RecipeCategory.UNCRAFTABLE),
    // not fond
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
