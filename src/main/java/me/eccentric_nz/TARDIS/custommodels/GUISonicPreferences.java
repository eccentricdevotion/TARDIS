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

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

public enum GUISonicPreferences {

    // Sonic Prefs Menu
    MARK_I(SonicVariant.MARK1.getKey(), 0, SonicVariant.MARK1.getFloats()),
    MARK_II(SonicVariant.MARK2.getKey(), 1, SonicVariant.MARK2.getFloats()),
    MARK_III(SonicVariant.MARK3.getKey(), 2, SonicVariant.MARK3.getFloats()),
    MARK_IV(SonicVariant.MARK4.getKey(), 3, SonicVariant.MARK4.getFloats()),
    EIGHTH_DOCTOR(SonicVariant.EIGHTH.getKey(), 4, SonicVariant.EIGHTH.getFloats()),
    NINTH_DOCTOR(SonicVariant.NINTH.getKey(), 5, SonicVariant.NINTH.getFloats()),
    TENTH_DOCTOR(SonicVariant.TENTH.getKey(), 6, SonicVariant.TENTH.getFloats()),
    ELEVENTH_DOCTOR(SonicVariant.ELEVENTH.getKey(), 7, SonicVariant.ELEVENTH.getFloats()),
    TWELFTH_DOCTOR(SonicVariant.TWELFTH.getKey(), 8, SonicVariant.TWELFTH.getFloats()),
    THIRTEENTH_DOCTOR(SonicVariant.THIRTEENTH.getKey(), 9, SonicVariant.THIRTEENTH.getFloats()),
    FOURTEENTH_DOCTOR(SonicVariant.FOURTEENTH.getKey(), 10, SonicVariant.FOURTEENTH.getFloats()),
    FIFTEENTH_DOCTOR(SonicVariant.FIFTEENTH.getKey(), 11, SonicVariant.FIFTEENTH.getFloats()),
    WAR_DOCTOR(SonicVariant.WAR.getKey(), 12, SonicVariant.WAR.getFloats()),
    MASTER(SonicVariant.MASTER.getKey(), 13, SonicVariant.MASTER.getFloats()),
    SARAH_JANE(SonicVariant.SARAH_JANE.getKey(), 14, SonicVariant.SARAH_JANE.getFloats()),
    AMY_POND(SonicVariant.SONIC_PROBE.getKey(), 15, SonicVariant.SONIC_PROBE.getFloats()),
    RIVER_SONG(SonicVariant.RIVER_SONG.getKey(), 16, SonicVariant.RIVER_SONG.getFloats()),
    MISSY(SonicVariant.UMBRELLA.getKey(), 17, SonicVariant.UMBRELLA.getFloats()),
    RANI(SonicVariant.RANI.getKey(), 18, SonicVariant.RANI.getFloats()),
    COLOUR(null, 28, List.of()),
    INSTRUCTIONS(GuiVariant.INFO.getKey(), 31, Material.BOOK),
    CLOSE(GuiVariant.CLOSE.getKey(), 35, Material.BOWL);

    private final NamespacedKey model;
    private final int slot;
    private final Material material;
    private final List<Float> floats;

    GUISonicPreferences(NamespacedKey model, int slot, Material material) {
        this.model = model;
        this.slot = slot;
        this.material = material;
        this.floats = new ArrayList<>();
    }

    GUISonicPreferences(NamespacedKey model, int slot, List<Float> floats) {
        this.model = model;
        this.slot = slot;
        this.material = Material.BLAZE_ROD;
        this.floats = floats;
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

    public List<Float> getFloats() {
        return floats;
    }

    public String getName() {
        String s = toString();
        return TARDISStringUtils.capitalise(s);
    }
}
