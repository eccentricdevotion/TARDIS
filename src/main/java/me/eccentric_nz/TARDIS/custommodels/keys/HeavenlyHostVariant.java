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

public enum HeavenlyHostVariant {

    BUTTON_HEAVENLY_HOST(new NamespacedKey(TARDIS.plugin, "button_heavenly_host")),
    HEAVENLY_HOST_HEAD(new NamespacedKey(TARDIS.plugin, "heavenly_host_head")),
    HEAVENLY_HOST_FEATURES(new NamespacedKey(TARDIS.plugin, "heavenly_host_features")),
    HEAVENLY_HOST_STATIC(new NamespacedKey(TARDIS.plugin, "heavenly_host_static"));

    private final NamespacedKey key;

    HeavenlyHostVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
