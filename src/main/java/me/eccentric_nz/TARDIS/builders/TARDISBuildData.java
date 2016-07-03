/*
 * Copyright (C) 2016 eccentric_nz
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
import org.bukkit.Material;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBuildData {

    private SCHEMATIC schematic;
    private Material wallType;
    private byte wallData;
    private Material floorType;
    private byte floorData;
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

    public Material getWallType() {
        return wallType;
    }

    public void setWallType(Material wallType) {
        this.wallType = wallType;
    }

    public byte getWallData() {
        return wallData;
    }

    public void setWallData(byte wallData) {
        this.wallData = wallData;
    }

    public Material getFloorType() {
        return floorType;
    }

    public void setFloorType(Material floorType) {
        this.floorType = floorType;
    }

    public byte getFloorData() {
        return floorData;
    }

    public void setFloorData(byte floorData) {
        this.floorData = floorData;
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
