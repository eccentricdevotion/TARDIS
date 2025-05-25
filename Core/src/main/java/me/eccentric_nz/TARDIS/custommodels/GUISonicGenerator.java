/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.List;

public enum GUISonicGenerator {

    // Sonic Generator
    MARK_I(SonicVariant.MARK1.getFloats(), 0, Material.BLAZE_ROD, ""),
    MARK_II(SonicVariant.MARK2.getFloats(), 1, Material.BLAZE_ROD, ""),
    MARK_III(SonicVariant.MARK3.getFloats(), 2, Material.BLAZE_ROD, ""),
    MARK_IV(SonicVariant.MARK4.getFloats(), 3, Material.BLAZE_ROD, ""),
    EIGHTH_DOCTOR(SonicVariant.EIGHTH.getFloats(), 4, Material.BLAZE_ROD, ""),
    TWELFTH_DOCTOR(SonicVariant.TWELFTH.getFloats(), 5, Material.BLAZE_ROD, ""),
    THIRTEENTH_DOCTOR(SonicVariant.THIRTEENTH.getFloats(), 6, Material.BLAZE_ROD, ""),
    NINTH_DOCTOR(SonicVariant.NINTH.getFloats(), 7, Material.BLAZE_ROD, ""),
    TENTH_DOCTOR(SonicVariant.TENTH.getFloats(), 8, Material.BLAZE_ROD, ""),
    ELEVENTH_DOCTOR(SonicVariant.ELEVENTH.getFloats(), 9, Material.BLAZE_ROD, ""),
    FOURTEENTH_DOCTOR(SonicVariant.FOURTEENTH.getFloats(), 10, Material.BLAZE_ROD, ""),
    FIFTEENTH_DOCTOR(SonicVariant.FIFTEENTH.getFloats(), 11, Material.BLAZE_ROD, ""),
    WAR_DOCTOR(SonicVariant.WAR.getFloats(), 12, Material.BLAZE_ROD, ""),
    MASTER(SonicVariant.MASTER.getFloats(), 13, Material.BLAZE_ROD, ""),
    SARAH_JANE(SonicVariant.SARAH_JANE.getFloats(), 14, Material.BLAZE_ROD, ""),
    AMY_POND(SonicVariant.SONIC_PROBE.getFloats(), 15, Material.BLAZE_ROD, ""),
    RIVER_SONG(SonicVariant.RIVER_SONG.getFloats(), 16, Material.BLAZE_ROD, ""),
    MISSY(SonicVariant.UMBRELLA.getFloats(), 17, Material.BLAZE_ROD, ""),
    RANI(SonicVariant.RANI.getFloats(), 18, Material.BLAZE_ROD, ""),
    BRUSH_UPGRADE(CircuitVariant.BRUSH.getKey(), 26, Material.BOWL, ""),
    BIO_SCANNER_UPGRADE(CircuitVariant.BIO.getKey(), 27, Material.BOWL, ""),
    CONVERSION_UPGRADE(CircuitVariant.CONVERSION.getKey(), 28, Material.BOWL, ""),
    DIAMOND_UPGRADE(CircuitVariant.DIAMOND.getKey(), 29, Material.BOWL, ""),
    EMERALD_UPGRADE(CircuitVariant.EMERALD.getKey(), 30, Material.BOWL, ""),
    IGNITE_UPGRADE(CircuitVariant.IGNITE.getKey(), 31, Material.BOWL, ""),
    KNOCKBACK_UPGRADE(CircuitVariant.KNOCKBACK.getKey(), 32, Material.BOWL, ""),
    PAINTER_UPGRADE(CircuitVariant.PAINTER.getKey(), 33, Material.BOWL, ""),
    PICKUP_ARROWS_UPGRADE(CircuitVariant.PICKUP.getKey(), 34, Material.BOWL, ""),
    REDSTONE_UPGRADE(CircuitVariant.REDSTONE.getKey(), 35, Material.BOWL, ""),
    STANDARD_SONIC(GuiVariant.STANDARD_SONIC.getKey(), 36, Material.BOWL, ""),
    INSTRUCTIONS_1_OF_3(GuiVariant.INFO.getKey(), 38, Material.BOOK, "Select your Sonic Screwdriver~type from the top two rows.~Click on the upgrades you~want to add to the sonic."),
    INSTRUCTIONS_2_OF_3(GuiVariant.INFO.getKey(), 39, Material.BOOK, "You can reset the upgrades~by clicking the 'Standard' button.~The Artron cost for the~sonic is shown bottom left."),
    INSTRUCTIONS_3_OF_3(GuiVariant.INFO.getKey(), 40, Material.BOOK, "The final sonic result~is shown in the middle~of the bottom row."),
    SAVE_SETTINGS(GuiVariant.SAVE.getKey(), 43, Material.BOWL, "Click to save the current sonic.~No item will be generated!"),
    GENERATE_SONIC_SCREWDRIVER(GuiVariant.GENERATE_SONIC_SCREWDRIVER.getKey(), 44, Material.BOWL, "Click to generate a sonic~with the current settings."),
    ARTRON_COST(Whoniverse.ARTRON_BATTERY.getKey(), 45, Material.BOWL, ""),
    SONIC_SCREWDRIVER(SonicVariant.ELEVENTH.getFloats(), 49, Material.BLAZE_ROD, ""),
    CLOSE(GuiVariant.CLOSE.getKey(), 53, Material.BOWL, "Close the menu without~saving or generating.");

    private final List<Float> floats;
    private final NamespacedKey model;
    private final int slot;
    private final Material material;
    private final String lore;

    GUISonicGenerator(NamespacedKey model, int slot, Material material, String lore) {
        this.floats = null;
        this.model = model;
        this.slot = slot;
        this.material = material;
        this.lore = lore;
    }

    GUISonicGenerator(List<Float> floats, int slot, Material material, String lore) {
        this.floats = floats;
        this.model = null;
        this.slot = slot;
        this.material = material;
        this.lore = lore;
    }

    public List<Float> getFloats() {
        return floats;
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
