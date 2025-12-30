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
package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

import java.util.List;

public enum SonicVariant {

    MARK1(new NamespacedKey(TARDIS.plugin, "sonic_mark1"), List.of(101f)),
    MARK2(new NamespacedKey(TARDIS.plugin, "sonic_mark2"), List.of(102f)),
    MARK3(new NamespacedKey(TARDIS.plugin, "sonic_mark3"), List.of(103f)),
    MARK4(new NamespacedKey(TARDIS.plugin, "sonic_mark4"), List.of(104f)),
    EIGHTH(new NamespacedKey(TARDIS.plugin, "sonic_eighth"), List.of(108f)),
    NINTH(new NamespacedKey(TARDIS.plugin, "sonic_ninth"), List.of(109f)),
    TENTH(new NamespacedKey(TARDIS.plugin, "sonic_tenth"), List.of(110f)),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "sonic_eleventh"), List.of(111f)),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "sonic_twelfth"), List.of(112f)),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "sonic_thirteenth"), List.of(113f)),
    FOURTEENTH(new NamespacedKey(TARDIS.plugin, "sonic_fourteenth"), List.of(114f)),
    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "sonic_fifteenth"), List.of(115f)),
    WAR(new NamespacedKey(TARDIS.plugin, "sonic_war"), List.of(120f)),
    MASTER(new NamespacedKey(TARDIS.plugin, "sonic_master"), List.of(121f)),
    SARAH_JANE(new NamespacedKey(TARDIS.plugin, "sonic_sarah_jane"), List.of(122f)),
    SONIC_PROBE(new NamespacedKey(TARDIS.plugin, "sonic_probe"), List.of(123f)),
    RANI(new NamespacedKey(TARDIS.plugin, "sonic_rani"), List.of(126f)),
    RIVER_SONG(new NamespacedKey(TARDIS.plugin, "sonic_river_song"), List.of(124f)),
    UMBRELLA(new NamespacedKey(TARDIS.plugin, "sonic_umbrella"), List.of(125f)),
    MARK1_ON(new NamespacedKey(TARDIS.plugin, "sonic_mark1_on"), List.of(201f)),
    MARK2_ON(new NamespacedKey(TARDIS.plugin, "sonic_mark2_on"), List.of(202f)),
    MARK3_ON(new NamespacedKey(TARDIS.plugin, "sonic_mark3_on"), List.of(203f)),
    MARK4_ON(new NamespacedKey(TARDIS.plugin, "sonic_mark4_on"), List.of(204f)),
    EIGHTH_ON(new NamespacedKey(TARDIS.plugin, "sonic_eighth_on"), List.of(208f)),
    NINTH_OPEN(new NamespacedKey(TARDIS.plugin, "sonic_ninth_open"), List.of(209f)),
    TENTH_OPEN(new NamespacedKey(TARDIS.plugin, "sonic_tenth_open"), List.of(210f)),
    ELEVENTH_OPEN(new NamespacedKey(TARDIS.plugin, "sonic_eleventh_open"), List.of(211f)),
    TWELFTH_ON(new NamespacedKey(TARDIS.plugin, "sonic_twelfth_on"), List.of(212f)),
    THIRTEENTH_ON(new NamespacedKey(TARDIS.plugin, "sonic_thirteenth_on"), List.of(213f)),
    FOURTEENTH_OPEN(new NamespacedKey(TARDIS.plugin, "sonic_fourteenth_open"), List.of(214f)),
    FIFTEENTH_ON(new NamespacedKey(TARDIS.plugin, "sonic_fifteenth_on"), List.of(215f)),
    WAR_ON(new NamespacedKey(TARDIS.plugin, "sonic_war_on"), List.of(220f)),
    MASTER_ON(new NamespacedKey(TARDIS.plugin, "sonic_master_on"), List.of(221f)),
    RANI_ON(new NamespacedKey(TARDIS.plugin, "sonic_rani_on"), List.of(226f)),
    RIVER_SONG_ON(new NamespacedKey(TARDIS.plugin, "sonic_river_song_on"), List.of(224f)),
    SARAH_JANE_ON(new NamespacedKey(TARDIS.plugin, "sonic_sarah_jane_on"), List.of(222f)),
    SONIC_PROBE_ON(new NamespacedKey(TARDIS.plugin, "sonic_sonic_probe_on"), List.of(223f)),
    UMBRELLA_ON(new NamespacedKey(TARDIS.plugin, "sonic_umbrella_on"), List.of(225f)),
    STANDARD_SONIC(new NamespacedKey(TARDIS.plugin, "button_standard_sonic"), List.of());

    private final NamespacedKey key;
    private final List<Float> floats;

    SonicVariant(NamespacedKey key, List<Float> floats) {
        this.key = key;
        this.floats = floats;
    }

    public static SonicVariant getByFloat(Float f) {
        for (SonicVariant variant : values()) {
            if (!variant.getFloats().isEmpty() && f.equals(variant.getFloats().getFirst())) {
                return variant;
            }
        }
        return ELEVENTH;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public List<Float> getFloats() {
        return floats;
    }
}
