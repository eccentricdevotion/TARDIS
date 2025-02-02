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
package me.eccentric_nz.tardischunkgenerator.worldgen.feature;

import org.bukkit.Material;

public class TARDISTreeData {

    private final Material base;
    private final Material stem;
    private final Material hat;
    private final Material decor;
    private final boolean planted;

    public TARDISTreeData(Material base, Material stem, Material hat, Material decor, boolean planted) {
        this.base = base;
        this.stem = stem;
        this.hat = hat;
        this.decor = decor;
        this.planted = planted;
    }

    public Material getBase() {
        return base;
    }

    public Material getStem() {
        return stem;
    }

    public Material getHat() {
        return hat;
    }

    public Material getDecor() {
        return decor;
    }

    public boolean isPlanted() {
        return planted;
    }
}
