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

public enum SystemTreeVariant {

    BOTH_DOWN(new NamespacedKey(TARDIS.plugin, "sys_both_down")),
    LEFT_DOWN(new NamespacedKey(TARDIS.plugin, "sys_left_down")),
    HORIZONTAL(new NamespacedKey(TARDIS.plugin, "sys_horizontal")),
    RIGHT_DOWN(new NamespacedKey(TARDIS.plugin, "sys_right_down")),
    BLANK(new NamespacedKey(TARDIS.plugin, "sys_blank")),
    CLOSE(new NamespacedKey(TARDIS.plugin, "sys_close")),
    VERTICAL(new NamespacedKey(TARDIS.plugin, "sys_vertical")),
    LOCKED_TREE(new NamespacedKey(TARDIS.plugin, "sys_locked_tree")),
    LOCKED_BRANCH_DEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_locked_branch_dependent")),
    LOCKED_BRANCH_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_locked_branch_independent")),
    LOCKED_DOWN(new NamespacedKey(TARDIS.plugin, "sys_locked_down")),
    LOCKED_END(new NamespacedKey(TARDIS.plugin, "sys_locked_end")),
    LOCKED_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_locked_independent")),
    UNLOCKED_TREE(new NamespacedKey(TARDIS.plugin, "sys_unlocked_tree")),
    UNLOCKED_BRANCH_DEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_unlocked_branch_dependent")),
    UNLOCKED_BRANCH_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_unlocked_branch_independent")),
    UNLOCKED_DOWN(new NamespacedKey(TARDIS.plugin, "sys_unlocked_down")),
    UNLOCKED_END(new NamespacedKey(TARDIS.plugin, "sys_unlocked_end")),
    UNLOCKED_INDEPENDENT(new NamespacedKey(TARDIS.plugin, "sys_unlocked_independent"));

    private final NamespacedKey key;

    SystemTreeVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
