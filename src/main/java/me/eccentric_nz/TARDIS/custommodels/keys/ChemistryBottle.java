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

public enum ChemistryBottle {

    ALUMINIUM_OXIDE(new NamespacedKey(TARDIS.plugin, "aluminium_oxide")),
    AMMONIA(new NamespacedKey(TARDIS.plugin, "ammonia")),
    JAR(new NamespacedKey(TARDIS.plugin, "jar")),
    BENZENE(new NamespacedKey(TARDIS.plugin, "benzene")),
    CRUDE_OIL(new NamespacedKey(TARDIS.plugin, "crude_oil")),
    GLUE(new NamespacedKey(TARDIS.plugin, "glue")),
    HYDROGEN_PEROXIDE(new NamespacedKey(TARDIS.plugin, "hydrogen_peroxide")),
    IRON_SULFIDE(new NamespacedKey(TARDIS.plugin, "iron_sulfide")),
    LATEX(new NamespacedKey(TARDIS.plugin, "latex")),
    LUMINOL(new NamespacedKey(TARDIS.plugin, "luminol")),
    MAGNESIUM_NITRATE(new NamespacedKey(TARDIS.plugin, "magnesium_nitrate")),
    POTASSIUM_IODIDE(new NamespacedKey(TARDIS.plugin, "potassium_iodide")),
    SALT(new NamespacedKey(TARDIS.plugin, "salt")),
    SOAP(new NamespacedKey(TARDIS.plugin, "soap")),
    SODIUM_FLUORIDE(new NamespacedKey(TARDIS.plugin, "sodium_fluoride")),
    SODIUM_HYDRIDE(new NamespacedKey(TARDIS.plugin, "sodium_hydride")),
    SODIUM_HYPOCHLORITE(new NamespacedKey(TARDIS.plugin, "sodium_hypochlorite"));

    private final NamespacedKey key;

    ChemistryBottle(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
