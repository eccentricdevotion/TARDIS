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

public enum EmptyChildVariant {

    BUTTON_EMPTY_CHILD(new NamespacedKey(TARDIS.plugin, "button_empty_child")),
    EMPTY_CHILD_HEAD(new NamespacedKey(TARDIS.plugin, "empty_child_head")),
    EMPTY_CHILD_STATIC(new NamespacedKey(TARDIS.plugin, "empty_child_static")),
    EMPTY_CHILD_MASK(new NamespacedKey(TARDIS.plugin, "empty_child_player_mask")),
    EMPTY_CHILD_OVERLAY(new NamespacedKey(TARDIS.plugin, "item/monster/empty_child/empty_child_overlay"));

    private final NamespacedKey key;

    EmptyChildVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

