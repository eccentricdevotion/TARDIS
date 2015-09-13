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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;

/**
 *
 * @author eccentric_nz
 */
public class TARDISUpgradeData {

    private SCHEMATIC schematic;
    private SCHEMATIC previous;
    private String wall;
    private String floor;
    private String siegeWall;
    private String siegeFloor;
    private int level;

    public SCHEMATIC getSchematic() {
        return schematic;
    }

    public void setSchematic(SCHEMATIC schematic) {
        this.schematic = schematic;
    }

    public SCHEMATIC getPrevious() {
        return previous;
    }

    public void setPrevious(SCHEMATIC previous) {
        this.previous = previous;
    }

    public String getWall() {
        return wall;
    }

    public void setWall(String wall) {
        this.wall = wall;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getSiegeWall() {
        return siegeWall;
    }

    public void setSiegeWall(String siegeWall) {
        this.siegeWall = siegeWall;
    }

    public String getSiegeFloor() {
        return siegeFloor;
    }

    public void setSiegeFloor(String siegeFloor) {
        this.siegeFloor = siegeFloor;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
