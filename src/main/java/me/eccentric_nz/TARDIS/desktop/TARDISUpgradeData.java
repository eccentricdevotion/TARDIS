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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.enumeration.Schematic;

/**
 * @author eccentric_nz
 */
public class TARDISUpgradeData {

    private Schematic schematic;
    private Schematic previous;
    private String wall;
    private String floor;
    private String siegeWall;
    private String siegeFloor;
    private int level;

    /**
     * Returns the desktop theme the upgrade will change to.
     *
     * @return the Schematic
     */
    public Schematic getSchematic() {
        return schematic;
    }

    public void setSchematic(Schematic schematic) {
        this.schematic = schematic;
    }

    /**
     * Returns the desktop theme the TARDIS had previously.
     *
     * @return the Schematic
     */
    public Schematic getPrevious() {
        return previous;
    }

    public void setPrevious(Schematic previous) {
        this.previous = previous;
    }

    /**
     * Returns the wall block the theme use when changing.
     *
     * @return the Material name of the wall block
     */
    public String getWall() {
        return wall;
    }

    public void setWall(String wall) {
        this.wall = wall;
    }

    /**
     * Returns the floor block the theme use when changing.
     *
     * @return the Material name of the floor block
     */
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
