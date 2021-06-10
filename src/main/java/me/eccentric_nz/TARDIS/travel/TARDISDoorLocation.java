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
package me.eccentric_nz.tardis.travel;

import me.eccentric_nz.tardis.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * A Tele-door was a teleport system that allowed an individual to travel vast spaces by merely walking through a door.
 *
 * @author eccentric_nz
 */
public class TARDISDoorLocation {

    private Location l;
    private World w;
    private COMPASS d;

    public TARDISDoorLocation() {
    }

    /**
     * @return the Location of the door
     */
    public Location getL() {
        return l;
    }

    /**
     * Set the location of the door
     *
     * @param l the location of the door
     */
    public void setL(Location l) {
        this.l = l;
    }

    /**
     * @return the World the door is in
     */
    public World getW() {
        return w;
    }

    /**
     * Set the world the door is in.
     *
     * @param w the world
     */
    public void setW(World w) {
        this.w = w;
    }

    /**
     * @return the direction of the door
     */
    public COMPASS getD() {
        return d;
    }

    /**
     * Set the door direction.
     *
     * @param d the direction (one of NORTH, SOUTH, EAST, WEST)
     */
    public void setD(COMPASS d) {
        this.d = d;
    }
}
