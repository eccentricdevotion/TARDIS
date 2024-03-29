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
package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUISonicGenerator {

    // Sonic Generator
    MARK_I(10000001, 0, Material.BLAZE_ROD, ""),
    MARK_II(10000002, 1, Material.BLAZE_ROD, ""),
    MARK_III(10000003, 2, Material.BLAZE_ROD, ""),
    MARK_IV(10000004, 3, Material.BLAZE_ROD, ""),
    EIGHTH_DOCTOR(10000008, 4, Material.BLAZE_ROD, ""),
    TWELFTH_DOCTOR(10000012, 5, Material.BLAZE_ROD, ""),
    THIRTEENTH_DOCTOR(10000013, 6, Material.BLAZE_ROD, ""),
    NINTH_DOCTOR(10000009, 7, Material.BLAZE_ROD, ""),
    TENTH_DOCTOR(10000010, 8, Material.BLAZE_ROD, ""),
    ELEVENTH_DOCTOR(10000011, 9, Material.BLAZE_ROD, ""),
    FOURTEENTH_DOCTOR(10000014, 10, Material.BLAZE_ROD, ""),
    FIFTEENTH_DOCTOR(10000015, 11, Material.BLAZE_ROD, ""),
    WAR_DOCTOR(10000085, 12, Material.BLAZE_ROD, ""),
    MASTER(10000032, 13, Material.BLAZE_ROD, ""),
    SARAH_JANE(10000033, 14, Material.BLAZE_ROD, ""),
    AMY_POND(10000034, 15, Material.BLAZE_ROD, ""),
    RIVER_SONG(10000031, 16, Material.BLAZE_ROD, ""),
    MISSY(10000035, 17, Material.BLAZE_ROD, ""),
    BRUSH_UPGRADE(150, 26, Material.BOWL, ""),
    BIO_SCANNER_UPGRADE(9, 27, Material.BOWL, ""),
    CONVERSION_UPGRADE(154, 28, Material.BOWL, ""),
    DIAMOND_UPGRADE(50, 29, Material.BOWL, ""),
    EMERALD_UPGRADE(51, 30, Material.BOWL, ""),
    IGNITE_UPGRADE(56, 31, Material.BOWL, ""),
    KNOCKBACK_UPGRADE(134, 32, Material.BOWL, ""),
    PAINTER_UPGRADE(67, 33, Material.BOWL, ""),
    PICKUP_ARROWS_UPGRADE(68, 34, Material.BOWL, ""),
    REDSTONE_UPGRADE(71, 35, Material.BOWL, ""),
    STANDARD_SONIC(80, 36, Material.BOWL, ""),
    INSTRUCTIONS_1_OF_3(1, 38, Material.BOOK, "Select your Sonic Screwdriver~type from the top two rows.~Click on the upgrades you~want to add to the sonic."),
    INSTRUCTIONS_2_OF_3(1, 39, Material.BOOK, "You can reset the upgrades~by clicking the 'Standard' button.~The Artron cost for the~sonic is shown bottom left."),
    INSTRUCTIONS_3_OF_3(1, 40, Material.BOOK, "The final sonic result~is shown in the middle~of the bottom row."),
    SAVE_SETTINGS(74, 43, Material.BOWL, "Click to save the current sonic.~No item will be generated!"),
    GENERATE_SONIC_SCREWDRIVER(54, 44, Material.BOWL, "Click to generate a sonic~with the current settings."),
    ARTRON_COST(6, 45, Material.BOWL, ""),
    SONIC_SCREWDRIVER(10000011, 49, Material.BLAZE_ROD, ""),
    CLOSE(1, 53, Material.BOWL, "Close the menu without~saving or generating.");

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String lore;

    GUISonicGenerator(int customModelData, int slot, Material material, String lore) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        this.lore = lore;
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
}
