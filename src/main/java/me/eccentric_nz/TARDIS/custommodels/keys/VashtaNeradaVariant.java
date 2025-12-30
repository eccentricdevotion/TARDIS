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

public enum VashtaNeradaVariant {

    BUTTON_VASHTA_NERADA(new NamespacedKey(TARDIS.plugin, "button_vashta_nerada")),
    VASHTA_NERADA_HEAD(new NamespacedKey(TARDIS.plugin, "vashta_nerada_head")),
    VASHTA_NERADA_STATIC(new NamespacedKey(TARDIS.plugin, "vashta_nerada_static"));

    private final NamespacedKey key;

    VashtaNeradaVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

