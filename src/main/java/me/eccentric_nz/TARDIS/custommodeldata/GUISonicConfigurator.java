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
package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

public enum GUISonicConfigurator {

    // Sonic Configurator
    BIO_SCANNER_UPGRADE(9, 0, Material.BOWL, ""),
    DIAMOND_UPGRADE(50, 1, Material.BOWL, ""),
    EMERALD_UPGRADE(51, 2, Material.BOWL, ""),
    REDSTONE_UPGRADE(71, 3, Material.BOWL, ""),
    PAINTER_UPGRADE(67, 4, Material.BOWL, ""),
    IGNITE_UPGRADE(56, 5, Material.BOWL, ""),
    PICKUP_ARROWS_UPGRADE(68, 6, Material.BOWL, ""),
    KNOCKBACK_UPGRADE(134, 7, Material.BOWL, ""),
    BRUSH_UPGRADE(150, 8, Material.BOWL, ""),
    CONVERSION_UPGRADE(154, 18, Material.BOWL, ""),
    INSTRUCTIONS_1_OF_2(1, 41, Material.BOOK, "Place your sonic screwdriver~in the bottom left slot.~Click on the upgrades you~want to enable/disable."),
    INSTRUCTIONS_2_OF_2(1, 42, Material.BOOK, "Click the 'Save' button~to apply your selections.~Click 'Close' to exit~without saving."),
    NOT_UPGRADED(1, -1, Material.GRAY_WOOL, ""),
    ENABLED(2, -1, Material.LIME_WOOL, ""),
    DISABLED(2, -1, Material.RED_WOOL, ""),
    PLACE_SONIC(2, -1, Material.GRAY_WOOL, ""),
    WAITING(3, -1, Material.GRAY_WOOL, ""),
    SAVE(74, 43, Material.BOWL, "Click to save the current configuration."),
    CLOSE(1, 44, Material.BOWL, "Close the menu~without saving.");

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String lore;

    GUISonicConfigurator(int customModelData, int slot, Material material, String lore) {
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
