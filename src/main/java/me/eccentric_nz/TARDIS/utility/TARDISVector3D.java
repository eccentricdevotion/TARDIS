/*
 * Freeze players
 *
 * Copyright 2012 Kristian S. Stangeland (Comphenix)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.utility;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class TardisVector3d {

    // Use protected members, like Bukkit
    public final double x;
    public final double y;
    public final double z;

    /**
     * Construct an immutable 3D vector.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    private TardisVector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Construct an immutable floating point 3D vector from a location object.
     *
     * @param location - the location to copy.
     */
    public TardisVector3d(Location location) {
        this(location.toVector());
    }

    /**
     * Construct an immutable floating point 3D vector from a mutable Bukkit vector.
     *
     * @param vector - the mutable real Bukkit vector to copy.
     */
    public TardisVector3d(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Vector cannot be NULL.");
        }
        x = vector.getX();
        y = vector.getY();
        z = vector.getZ();
    }

    /**
     * Adds the current vector and a given position vector, producing a result vector.
     *
     * @param other - the other vector.
     * @return The new result vector.
     */
    public TardisVector3d add(TardisVector3d other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be NULL");
        }
        return new TardisVector3d(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Adds the current vector and a given vector together, producing a result vector.
     *
     * @param x the x coordinate to add
     * @param y the y coordinate to add
     * @param z the z coordinate to add
     * @return The new result vector.
     */
    public TardisVector3d add(double x, double y, double z) {
        return new TardisVector3d(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Substracts the current vector and a given vector, producing a result position.
     *
     * @param other - the other position.
     * @return The new result position.
     */
    public TardisVector3d subtract(TardisVector3d other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be NULL");
        }
        return new TardisVector3d(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Multiply each dimension in the current vector by the given factor.
     *
     * @param factor - multiplier.
     * @return The new result.
     */
    public TardisVector3d multiply(int factor) {
        return new TardisVector3d(x * factor, y * factor, z * factor);
    }

    /**
     * Multiply each dimension in the current vector by the given factor.
     *
     * @param factor - multiplier.
     * @return The new result.
     */
    public TardisVector3d multiply(double factor) {
        return new TardisVector3d(x * factor, y * factor, z * factor);
    }

    /**
     * Retrieve the absolute value of this vector.
     *
     * @return The new result.
     */
    public TardisVector3d abs() {
        return new TardisVector3d(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    @Override
    public String toString() {
        return String.format("[x: %s, y: %s, z: %s]", x, y, z);
    }
}
