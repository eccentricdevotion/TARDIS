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

public enum Sparkler {

    SPARKLER_ORANGE(new NamespacedKey(TARDIS.plugin, "sparkler_orange")),
    SPARKLER_BLUE(new NamespacedKey(TARDIS.plugin, "sparkler_blue")),
    SPARKLER_GREEN(new NamespacedKey(TARDIS.plugin, "sparkler_green")),
    SPARKLER_PURPLE(new NamespacedKey(TARDIS.plugin, "sparkler_purple")),
    SPARKLER_RED(new NamespacedKey(TARDIS.plugin, "sparkler_red")),
    SPARKLER_ORANGE_LIT(new NamespacedKey(TARDIS.plugin, "sparkler_orange_lit")),
    SPARKLER_BLUE_LIT(new NamespacedKey(TARDIS.plugin, "sparkler_blue_lit")),
    SPARKLER_GREEN_LIT(new NamespacedKey(TARDIS.plugin, "sparkler_green_lit")),
    SPARKLER_PURPLE_LIT(new NamespacedKey(TARDIS.plugin, "sparkler_purple_lit")),
    SPARKLER_RED_LIT(new NamespacedKey(TARDIS.plugin, "sparkler_red_lit"));

    private final NamespacedKey key;

    Sparkler(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
