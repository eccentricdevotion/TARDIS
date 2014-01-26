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
package me.eccentric_nz.TARDIS.builders;

/**
 * Data storage container for the TARDIS Interior Positioning System. It stores
 * the coordinates of the TIPS slot.
 *
 * minx, maxx, minz and maxz are the diagonal corners of a cuboid region.
 * centrex, centrez are the coordinates where the TARDIS will tart to be grown
 * from.
 *
 * @author eccentric_nz
 */
public class TARDISTIPSData {

    private int slot;
    private int minX;
    private int centreX;
    private int maxX;
    private int minZ;
    private int centreZ;
    private int maxZ;

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getCentreX() {
        return centreX;
    }

    public void setCentreX(int centreX) {
        this.centreX = centreX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinZ() {
        return minZ;
    }

    public void setMinZ(int minZ) {
        this.minZ = minZ;
    }

    public int getCentreZ() {
        return centreZ;
    }

    public void setCentreZ(int centreZ) {
        this.centreZ = centreZ;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(int maxZ) {
        this.maxZ = maxZ;
    }
}
