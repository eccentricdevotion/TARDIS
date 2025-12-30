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

public enum Food {

    FISH_FINGER(new NamespacedKey(TARDIS.plugin, "fish_finger")),
    JAMMY_DODGER(new NamespacedKey(TARDIS.plugin, "jammy_dodger")),
    CUSTARD_CREAM(new NamespacedKey(TARDIS.plugin, "custard_cream")),
    PAPER_BAG(new NamespacedKey(TARDIS.plugin, "paper_bag")),
    BOWL_OF_CUSTARD(new NamespacedKey(TARDIS.plugin, "bowl_of_custard"));

    private final NamespacedKey key;

    Food(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
