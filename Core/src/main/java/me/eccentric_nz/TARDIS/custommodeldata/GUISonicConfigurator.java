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

import me.eccentric_nz.TARDIS.custommodeldata.keys.Book;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Wool;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUISonicConfigurator {

    // Sonic Configurator
    BIO_SCANNER_UPGRADE(Bowl.BIO.getKey(), 0, Material.BOWL, ""),
    DIAMOND_UPGRADE(Bowl.DIAMOND.getKey(), 1, Material.BOWL, ""),
    EMERALD_UPGRADE(Bowl.EMERALD.getKey(), 2, Material.BOWL, ""),
    REDSTONE_UPGRADE(Bowl.REDSTONE.getKey(), 3, Material.BOWL, ""),
    PAINTER_UPGRADE(Bowl.PAINTER.getKey(), 4, Material.BOWL, ""),
    IGNITE_UPGRADE(Bowl.IGNITE.getKey(), 5, Material.BOWL, ""),
    PICKUP_ARROWS_UPGRADE(Bowl.PICKUP.getKey(), 6, Material.BOWL, ""),
    KNOCKBACK_UPGRADE(Bowl.KNOCKBACK.getKey(), 7, Material.BOWL, ""),
    BRUSH_UPGRADE(Bowl.BRUSH.getKey(), 8, Material.BOWL, ""),
    CONVERSION_UPGRADE(Bowl.CONVERSION.getKey(), 18, Material.BOWL, ""),
    INSTRUCTIONS_1_OF_2(Book.INFO.getKey(), 50, Material.BOOK, "Place your sonic screwdriver~in the bottom left slot.~Click on the upgrades you~want to enable/disable."),
    INSTRUCTIONS_2_OF_2(Book.INFO.getKey(), 51, Material.BOOK, "Click the 'Save' button~to apply your selections.~Click 'Close' to exit~without saving."),
    NOT_UPGRADED(Wool.NOT_UPGRADED.getKey(), -1, Material.GRAY_WOOL, ""),
    ENABLED(Wool.ENABLED.getKey(), -1, Material.LIME_WOOL, ""),
    DISABLED(Wool.DISABLED.getKey(), -1, Material.RED_WOOL, ""),
    PLACE_SONIC(Wool.PLACE.getKey(), -1, Material.GRAY_WOOL, ""),
    WAITING(Wool.WAITING.getKey(), -1, Material.GRAY_WOOL, ""),
    SAVE(Bowl.SAVE.getKey(), 52, Material.BOWL, "Click to save the current configuration."),
    CLOSE(Bowl.CLOSE.getKey(), 53, Material.BOWL, "Close the menu~without saving.");

    private final NamespacedKey model;
    private final int slot;
    private final Material material;
    private final String lore;

    GUISonicConfigurator(NamespacedKey model, int slot, Material material, String lore) {
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
