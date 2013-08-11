/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.ARS;

/**
 *
 * @author eccentric_nz
 */
public class TARDISARSSlot {

    private int l;
    private int x;
    private int z;

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        // convert to the actual x coordinate of the start of the chunk
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        // convert to the actual z coordinate of the start of the chunk
        this.z = z;
    }
}
