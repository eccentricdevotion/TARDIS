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

public enum BoneDoorVariant {

    BONE_DOOR_CLOSED(new NamespacedKey(TARDIS.plugin, "bone_door_closed")),
    BONE_DOOR_0(new NamespacedKey(TARDIS.plugin, "bone_door_0")),
    BONE_DOOR_1(new NamespacedKey(TARDIS.plugin, "bone_door_1")),
    BONE_DOOR_2(new NamespacedKey(TARDIS.plugin, "bone_door_2")),
    BONE_DOOR_3(new NamespacedKey(TARDIS.plugin, "bone_door_3")),
    BONE_DOOR_4(new NamespacedKey(TARDIS.plugin, "bone_door_4")),
    BONE_DOOR_OPEN(new NamespacedKey(TARDIS.plugin, "bone_door_open"));

    private final NamespacedKey key;

    BoneDoorVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
