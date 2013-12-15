package me.eccentric_nz.TARDIS.utility;

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
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class TARDISVector3D {

    /**
     * Represents the null (0, 0, 0) origin.
     */
    public static final TARDISVector3D ORIGIN = new TARDISVector3D(0, 0, 0);

    // Use protected members, like Bukkit
    public final double x;
    public final double y;
    public final double z;

    /**
     * Construct an immutable 3D vector.
     *
     * @param x
     * @param y
     * @param z
     */
    public TARDISVector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Construct an immutable floating point 3D vector from a location object.
     *
     * @param location - the location to copy.
     */
    public TARDISVector3D(Location location) {
        this(location.toVector());
    }

    /**
     * Construct an immutable floating point 3D vector from a mutable Bukkit
     * vector.
     *
     * @param vector - the mutable real Bukkit vector to copy.
     */
    public TARDISVector3D(Vector vector) {
        if (vector == null) {
            throw new IllegalArgumentException("Vector cannot be NULL.");
        }
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }

    /**
     * Convert this instance to an equivalent real 3D vector.
     *
     * @return Real 3D vector.
     */
    public Vector toVector() {
        return new Vector(x, y, z);
    }

    /**
     * Adds the current vector and a given position vector, producing a result
     * vector.
     *
     * @param other - the other vector.
     * @return The new result vector.
     */
    public TARDISVector3D add(TARDISVector3D other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be NULL");
        }
        return new TARDISVector3D(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Adds the current vector and a given vector together, producing a result
     * vector.
     *
     * @param x
     * @param y
     * @param z
     * @return The new result vector.
     */
    public TARDISVector3D add(double x, double y, double z) {
        return new TARDISVector3D(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Substracts the current vector and a given vector, producing a result
     * position.
     *
     * @param other - the other position.
     * @return The new result position.
     */
    public TARDISVector3D subtract(TARDISVector3D other) {
        if (other == null) {
            throw new IllegalArgumentException("other cannot be NULL");
        }
        return new TARDISVector3D(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Substracts the current vector and a given vector together, producing a
     * result vector.
     *
     * @param x
     * @param y
     * @param z
     * @return The new result vector.
     */
    public TARDISVector3D subtract(double x, double y, double z) {
        return new TARDISVector3D(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Multiply each dimension in the current vector by the given factor.
     *
     * @param factor - multiplier.
     * @return The new result.
     */
    public TARDISVector3D multiply(int factor) {
        return new TARDISVector3D(x * factor, y * factor, z * factor);
    }

    /**
     * Multiply each dimension in the current vector by the given factor.
     *
     * @param factor - multiplier.
     * @return The new result.
     */
    public TARDISVector3D multiply(double factor) {
        return new TARDISVector3D(x * factor, y * factor, z * factor);
    }

    /**
     * Divide each dimension in the current vector by the given divisor.
     *
     * @param divisor - the divisor.
     * @return The new result.
     */
    public TARDISVector3D divide(int divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Cannot divide by null.");
        }
        return new TARDISVector3D(x / divisor, y / divisor, z / divisor);
    }

    /**
     * Divide each dimension in the current vector by the given divisor.
     *
     * @param divisor - the divisor.
     * @return The new result.
     */
    public TARDISVector3D divide(double divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Cannot divide by null.");
        }
        return new TARDISVector3D(x / divisor, y / divisor, z / divisor);
    }

    /**
     * Retrieve the absolute value of this vector.
     *
     * @return The new result.
     */
    public TARDISVector3D abs() {
        return new TARDISVector3D(Math.abs(x), Math.abs(y), Math.abs(z));
    }

    @Override
    public String toString() {
        return String.format("[x: %s, y: %s, z: %s]", x, y, z);
    }
}
