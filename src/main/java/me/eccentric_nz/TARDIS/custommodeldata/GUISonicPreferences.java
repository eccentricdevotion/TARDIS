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

public enum GUISonicPreferences {

    // Sonic Prefs Menu
    MARK_I(10000001, 0),
    MARK_II(10000002, 1),
    MARK_III(10000003, 2),
    MARK_IV(10000004, 3),
    EIGHTH_DOCTOR(10000008, 4),
    NINTH_DOCTOR(10000009, 5),
    TENTH_DOCTOR(10000010, 6),
    ELEVENTH_DOCTOR(10000011, 7),
    TWELFTH_DOCTOR(10000012, 8),
    THIRTEENTH_DOCTOR(10000013, 9),
    FOURTEENTH_DOCTOR(10000014, 10),
    FIFTEENTH_DOCTOR(10000015, 11),
    WAR_DOCTOR(10000085, 12),
    MASTER(10000032, 13),
    SARAH_JANE(10000033, 14),
    AMY_POND(10000034, 15),
    RIVER_SONG(10000031, 16),
    MISSY(10000035, 17),
    COLOUR(-1, 28),
    INSTRUCTIONS(1, 31, Material.BOOK),
    CLOSE(1, 35, Material.BOWL);

    private final int customModelData;
    private final int slot;
    private final Material material;

    GUISonicPreferences(int customModelData, int slot, Material material) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
    }

    GUISonicPreferences(int customModelData, int slot) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = Material.BLAZE_ROD;
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
}
