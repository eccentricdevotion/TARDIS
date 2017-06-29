/*
 * Copyright (C) 2017 eccentric_nz
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
package me.eccentric_nz.TARDIS.enumeration;

import org.bukkit.Material;

/**
 *
 * @author eccentric_nz
 */
public enum USE_CLAY {

    WOOL(Material.WOOL),
    TERRACOTTA(Material.STAINED_CLAY),
    CONCRETE(Material.CONCRETE);

    Material material;

    private USE_CLAY(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
