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

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUISonicPreferences {

    // Sonic Prefs Menu
    MARK_I(SonicVariant.MARK1.getKey(), 0),
    MARK_II(SonicVariant.MARK2.getKey(), 1),
    MARK_III(SonicVariant.MARK3.getKey(), 2),
    MARK_IV(SonicVariant.MARK4.getKey(), 3),
    EIGHTH_DOCTOR(SonicVariant.EIGHTH.getKey(), 4),
    NINTH_DOCTOR(SonicVariant.NINTH.getKey(), 5),
    TENTH_DOCTOR(SonicVariant.TENTH.getKey(), 6),
    ELEVENTH_DOCTOR(SonicVariant.ELEVENTH.getKey(), 7),
    TWELFTH_DOCTOR(SonicVariant.TWELFTH.getKey(), 8),
    THIRTEENTH_DOCTOR(SonicVariant.THIRTEENTH.getKey(), 9),
    FOURTEENTH_DOCTOR(SonicVariant.FOURTEENTH.getKey(), 10),
    FIFTEENTH_DOCTOR(SonicVariant.FIFTEENTH.getKey(), 11),
    WAR_DOCTOR(SonicVariant.WAR.getKey(), 12),
    MASTER(SonicVariant.MASTER.getKey(), 13),
    SARAH_JANE(SonicVariant.SARAH_JANE.getKey(), 14),
    AMY_POND(SonicVariant.SONIC_PROBE.getKey(), 15),
    RIVER_SONG(SonicVariant.RIVER_SONG.getKey(), 16),
    MISSY(SonicVariant.UMBRELLA.getKey(), 17),
    COLOUR(null, 28),
    INSTRUCTIONS(GuiVariant.INFO.getKey(), 31, Material.BOOK),
    CLOSE(GuiVariant.CLOSE.getKey(), 35, Material.BOWL);

    private final NamespacedKey model;
    private final int slot;
    private final Material material;

    GUISonicPreferences(NamespacedKey model, int slot, Material material) {
        this.model = model;
        this.slot = slot;
        this.material = material;
    }

    GUISonicPreferences(NamespacedKey model, int slot) {
        this.model = model;
        this.slot = slot;
        this.material = Material.BLAZE_ROD;
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
}
