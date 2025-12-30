/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.KeyVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUIKeyPreferences {

    // TARDIS Key Prefs Menu
    BRASS_YALE(KeyVariant.BRASS_YALE.getKey(), 0, "First & Sixth Doctors"),
    BRASS_PLAIN(KeyVariant.BRASS_PLAIN.getKey(), 1, "Second Doctor"),
    SPADE_SHAPED(KeyVariant.SPADE_SHAPED.getKey(), 2, "Third, Fourth & Eighth Doctors"),
    SILVER_YALE(KeyVariant.SILVER_YALE.getKey(), 3, "Fifth Doctor"),
    SEAL_OF_RASSILON(KeyVariant.SEAL_OF_RASSILON.getKey(), 4, "Seventh Doctor"),
    SILVER_VARIANT(KeyVariant.SILVER_VARIANT.getKey(), 5, "Ninth Doctor"),
    SILVER_PLAIN(KeyVariant.SILVER_PLAIN.getKey(), 6, "Tenth Doctor, Martha Jones & Donna Noble"),
    SILVER_NEW(KeyVariant.SILVER_NEW.getKey(), 7, "Eleventh Doctor & Clara Oswald"),
    SILVER_ERA(KeyVariant.SILVER_ERA.getKey(), 8, "Rose Tyler"),
    SILVER_STRING(KeyVariant.SILVER_STRING.getKey(), 10, "Sally Sparrow"),
    FILTER(KeyVariant.FILTER.getKey(), 12, "Tenth Doctor, Martha Jones & Jack Harkness"),
    BRASS_STRING(KeyVariant.BRASS_STRING.getKey(), 14, "Susan Foreman"),
    BROMLEY_GOLD(KeyVariant.BROMLEY_GOLD.getKey(), 16, "eccentric_nz"),
    DISPLAY_NAME_COLOUR(null, 19, Material.WHITE_WOOL, "Click to select"),
    INSTRUCTIONS(GuiVariant.INFO.getKey(), 22, Material.BOOK, "Put your TARDIS Key~in the bottom left most slot~and then click on the~key of your choice."),
    NAME(GuiVariant.INFO.getKey(), 23, Material.BOOK, "If you want to have~a coloured display name~click the wool block~to choose a colour."),
    CLOSE(GuiVariant.CLOSE.getKey(), 26, Material.BOWL, "");

    private final NamespacedKey model;
    private final int slot;
    private final Material material;
    private final String lore;

    GUIKeyPreferences(NamespacedKey model, int slot, Material material, String lore) {
        this.model = model;
        this.slot = slot;
        this.material = material;
        this.lore = lore;
    }

    GUIKeyPreferences(NamespacedKey model, int slot, String lore) {
        this.model = model;
        this.slot = slot;
        Material material;
        try {
            material = Material.valueOf(TARDIS.plugin.getConfig().getString("preferences.key"));
        } catch (IllegalArgumentException e) {
            material = Material.GOLD_NUGGET;
        }
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
        if (s.endsWith("ERA")) {
            return "Silver ERA";
        } else {
            return TARDISStringUtils.capitalise(s);
        }
    }

    public String getLore() {
        return lore;
    }
}
