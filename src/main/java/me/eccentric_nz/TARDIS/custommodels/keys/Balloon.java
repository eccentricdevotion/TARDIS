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

public enum Balloon {

    WHITE_BALLOON(new NamespacedKey(TARDIS.plugin, "white_balloon")),
    ORANGE_BALLOON(new NamespacedKey(TARDIS.plugin, "orange_balloon")),
    MAGENTA_BALLOON(new NamespacedKey(TARDIS.plugin, "magenta_balloon")),
    LIGHT_BLUE_BALLOON(new NamespacedKey(TARDIS.plugin, "light_blue_balloon")),
    YELLOW_BALLOON(new NamespacedKey(TARDIS.plugin, "yellow_balloon")),
    LIME_BALLOON(new NamespacedKey(TARDIS.plugin, "lime_balloon")),
    PINK_BALLOON(new NamespacedKey(TARDIS.plugin, "pink_balloon")),
    GRAY_BALLOON(new NamespacedKey(TARDIS.plugin, "gray_balloon")),
    LIGHT_GRAY_BALLOON(new NamespacedKey(TARDIS.plugin, "light_gray_balloon")),
    CYAN_BALLOON(new NamespacedKey(TARDIS.plugin, "cyan_balloon")),
    PURPLE_BALLOON(new NamespacedKey(TARDIS.plugin, "purple_balloon")),
    BLUE_BALLOON(new NamespacedKey(TARDIS.plugin, "blue_balloon")),
    BROWN_BALLOON(new NamespacedKey(TARDIS.plugin, "brown_balloon")),
    GREEN_BALLOON(new NamespacedKey(TARDIS.plugin, "green_balloon")),
    RED_BALLOON(new NamespacedKey(TARDIS.plugin, "red_balloon")),
    BLACK_BALLOON(new NamespacedKey(TARDIS.plugin, "black_balloon"));

    private final NamespacedKey key;

    Balloon(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
