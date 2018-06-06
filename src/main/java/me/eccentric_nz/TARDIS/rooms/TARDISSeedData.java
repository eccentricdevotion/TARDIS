/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;

/**
 * @author eccentric_nz
 */
public class TARDISSeedData {

    private int id;
    private SCHEMATIC schematic;
    private String room;
    private int minx;
    private int maxx;
    private int minz;
    private int maxz;
    private boolean ARS;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SCHEMATIC getSchematic() {
        return schematic;
    }

    public void setSchematic(SCHEMATIC schematic) {
        this.schematic = schematic;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getMinx() {
        return minx;
    }

    public int getMaxx() {
        return maxx;
    }

    public int getMinz() {
        return minz;
    }

    public int getMaxz() {
        return maxz;
    }

    public void setChunkMinMax(String s) {
        String[] data = s.split(":");
        int x = TARDISNumberParsers.parseInt(data[1]);
        int z = TARDISNumberParsers.parseInt(data[2]);
        minx = x - 4;
        maxx = x + 4;
        minz = z - 4;
        maxz = z + 4;
    }

    public boolean hasARS() {
        return ARS;
    }

    public void setARS(boolean ARS) {
        this.ARS = ARS;
    }
}
