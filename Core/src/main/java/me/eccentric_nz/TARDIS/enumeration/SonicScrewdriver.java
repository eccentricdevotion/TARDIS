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
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.NamespacedKey;

import java.util.Locale;

public enum SonicScrewdriver {

    MARK1(SonicVariant.MARK1.getKey(), SonicVariant.MARK1_ON.getKey()),
    MARK2(SonicVariant.MARK2.getKey(), SonicVariant.MARK2_ON.getKey()),
    MARK3(SonicVariant.MARK3.getKey(), SonicVariant.MARK3_ON.getKey()),
    MARK4(SonicVariant.MARK4.getKey(), SonicVariant.MARK4_ON.getKey()),
    EIGHTH(SonicVariant.EIGHTH.getKey(), SonicVariant.EIGHTH_ON.getKey()),
    NINTH(SonicVariant.NINTH.getKey(), SonicVariant.NINTH_OPEN.getKey()),
    TENTH(SonicVariant.TENTH.getKey(), SonicVariant.TENTH_OPEN.getKey()),
    ELEVENTH(SonicVariant.ELEVENTH.getKey(), SonicVariant.ELEVENTH_OPEN.getKey()),
    TWELFTH(SonicVariant.TWELFTH.getKey(), SonicVariant.TWELFTH_ON.getKey()),
    THIRTEENTH(SonicVariant.THIRTEENTH.getKey(), SonicVariant.THIRTEENTH_ON.getKey()),
    FOURTEENTH(SonicVariant.FOURTEENTH.getKey(), SonicVariant.FOURTEENTH_OPEN.getKey()),
    FIFTEENTH(SonicVariant.FIFTEENTH.getKey(), SonicVariant.FIFTEENTH_ON.getKey()),
    RIVER_SONG(SonicVariant.RIVER_SONG.getKey(), SonicVariant.RIVER_SONG_ON.getKey()),
    MASTER(SonicVariant.MASTER.getKey(), SonicVariant.MASTER_ON.getKey()),
    SARAH_JANE(SonicVariant.SARAH_JANE.getKey(), SonicVariant.SARAH_JANE_ON.getKey()),
    SONIC_PROBE(SonicVariant.SONIC_PROBE.getKey(), SonicVariant.SONIC_PROBE_ON.getKey()),
    UMBRELLA(SonicVariant.UMBRELLA.getKey(), SonicVariant.UMBRELLA_ON.getKey()),
    WAR(SonicVariant.WAR.getKey(), SonicVariant.WAR_ON.getKey());

    private final NamespacedKey model;
    private final NamespacedKey active;

    SonicScrewdriver(NamespacedKey model, NamespacedKey active) {
        this.model = model;
        this.active = active;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public NamespacedKey getActive() {
        return active;
    }

    public String getName() {
        return TARDISStringUtils.sentenceCase(toString());
    }

    public static SonicScrewdriver getByKey(String key) {
        String[] split = key.split("/");
        if (split.length > 1) {
            String model = split[1].toUpperCase(Locale.ROOT);
            if (model.endsWith("_ON")) {
                model = model.substring(0, model.length() - 3);
            }
            if (model.endsWith("_OPEN")) {
                model = model.substring(0, model.length() - 5);
            }
            return SonicScrewdriver.valueOf(model);
        }
        return ELEVENTH;
    }
}
