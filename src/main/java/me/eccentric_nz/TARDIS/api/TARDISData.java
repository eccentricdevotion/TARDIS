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
package me.eccentric_nz.TARDIS.api;

import org.bukkit.Location;

import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISData {

    private final String owner;
    private final Location location;
    private final String console;
    private final String chameleon;
    private final String door;
    private final String powered;
    private final String siege;
    private final String abandoned;
    private final List<String> occupants;

    public TARDISData(String owner, Location location, String console, String chameleon, String door, String powered, String siege, String abandoned, List<String> occupants) {
        this.owner = owner;
        this.location = location;
        this.console = console;
        this.chameleon = chameleon;
        this.door = door;
        this.powered = powered;
        this.siege = siege;
        this.abandoned = abandoned;
        this.occupants = occupants;
    }

    /**
     * Gets the TARDIS' Owner
     *
     * @return the owner's name
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the TARDIS' current location
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the TARDIS console type
     *
     * @return the name of the console
     */
    public String getConsole() {
        return console;
    }

    /**
     * Gets the TARDIS chamemelon circuit setting
     *
     * @return the name of the Chameleon preset
     */
    public String getChameleon() {
        return chameleon;
    }

    /**
     * Gets the state of the TARDIS door
     *
     * @return "Open" or "Closed"
     */
    public String getDoor() {
        return door;
    }

    /**
     * Gets whether the TARDIS is powered on
     *
     * @return "Yes" or "No"
     */
    public String getPowered() {
        return powered;
    }

    /**
     * Gets whether the TARDIS is in Siege mode
     *
     * @return "Yes" or "No"
     */
    public String getSiege() {
        return siege;
    }

    /**
     * Gets whether the TARDIS is abandoned
     *
     * @return "Yes" or "No"
     */
    public String getAbandoned() {
        return abandoned;
    }

    /**
     * Gets a list of the players in the TARDIS
     *
     * @return a list of playuer names
     */
    public List<String> getOccupants() {
        return occupants;
    }
}
