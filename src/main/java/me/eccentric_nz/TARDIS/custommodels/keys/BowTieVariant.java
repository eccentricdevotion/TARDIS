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

public enum BowTieVariant {

    BOWTIE_WHITE(new NamespacedKey(TARDIS.plugin, "bowtie_white")),
    BOWTIE_ORANGE(new NamespacedKey(TARDIS.plugin, "bowtie_orange")),
    BOWTIE_MAGENTA(new NamespacedKey(TARDIS.plugin, "bowtie_magenta")),
    BOWTIE_LIGHT_BLUE(new NamespacedKey(TARDIS.plugin, "bowtie_light_blue")),
    BOWTIE_YELLOW(new NamespacedKey(TARDIS.plugin, "bowtie_yellow")),
    BOWTIE_LIME(new NamespacedKey(TARDIS.plugin, "bowtie_lime")),
    BOWTIE_PINK(new NamespacedKey(TARDIS.plugin, "bowtie_pink")),
    BOWTIE_GRAY(new NamespacedKey(TARDIS.plugin, "bowtie_gray")),
    BOWTIE_LIGHT_GRAY(new NamespacedKey(TARDIS.plugin, "bowtie_light_gray")),
    BOWTIE_CYAN(new NamespacedKey(TARDIS.plugin, "bowtie_cyan")),
    BOWTIE_PURPLE(new NamespacedKey(TARDIS.plugin, "bowtie_purple")),
    BOWTIE_BLUE(new NamespacedKey(TARDIS.plugin, "bowtie_blue")),
    BOWTIE_BROWN(new NamespacedKey(TARDIS.plugin, "bowtie_brown")),
    BOWTIE_GREEN(new NamespacedKey(TARDIS.plugin, "bowtie_green")),
    BOWTIE_RED(new NamespacedKey(TARDIS.plugin, "bowtie_red")),
    BOWTIE_BLACK(new NamespacedKey(TARDIS.plugin, "bowtie_black"));

    private final NamespacedKey key;

    BowTieVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
