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

public enum SmilerVariant {

    BUTTON_SMILER(new NamespacedKey(TARDIS.plugin, "button_smiler")),
    SMILER_STATIC(new NamespacedKey(TARDIS.plugin, "smiler_static")),
    SMILER_HEAD(new NamespacedKey(TARDIS.plugin, "smiler_head")),
    SMILER_ANGRY_HEAD(new NamespacedKey(TARDIS.plugin, "smiler_angry_head"));

    private final NamespacedKey key;

    SmilerVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
