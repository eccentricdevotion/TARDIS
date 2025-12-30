/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.api;

import org.bukkit.Location;

import java.util.List;

/**
 * @author eccentric_nz
 */
public record TARDISData(String owner, Location location, String console, String chameleon, String door, String powered,
                         String siege, String abandoned, List<String> occupants) {

    /**
     * Gets the TARDIS' Owner
     *
     * @return the owner's name
     */
    @Override
    public String owner() {
        return owner;
    }

    /**
     * Gets the TARDIS' current location
     *
     * @return the location
     */
    @Override
    public Location location() {
        return location;
    }

    /**
     * Gets the TARDIS console type
     *
     * @return the name of the console
     */
    @Override
    public String console() {
        return console;
    }

    /**
     * Gets the TARDIS chamemelon circuit setting
     *
     * @return the name of the Chameleon preset
     */
    @Override
    public String chameleon() {
        return chameleon;
    }

    /**
     * Gets the state of the TARDIS door
     *
     * @return "Open" or "Closed"
     */
    @Override
    public String door() {
        return door;
    }

    /**
     * Gets whether the TARDIS is powered on
     *
     * @return "Yes" or "No"
     */
    @Override
    public String powered() {
        return powered;
    }

    /**
     * Gets whether the TARDIS is in Siege mode
     *
     * @return "Yes" or "No"
     */
    @Override
    public String siege() {
        return siege;
    }

    /**
     * Gets whether the TARDIS is abandoned
     *
     * @return "Yes" or "No"
     */
    @Override
    public String abandoned() {
        return abandoned;
    }

    /**
     * Gets a list of the players in the TARDIS
     *
     * @return a list of playuer names
     */
    @Override
    public List<String> occupants() {
        return occupants;
    }
}
