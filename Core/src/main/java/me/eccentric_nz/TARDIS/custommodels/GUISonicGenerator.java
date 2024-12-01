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
package me.eccentric_nz.TARDIS.custommodels;

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUISonicGenerator {

    // Sonic Generator
    MARK_I(SonicVariant.MARK1.getKey(), 0, Material.BLAZE_ROD, ""),
    MARK_II(SonicVariant.MARK2.getKey(), 1, Material.BLAZE_ROD, ""),
    MARK_III(SonicVariant.MARK3.getKey(), 2, Material.BLAZE_ROD, ""),
    MARK_IV(SonicVariant.MARK4.getKey(), 3, Material.BLAZE_ROD, ""),
    EIGHTH_DOCTOR(SonicVariant.EIGHTH.getKey(), 4, Material.BLAZE_ROD, ""),
    TWELFTH_DOCTOR(SonicVariant.TWELFTH.getKey(), 5, Material.BLAZE_ROD, ""),
    THIRTEENTH_DOCTOR(SonicVariant.THIRTEENTH.getKey(), 6, Material.BLAZE_ROD, ""),
    NINTH_DOCTOR(SonicVariant.NINTH.getKey(), 7, Material.BLAZE_ROD, ""),
    TENTH_DOCTOR(SonicVariant.TENTH.getKey(), 8, Material.BLAZE_ROD, ""),
    ELEVENTH_DOCTOR(SonicVariant.ELEVENTH.getKey(), 9, Material.BLAZE_ROD, ""),
    FOURTEENTH_DOCTOR(SonicVariant.FOURTEENTH.getKey(), 10, Material.BLAZE_ROD, ""),
    FIFTEENTH_DOCTOR(SonicVariant.FIFTEENTH.getKey(), 11, Material.BLAZE_ROD, ""),
    WAR_DOCTOR(SonicVariant.WAR.getKey(), 12, Material.BLAZE_ROD, ""),
    MASTER(SonicVariant.MASTER.getKey(), 13, Material.BLAZE_ROD, ""),
    SARAH_JANE(SonicVariant.SARAH_JANE.getKey(), 14, Material.BLAZE_ROD, ""),
    AMY_POND(SonicVariant.SONIC_PROBE.getKey(), 15, Material.BLAZE_ROD, ""),
    RIVER_SONG(SonicVariant.RIVER_SONG.getKey(), 16, Material.BLAZE_ROD, ""),
    MISSY(SonicVariant.UMBRELLA.getKey(), 17, Material.BLAZE_ROD, ""),
    BRUSH_UPGRADE(GuiVariant.BRUSH.getKey(), 26, Material.BOWL, ""),
    BIO_SCANNER_UPGRADE(GuiVariant.BIO.getKey(), 27, Material.BOWL, ""),
    CONVERSION_UPGRADE(GuiVariant.CONVERSION.getKey(), 28, Material.BOWL, ""),
    DIAMOND_UPGRADE(GuiVariant.DIAMOND.getKey(), 29, Material.BOWL, ""),
    EMERALD_UPGRADE(GuiVariant.EMERALD.getKey(), 30, Material.BOWL, ""),
    IGNITE_UPGRADE(GuiVariant.IGNITE.getKey(), 31, Material.BOWL, ""),
    KNOCKBACK_UPGRADE(GuiVariant.KNOCKBACK.getKey(), 32, Material.BOWL, ""),
    PAINTER_UPGRADE(GuiVariant.PAINTER.getKey(), 33, Material.BOWL, ""),
    PICKUP_ARROWS_UPGRADE(GuiVariant.PICKUP.getKey(), 34, Material.BOWL, ""),
    REDSTONE_UPGRADE(GuiVariant.REDSTONE.getKey(), 35, Material.BOWL, ""),
    STANDARD_SONIC(GuiVariant.STANDARD_SONIC.getKey(), 36, Material.BOWL, ""),
    INSTRUCTIONS_1_OF_3(GuiVariant.INFO.getKey(), 38, Material.BOOK, "Select your Sonic Screwdriver~type from the top two rows.~Click on the upgrades you~want to add to the sonic."),
    INSTRUCTIONS_2_OF_3(GuiVariant.INFO.getKey(), 39, Material.BOOK, "You can reset the upgrades~by clicking the 'Standard' button.~The Artron cost for the~sonic is shown bottom left."),
    INSTRUCTIONS_3_OF_3(GuiVariant.INFO.getKey(), 40, Material.BOOK, "The final sonic result~is shown in the middle~of the bottom row."),
    SAVE_SETTINGS(GuiVariant.SAVE.getKey(), 43, Material.BOWL, "Click to save the current sonic.~No item will be generated!"),
    GENERATE_SONIC_SCREWDRIVER(GuiVariant.GENERATE_SONIC_SCREWDRIVER.getKey(), 44, Material.BOWL, "Click to generate a sonic~with the current settings."),
    ARTRON_COST(GuiVariant.ARTRON_BATTERY.getKey(), 45, Material.BOWL, ""),
    SONIC_SCREWDRIVER(SonicVariant.ELEVENTH.getKey(), 49, Material.BLAZE_ROD, ""),
    CLOSE(GuiVariant.CLOSE.getKey(), 53, Material.BOWL, "Close the menu without~saving or generating.");

    private final NamespacedKey model;
    private final int slot;
    private final Material material;
    private final String lore;

    GUISonicGenerator(NamespacedKey model, int slot, Material material, String lore) {
        this.model = model;
        this.slot = slot;
        this.material = material;
        this.lore = lore;
    }

    public NamespacedKey getModel() {
        return model;
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
