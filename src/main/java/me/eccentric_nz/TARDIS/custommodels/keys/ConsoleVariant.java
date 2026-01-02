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

public enum ConsoleVariant {

    CONSOLE_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "console_light_gray")),
    CONSOLE_GRAY(new NamespacedKey(TARDIS.plugin, "console_gray")),
    CONSOLE_BLACK(new NamespacedKey(TARDIS.plugin, "console_black")),
    CONSOLE_WHITE(new NamespacedKey(TARDIS.plugin, "console_white")),
    CONSOLE_RED(new NamespacedKey(TARDIS.plugin, "console_red")),
    CONSOLE_ORANGE(new NamespacedKey(TARDIS.plugin, "console_orange")),
    CONSOLE_YELLOW(new NamespacedKey(TARDIS.plugin, "console_yellow")),
    CONSOLE_LIME(new NamespacedKey(TARDIS.plugin, "console_lime")),
    CONSOLE_GREEN(new NamespacedKey(TARDIS.plugin, "console_green")),
    CONSOLE_CYAN(new NamespacedKey(TARDIS.plugin, "console_cyan")),
    CONSOLE_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "console_light_blue")),
    CONSOLE_BLUE(new NamespacedKey(TARDIS.plugin, "console_blue")),
    CONSOLE_PURPLE(new NamespacedKey(TARDIS.plugin, "console_purple")),
    CONSOLE_PINK(new NamespacedKey(TARDIS.plugin, "console_pink")),
    CONSOLE_MAGENTA(new NamespacedKey(TARDIS.plugin, "console_magenta")),
    CONSOLE_BROWN(new NamespacedKey(TARDIS.plugin, "console_brown")),
    CONSOLE_RUSTIC(new NamespacedKey(TARDIS.plugin, "console_rustic"));

    private final NamespacedKey key;

    ConsoleVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}

