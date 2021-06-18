/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.builders;

import me.eccentric_nz.tardis.enumeration.Schematic;
import org.bukkit.Material;

/**
 * @author eccentric_nz
 */
public class TardisBuildData {

    private Schematic schematic;
    private Material wallType;
    private Material floorType;
    private int lamp;

    public Schematic getSchematic() {
        return schematic;
    }

    public void setSchematic(Schematic schematic) {
        this.schematic = schematic;
    }

    public Material getWallType() {
        return wallType;
    }

    public void setWallType(Material wallType) {
        this.wallType = wallType;
    }

    public Material getFloorType() {
        return floorType;
    }

    public void setFloorType(Material floorType) {
        this.floorType = floorType;
    }

    public int getLamp() {
        return lamp;
    }

    public void setLamp(int lamp) {
        this.lamp = lamp;
    }
}
