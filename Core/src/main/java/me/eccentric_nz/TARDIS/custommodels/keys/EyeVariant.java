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

public enum EyeVariant {

    SPHERE_0(new NamespacedKey(TARDIS.plugin, "sphere_0")),
    SPHERE_1(new NamespacedKey(TARDIS.plugin, "sphere_1")),
    SPHERE_2(new NamespacedKey(TARDIS.plugin, "sphere_2")),
    SPHERE_3(new NamespacedKey(TARDIS.plugin, "sphere_3")),
    SPHERE_4(new NamespacedKey(TARDIS.plugin, "sphere_4"));

    private final NamespacedKey key;

    EyeVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
