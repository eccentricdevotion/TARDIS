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

public enum Wool {

    BLANK(new NamespacedKey(TARDIS.plugin, "map_blank")),
    BLUE_BOX(new NamespacedKey(TARDIS.plugin, "blue_box")),
    THE_MOMENT(new NamespacedKey(TARDIS.plugin, "the_moment")),
    UP(new NamespacedKey(TARDIS.plugin, "button_up")),
    DOWN(new NamespacedKey(TARDIS.plugin, "button_down")),
    LEFT(new NamespacedKey(TARDIS.plugin, "button_left")),
    RIGHT(new NamespacedKey(TARDIS.plugin, "button_right")),
    NOT_UPGRADED(new NamespacedKey(TARDIS.plugin, "sonic_not_upgraded")),
    PLACE(new NamespacedKey(TARDIS.plugin, "sonic_place")),
    WAITING(new NamespacedKey(TARDIS.plugin, "sonic_waiting")),
    COG(new NamespacedKey(TARDIS.plugin, "cog")),
    CLASSIC(new NamespacedKey(TARDIS.plugin, "light_classic")),
    TENTH(new NamespacedKey(TARDIS.plugin, "light_tenth")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "light_eleventh")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "light_twelfth")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "light_thirteenth")),
    CLASSIC_OFFSET(new NamespacedKey(TARDIS.plugin, "light_classic_offset")),
    SCROLL_RIGHT(new NamespacedKey(TARDIS.plugin, "map_scroll_right")),
    ENABLED(new NamespacedKey(TARDIS.plugin, "sonic_enabled")),
    HEXAGON(new NamespacedKey(TARDIS.plugin, "hexagon")),
    RECONFIGURE(new NamespacedKey(TARDIS.plugin, "map_reconfigure")),
    RELOCATOR(new NamespacedKey(TARDIS.plugin, "map_relocator")),
    SCROLL_LEFT(new NamespacedKey(TARDIS.plugin, "map_scroll_left")),
    DISABLED(new NamespacedKey(TARDIS.plugin, "sonic_disabled")),
    LEVEL_BOTTOM(new NamespacedKey(TARDIS.plugin, "map_level_bottom")),
    LEVEL_MAIN(new NamespacedKey(TARDIS.plugin, "map_level_main")),
    LEVEL_TOP(new NamespacedKey(TARDIS.plugin, "map_level_top")),
    ROUNDEL(new NamespacedKey(TARDIS.plugin, "roundel")),
    ROUNDEL_OFFSET(new NamespacedKey(TARDIS.plugin, "roundel_offset")),
    LEVEL_BOTTOM_ACTIVE(new NamespacedKey(TARDIS.plugin, "map_level_bottom_active")),
    LEVEL_MAIN_ACTIVE(new NamespacedKey(TARDIS.plugin, "map_level_main_active")),
    LEVEL_TOP_ACTIVE(new NamespacedKey(TARDIS.plugin, "map_level_top_active"));

    private final NamespacedKey key;

    Wool(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
