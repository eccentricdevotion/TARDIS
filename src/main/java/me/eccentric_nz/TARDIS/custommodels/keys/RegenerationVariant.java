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

public enum RegenerationVariant {

    FIRST(new NamespacedKey(TARDIS.plugin, "regeneration_first")),
    SECOND(new NamespacedKey(TARDIS.plugin, "regeneration_second")),
    THIRD(new NamespacedKey(TARDIS.plugin, "regeneration_third")),
    FOURTH(new NamespacedKey(TARDIS.plugin, "regeneration_fourth")),
    FIFTH(new NamespacedKey(TARDIS.plugin, "regeneration_fifth")),
    SIXTH(new NamespacedKey(TARDIS.plugin, "regeneration_sixth")),
    SEVENTH(new NamespacedKey(TARDIS.plugin, "regeneration_seventh")),
    EIGHTH(new NamespacedKey(TARDIS.plugin, "regeneration_eighth")),
    NINTH(new NamespacedKey(TARDIS.plugin, "regeneration_ninth")),
    TENTH(new NamespacedKey(TARDIS.plugin, "regeneration_tenth")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "regeneration_eleventh")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "regeneration_twelfth")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "regeneration_thirteenth")),
    FOURTEENTH(new NamespacedKey(TARDIS.plugin, "regeneration_fourteenth")),
    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "regeneration_fifteenth")),
    WAR(new NamespacedKey(TARDIS.plugin, "regeneration_war")),
    RASSILON(new NamespacedKey(TARDIS.plugin, "regeneration_rassilon"));

    private final NamespacedKey key;

    RegenerationVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
