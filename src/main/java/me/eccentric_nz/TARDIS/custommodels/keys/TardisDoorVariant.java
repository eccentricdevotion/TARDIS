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

public enum TardisDoorVariant {

    TARDIS_DOOR_CLOSED(new NamespacedKey(TARDIS.plugin, "tardis_door_closed")),
    TARDIS_DOOR_0(new NamespacedKey(TARDIS.plugin, "tardis_door_0")),
    TARDIS_DOOR_1(new NamespacedKey(TARDIS.plugin, "tardis_door_1")),
    TARDIS_DOOR_2(new NamespacedKey(TARDIS.plugin, "tardis_door_2")),
    TARDIS_DOOR_OPEN(new NamespacedKey(TARDIS.plugin, "tardis_door_open")),
    TARDIS_DOOR_EXTRA(new NamespacedKey(TARDIS.plugin, "tardis_door_extra"));

    private final NamespacedKey key;

    TardisDoorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
