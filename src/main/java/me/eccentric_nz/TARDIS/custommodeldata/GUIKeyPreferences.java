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

public enum GUIKeyPreferences {

    // TARDIS Key Prefs Menu
    BRASS_YALE(1, 0, Material.GOLD_NUGGET, "First & Sixth Doctors"),
    BRASS_PLAIN(2, 1, Material.GOLD_NUGGET, "Second Doctor"),
    SPADE_SHAPED(3, 2, Material.GOLD_NUGGET, "Third, Fourth & Eighth Doctors"),
    SILVER_YALE(4, 3, Material.GOLD_NUGGET, "Fifth Doctor"),
    SEAL_OF_RASSILON(5, 4, Material.GOLD_NUGGET, "Seventh Doctor"),
    SILVER_VARIANT(6, 5, Material.GOLD_NUGGET, "Ninth Doctor"),
    SILVER_PLAIN(7, 6, Material.GOLD_NUGGET, "Tenth Doctor, Martha Jones & Donna Noble"),
    SILVER_NEW(8, 7, Material.GOLD_NUGGET, "Eleventh Doctor & Clara Oswald"),
    SILVER_ERA(9, 8, Material.GOLD_NUGGET, "Rose Tyler"),
    SILVER_STRING(10, 10, Material.GOLD_NUGGET, "Sally Sparrow"),
    FILTER(11, 12, Material.GOLD_NUGGET, "Tenth Doctor, Martha Jones & Jack Harkness"),
    BRASS_STRING(12, 14, Material.GOLD_NUGGET, "Susan Foreman"),
    BROMLEY_GOLD(13, 16, Material.GOLD_NUGGET, "eccentric_nz"),
    INSTRUCTIONS(1, 22, Material.BOOK, "Put your TARDIS Key~in the bottom left most slot~and then click on the~key of your choice."),
    CLOSE(1, 26, Material.BOWL, "");

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final String lore;

    GUIKeyPreferences(int customModelData, int slot, Material material, String lore) {
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
