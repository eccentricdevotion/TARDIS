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

public enum SilentVariant {

    BUTTON_SILENT(new NamespacedKey(TARDIS.plugin, "button_silent")),
    SILENT(new NamespacedKey(TARDIS.plugin, "silent")),
    SILENT_MOUTH_CLOSED(new NamespacedKey(TARDIS.plugin, "silent_mouth_closed")),
    SILENT_MOUTH_OPEN(new NamespacedKey(TARDIS.plugin, "silent_mouth_open")),
    SILENT_BEAMING(new NamespacedKey(TARDIS.plugin, "silent_beaming")),
    SILENT_STATIC(new NamespacedKey(TARDIS.plugin, "silent_static")),
    SILENT_HEAD(new NamespacedKey(TARDIS.plugin, "silent_head")),
    SILENCE_HAND(new NamespacedKey(TARDIS.plugin, "silent_hand")),
    SILENCE_OFFHAND(new NamespacedKey(TARDIS.plugin, "silent_offhand"));

    private final NamespacedKey key;

    SilentVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

