/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum GUISonicGenerator {

    // Sonic Generator
    MARK_I(10000001, 0, Material.BLAZE_ROD, "", ChatColor.DARK_GRAY),
    MARK_II(10000002, 1, Material.BLAZE_ROD, "", ChatColor.YELLOW),
    MARK_III(10000003, 2, Material.BLAZE_ROD, "", ChatColor.DARK_PURPLE),
    MARK_IV(10000004, 3, Material.BLAZE_ROD, "", ChatColor.GRAY),
    EIGHTH_DOCTOR(10000008, 4, Material.BLAZE_ROD, "", ChatColor.BLUE),
    NINTH_DOCTOR(10000009, 5, Material.BLAZE_ROD, "", ChatColor.GREEN),
    TENTH_DOCTOR(10000010, 6, Material.BLAZE_ROD, "", ChatColor.AQUA),
    ELEVENTH_DOCTOR(10000011, 7, Material.BLAZE_ROD, "", ChatColor.RESET),
    WAR_DOCTOR(10000085, 8, Material.BLAZE_ROD, "", ChatColor.DARK_RED),
    THIRTEENTH_DOCTOR(10000013, 9, Material.BLAZE_ROD, "", ChatColor.BLACK),
    MASTER(10000032, 10, Material.BLAZE_ROD, "", ChatColor.DARK_BLUE),
    SARAH_JANE(10000033, 11, Material.BLAZE_ROD, "", ChatColor.RED),
    RIVER_SONG(10000031, 12, Material.BLAZE_ROD, "", ChatColor.GOLD),
    NINTH_DOCTOR_OPEN(12000009, 14, Material.BLAZE_ROD, "", ChatColor.DARK_GREEN),
    TENTH_DOCTOR_OPEN(12000010, 15, Material.BLAZE_ROD, "", ChatColor.DARK_AQUA),
    ELEVENTH_DOCTOR_OPEN(12000011, 16, Material.BLAZE_ROD, "", ChatColor.LIGHT_PURPLE),
    TWELFTH_DOCTOR(10000012, 17, Material.BLAZE_ROD, "", ChatColor.UNDERLINE),
    STANDARD_SONIC(80, 27, Material.BOWL, "", ChatColor.RESET),
    BIO_SCANNER_UPGRADE(9, 29, Material.BOWL, "", ChatColor.RESET),
    DIAMOND_UPGRADE(50, 30, Material.BOWL, "", ChatColor.RESET),
    EMERALD_UPGRADE(51, 31, Material.BOWL, "", ChatColor.RESET),
    REDSTONE_UPGRADE(71, 32, Material.BOWL, "", ChatColor.RESET),
    PAINTER_UPGRADE(67, 33, Material.BOWL, "", ChatColor.RESET),
    IGNITE_UPGRADE(56, 34, Material.BOWL, "", ChatColor.RESET),
    PICKUP_ARROWS_UPGRADE(68, 35, Material.BOWL, "", ChatColor.RESET),
    KNOCKBACK_UPGRADE(134, 28, Material.BOWL, "", ChatColor.RESET),
    INSTRUCTIONS_1_OF_3(1, 38, Material.BOOK, "Select your Sonic Screwdriver~type from the top two rows.~Click on the upgrades you~want to add to the sonic.", ChatColor.RESET),
    INSTRUCTIONS_2_OF_3(1, 39, Material.BOOK, "You can reset the upgrades~by clicking the 'Standard' button.~The Artron cost for the~sonic is shown bottom left.", ChatColor.RESET),
    INSTRUCTIONS_3_OF_3(1, 40, Material.BOOK, "The final sonic result~is shown in the middle~of the bottom row.", ChatColor.RESET),
    SAVE_SETTINGS(74, 43, Material.BOWL, "Click to save the current sonic.~No item will be generated!", ChatColor.RESET),
    GENERATE_SONIC_SCREWDRIVER(54, 44, Material.BOWL, "Click to generate a sonic~with the current settings.", ChatColor.RESET),
    ARTRON_COST(6, 45, Material.BOWL, "", ChatColor.RESET),
    SONIC_SCREWDRIVER(10000011, 49, Material.BLAZE_ROD, "", ChatColor.RESET),
    CLOSE(1, 53, Material.BOWL, "Close the menu without~saving or generating.", ChatColor.RESET);

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String lore;
    private final ChatColor chatColor;

    GUISonicGenerator(int customModelData, int slot, Material material, String lore, ChatColor chatColor) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        this.lore = lore;
        this.chatColor = chatColor;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        String s = toString();
        return TARDISStringUtils.capitalise(s);
    }

    public String getLore() {
        return lore;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }
}
