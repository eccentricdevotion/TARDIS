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

public enum LightVariant {

    OFF(new NamespacedKey(TARDIS.plugin, "light_off")),
    ON(new NamespacedKey(TARDIS.plugin, "light_on")),
    CLOISTER(new NamespacedKey(TARDIS.plugin, "light_cloister")),
    VARIABLE(new NamespacedKey(TARDIS.plugin, "light_variable")),
    BLUE(new NamespacedKey(TARDIS.plugin, "light_blue")),
    GREEN(new NamespacedKey(TARDIS.plugin, "light_green")),
    ORANGE(new NamespacedKey(TARDIS.plugin, "light_orange")),
    PINK(new NamespacedKey(TARDIS.plugin, "light_pink")),
    PURPLE(new NamespacedKey(TARDIS.plugin, "light_purple")),
    YELLOW(new NamespacedKey(TARDIS.plugin, "light_yellow")),
    BLUE_OFF(new NamespacedKey(TARDIS.plugin, "light_blue_off")),
    GREEN_OFF(new NamespacedKey(TARDIS.plugin, "light_green_off")),
    ORANGE_OFF(new NamespacedKey(TARDIS.plugin, "light_orange_off")),
    PINK_OFF(new NamespacedKey(TARDIS.plugin, "light_pink_off")),
    PURPLE_OFF(new NamespacedKey(TARDIS.plugin, "light_purple_off")),
    YELLOW_OFF(new NamespacedKey(TARDIS.plugin, "light_yellow_off")),
    INTERIOR(new NamespacedKey(TARDIS.plugin, "lights_interior")),
    EXTERIOR(new NamespacedKey(TARDIS.plugin, "lights_exterior")),
    CONSOLE(new NamespacedKey(TARDIS.plugin, "lights_console")),
    BULB(new NamespacedKey(TARDIS.plugin, "light_bulb"));

    private final NamespacedKey key;

    LightVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
