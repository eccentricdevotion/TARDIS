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

import me.eccentric_nz.TARDIS.custommodeldata.keys.BlazeRod;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Book;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public enum GUISonicPreferences {

    // Sonic Prefs Menu
    MARK_I(BlazeRod.MARK1.getKey(), 0),
    MARK_II(BlazeRod.MARK2.getKey(), 1),
    MARK_III(BlazeRod.MARK3.getKey(), 2),
    MARK_IV(BlazeRod.MARK4.getKey(), 3),
    EIGHTH_DOCTOR(BlazeRod.EIGHTH.getKey(), 4),
    NINTH_DOCTOR(BlazeRod.NINTH.getKey(), 5),
    TENTH_DOCTOR(BlazeRod.TENTH.getKey(), 6),
    ELEVENTH_DOCTOR(BlazeRod.ELEVENTH.getKey(), 7),
    TWELFTH_DOCTOR(BlazeRod.TWELFTH.getKey(), 8),
    THIRTEENTH_DOCTOR(BlazeRod.THIRTEENTH.getKey(), 9),
    FOURTEENTH_DOCTOR(BlazeRod.FOURTEENTH.getKey(), 10),
    FIFTEENTH_DOCTOR(BlazeRod.FIFTEENTH.getKey(), 11),
    WAR_DOCTOR(BlazeRod.WAR.getKey(), 12),
    MASTER(BlazeRod.MASTER.getKey(), 13),
    SARAH_JANE(BlazeRod.SARAH_JANE.getKey(), 14),
    AMY_POND(BlazeRod.SONIC_PROBE.getKey(), 15),
    RIVER_SONG(BlazeRod.RIVER_SONG.getKey(), 16),
    MISSY(BlazeRod.UMBRELLA.getKey(), 17),
    COLOUR(null, 28),
    INSTRUCTIONS(Book.INFO.getKey(), 31, Material.BOOK),
    CLOSE(Bowl.CLOSE.getKey(), 35, Material.BOWL);

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
