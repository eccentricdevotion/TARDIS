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

public enum GlowStickVariant {

    BLUE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "blue_glow_stick")),
    BLUE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "blue_glow_stick_active")),
    BROWN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "brown_glow_stick")),
    BROWN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "brown_glow_stick_active")),
    CYAN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "cyan_glow_stick")),
    CYAN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "cyan_glow_stick_active")),
    GREEN_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "green_glow_stick")),
    GREEN_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "green_glow_stick_active")),
    LIGHT_BLUE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "light_blue_glow_stick")),
    LIGHT_BLUE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "light_blue_glow_stick_active")),
    LIGHT_GRAY_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "light_gray_glow_stick")),
    LIGHT_GRAY_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "light_gray_glow_stick_active")),
    LIME_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "lime_glow_stick")),
    LIME_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "lime_glow_stick_active")),
    MAGENTA_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "magenta_glow_stick")),
    MAGENTA_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "magenta_glow_stick_active")),
    ORANGE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "orange_glow_stick")),
    ORANGE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "orange_glow_stick_active")),
    PINK_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "pink_glow_stick")),
    PINK_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "pink_glow_stick_active")),
    PURPLE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "purple_glow_stick")),
    PURPLE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "purple_glow_stick_active")),
    RED_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "red_glow_stick")),
    RED_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "red_glow_stick_active")),
    WHITE_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "white_glow_stick")),
    WHITE_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "white_glow_stick_active")),
    YELLOW_GLOW_STICK(new NamespacedKey(TARDIS.plugin, "yellow_glow_stick")),
    YELLOW_GLOW_STICK_ACTIVE(new NamespacedKey(TARDIS.plugin, "yellow_glow_stick_active"));

    private final NamespacedKey key;

    GlowStickVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
