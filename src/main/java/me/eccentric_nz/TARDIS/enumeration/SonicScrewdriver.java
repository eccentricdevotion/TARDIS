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
package me.eccentric_nz.TARDIS.enumeration;

import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;

import java.util.List;
import java.util.Locale;

public enum SonicScrewdriver {

    MARK1(SonicVariant.MARK1.getFloats(), SonicVariant.MARK1_ON.getFloats()),
    MARK2(SonicVariant.MARK2.getFloats(), SonicVariant.MARK2_ON.getFloats()),
    MARK3(SonicVariant.MARK3.getFloats(), SonicVariant.MARK3_ON.getFloats()),
    MARK4(SonicVariant.MARK4.getFloats(), SonicVariant.MARK4_ON.getFloats()),
    EIGHTH(SonicVariant.EIGHTH.getFloats(), SonicVariant.EIGHTH_ON.getFloats()),
    NINTH(SonicVariant.NINTH.getFloats(), SonicVariant.NINTH_OPEN.getFloats()),
    TENTH(SonicVariant.TENTH.getFloats(), SonicVariant.TENTH_OPEN.getFloats()),
    ELEVENTH(SonicVariant.ELEVENTH.getFloats(), SonicVariant.ELEVENTH_OPEN.getFloats()),
    TWELFTH(SonicVariant.TWELFTH.getFloats(), SonicVariant.TWELFTH_ON.getFloats()),
    THIRTEENTH(SonicVariant.THIRTEENTH.getFloats(), SonicVariant.THIRTEENTH_ON.getFloats()),
    FOURTEENTH(SonicVariant.FOURTEENTH.getFloats(), SonicVariant.FOURTEENTH_OPEN.getFloats()),
    FIFTEENTH(SonicVariant.FIFTEENTH.getFloats(), SonicVariant.FIFTEENTH_ON.getFloats()),
    RANI(SonicVariant.RANI.getFloats(), SonicVariant.RANI_ON.getFloats()),
    RIVER_SONG(SonicVariant.RIVER_SONG.getFloats(), SonicVariant.RIVER_SONG_ON.getFloats()),
    MASTER(SonicVariant.MASTER.getFloats(), SonicVariant.MASTER_ON.getFloats()),
    SARAH_JANE(SonicVariant.SARAH_JANE.getFloats(), SonicVariant.SARAH_JANE_ON.getFloats()),
    SONIC_PROBE(SonicVariant.SONIC_PROBE.getFloats(), SonicVariant.SONIC_PROBE_ON.getFloats()),
    UMBRELLA(SonicVariant.UMBRELLA.getFloats(), SonicVariant.UMBRELLA_ON.getFloats()),
    WAR(SonicVariant.WAR.getFloats(), SonicVariant.WAR_ON.getFloats());

    private final List<Float> model;
    private final List<Float> active;

    SonicScrewdriver(List<Float> model, List<Float> active) {
        this.model = model;
        this.active = active;
    }

    public static SonicScrewdriver getByKey(String key) {
        String[] split = key.split("_");
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

    public static SonicScrewdriver getByFloat(float f) {
        for (SonicScrewdriver variant: SonicScrewdriver.values()) {
            if (f == variant.getModel().getFirst()) {
                return variant;
            }
        }
        return ELEVENTH;
    }

    public List<Float> getModel() {
        return model;
    }

    public List<Float> getActive() {
        return active;
    }

    public String getName() {
        return TARDISStringUtils.sentenceCase(toString());
    }
}
