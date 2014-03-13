/*
 * Copyright (C) 2014 eccentric_nz
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

import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBuildData {

    private SCHEMATIC schematic;
    private int wall_id;
    private byte wall_data;
    private int floor_id;
    private byte floor_data;
    private int box_id;
    private byte box_data;
    private int lamp;

    public TARDISBuildData() {
    }

    public SCHEMATIC getSchematic() {
        return schematic;
    }

    public void setSchematic(SCHEMATIC schematic) {
        this.schematic = schematic;
    }

    public int getWall_id() {
        return wall_id;
    }

    public void setWall_id(int wall_id) {
        this.wall_id = wall_id;
    }

    public byte getWall_data() {
        return wall_data;
    }

    public void setWall_data(byte wall_data) {
        this.wall_data = wall_data;
    }

    public int getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

    public byte getFloor_data() {
        return floor_data;
    }

    public void setFloor_data(byte floor_data) {
        this.floor_data = floor_data;
    }

    public int getBox_id() {
        return box_id;
    }

    public void setBox_id(int box_id) {
        this.box_id = box_id;
    }

    public byte getBox_data() {
        return box_data;
    }

    public void setBox_data(byte box_data) {
        this.box_data = box_data;
    }

    public int getLamp() {
        return lamp;
    }

    public void setLamp(int lamp) {
        this.lamp = lamp;
    }
}
