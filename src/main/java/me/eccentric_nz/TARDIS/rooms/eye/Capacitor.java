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
package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.custommodels.keys.EyeVariant;
import org.bukkit.NamespacedKey;

public enum Capacitor {

    NORMAL(0.75, 6, EyeVariant.SPHERE_0.getKey()),
    MEDIUM(1.0, 12, EyeVariant.SPHERE_1.getKey()),
    LARGE(1.33, 18, EyeVariant.SPHERE_2.getKey()),
    ENORMOUS(1.66, 24, EyeVariant.SPHERE_3.getKey()),
    SUPERMASSIVE(2, 30, EyeVariant.SPHERE_4.getKey());

    private final double radius;
    private final double rings;
    private final NamespacedKey model;

    Capacitor(double radius, double rings, NamespacedKey model) {
        // larger radii are needed for bigger spheres
        this.radius = radius;
        // more rings are needed for bigger spheres
        this.rings = rings;
        this.model = model;
    }

    public double getRadius() {
        return radius;
    }

    public double getRings() {
        return rings;
    }

    public NamespacedKey getModel() {
        return model;
    }
}
