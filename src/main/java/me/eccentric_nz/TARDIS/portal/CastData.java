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
package me.eccentric_nz.TARDIS.portal;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import org.bukkit.Location;

import java.util.UUID;

public class CastData {

    final Location interior;
    final Location exterior;
    final COMPASS direction;
    final UUID rotor;

    final ConsoleSize consoleSize;

    public CastData(Location interior, Location exterior, COMPASS direction, UUID rotor, ConsoleSize consoleSize) {
        this.interior = interior;
        this.exterior = exterior;
        this.direction = direction;
        this.rotor = rotor;
        this.consoleSize = consoleSize;
    }

    public Location getInterior() {
        return interior;
    }

    public Location getExterior() {
        return exterior;
    }

    public COMPASS getDirection() {
        return direction;
    }

    public UUID getRotor() {
        return rotor;
    }

    public ConsoleSize getConsoleSize() {
        return consoleSize;
    }
}
