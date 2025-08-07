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
package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum MireVariant {

    BUTTON_THE_MIRE(new NamespacedKey(TARDIS.plugin, "button_the_mire")),
    THE_MIRE_HEAD(new NamespacedKey(TARDIS.plugin, "the_mire_head")),
    THE_MIRE_HELMETLESS(new NamespacedKey(TARDIS.plugin, "the_mire_helmetless")),
    MIRE_RIGHT_ARM(new NamespacedKey(TARDIS.plugin, "mire_right_arm")),
    MIRE_LEFT_ARM(new NamespacedKey(TARDIS.plugin, "mire_left_arm")),
    THE_MIRE_STATIC(new NamespacedKey(TARDIS.plugin, "the_mire_static"));

    private final NamespacedKey key;

    MireVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

