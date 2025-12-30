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

public enum DalekVariant {

    BUTTON_DALEK(new NamespacedKey(TARDIS.plugin, "button_dalek")),
    DALEK_BOW(new NamespacedKey(TARDIS.plugin, "dalek_bow")),
    DALEK_HEAD(new NamespacedKey(TARDIS.plugin, "dalek_head")),
    DALEK_BRASS(new NamespacedKey(TARDIS.plugin, "dalek_brass")),
    DALEK_WHITE(new NamespacedKey(TARDIS.plugin, "dalek_white")),
    DALEK_ORANGE(new NamespacedKey(TARDIS.plugin, "dalek_orange")),
    DALEK_MAGENTA(new NamespacedKey(TARDIS.plugin, "dalek_magenta")),
    DALEK_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "dalek_light_blue")),
    DALEK_YELLOW(new NamespacedKey(TARDIS.plugin, "dalek_yellow")),
    DALEK_LIME(new NamespacedKey(TARDIS.plugin, "dalek_lime")),
    DALEK_PINK(new NamespacedKey(TARDIS.plugin, "dalek_pink")),
    DALEK_GRAY(new NamespacedKey(TARDIS.plugin, "dalek_gray")),
    DALEK_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "dalek_light_gray")),
    DALEK_CYAN(new NamespacedKey(TARDIS.plugin, "dalek_cyan")),
    DALEK_PURPLE(new NamespacedKey(TARDIS.plugin, "dalek_purple")),
    DALEK_BLUE(new NamespacedKey(TARDIS.plugin, "dalek_blue")),
    DALEK_BROWN(new NamespacedKey(TARDIS.plugin, "dalek_brown")),
    DALEK_GREEN(new NamespacedKey(TARDIS.plugin, "dalek_green")),
    DALEK_RED(new NamespacedKey(TARDIS.plugin, "dalek_red")),
    DALEK_BLACK(new NamespacedKey(TARDIS.plugin, "dalek_black")),
    DALEK_OVERLAY(new NamespacedKey(TARDIS.plugin, "item/monster/dalek/dalek_overlay"));

    private final NamespacedKey key;

    DalekVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
