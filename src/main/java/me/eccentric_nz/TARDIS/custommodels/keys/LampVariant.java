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

public enum LampVariant {

    BLUE_LAMP(new NamespacedKey(TARDIS.plugin, "blue_lamp")),
    GREEN_LAMP(new NamespacedKey(TARDIS.plugin, "green_lamp")),
    PURPLE_LAMP(new NamespacedKey(TARDIS.plugin, "purple_lamp")),
    RED_LAMP(new NamespacedKey(TARDIS.plugin, "red_lamp")),
    LAMP_ON(new NamespacedKey(TARDIS.plugin, "light_lamp_on")),
    TENTH_ON(new NamespacedKey(TARDIS.plugin, "light_tenth_on")),
    ELEVENTH_ON(new NamespacedKey(TARDIS.plugin, "light_eleventh_on")),
    BULB_ON(new NamespacedKey(TARDIS.plugin, "light_bulb_on")),
    TENTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "light_tenth_cloister")),
    ELEVENTH_CLOISTER(new NamespacedKey(TARDIS.plugin, "light_eleventh_cloister")),
    BULB_CLOISTER(new NamespacedKey(TARDIS.plugin, "light_bulb_cloister"));

    private final NamespacedKey key;

    LampVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
