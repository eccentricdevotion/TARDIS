/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.update;


public enum TARDISUpdateableCategory {

    CONTROLS("#55FF55", "TARDIS Controls"),
    INTERFACES("#FF5555", "TARDIS User Interfaces"),
    LOCATIONS("#55FFFF", "TARDIS Internal Spawn Locations"),

    SENSORS("#5555FF", "TARDIS Sensors"),
    OTHERS("#FF55FF", "Others");

    private final String colour;
    private final String name;

    TARDISUpdateableCategory(String colour, String name) {
        this.colour = colour;
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public String getName() {
        return name;
    }
}
