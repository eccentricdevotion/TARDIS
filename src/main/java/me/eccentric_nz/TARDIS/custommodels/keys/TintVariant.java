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

public enum TintVariant {

    TINT_BLACK(new NamespacedKey(TARDIS.plugin, "light_tint_black")),
    TINT_BLUE(new NamespacedKey(TARDIS.plugin, "light_tint_blue")),
    TINT_BROWN(new NamespacedKey(TARDIS.plugin, "light_tint_brown")),
    TINT_CYAN(new NamespacedKey(TARDIS.plugin, "light_tint_cyan")),
    TINT_GRAY(new NamespacedKey(TARDIS.plugin, "light_tint_gray")),
    TINT_GREEN(new NamespacedKey(TARDIS.plugin, "light_tint_green")),
    TINT_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "light_tint_light_blue")),
    TINT_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "light_tint_light_gray")),
    TINT_LIME(new NamespacedKey(TARDIS.plugin, "light_tint_lime")),
    TINT_MAGENTA(new NamespacedKey(TARDIS.plugin, "light_tint_magenta")),
    TINT_ORANGE(new NamespacedKey(TARDIS.plugin, "light_tint_orange")),
    TINT_PINK(new NamespacedKey(TARDIS.plugin, "light_tint_pink")),
    TINT_PURPLE(new NamespacedKey(TARDIS.plugin, "light_tint_purple")),
    TINT_RED(new NamespacedKey(TARDIS.plugin, "light_tint_red")),
    TINT_WHITE(new NamespacedKey(TARDIS.plugin, "light_tint_white")),
    TINT_YELLOW(new NamespacedKey(TARDIS.plugin, "light_tint_yellow"));

    private final NamespacedKey key;

    TintVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
