/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;

import java.util.UUID;

public class ComehereRequest {

    private UUID requester;
    private UUID accepter;
    private int id;
    private int level;
    private Location current;
    private COMPASS currentDirection;
    private Location destination;
    private COMPASS destinationDirection;
    private boolean submarine;
    private boolean hidden;

    public UUID getRequester() {
        return requester;
    }

    public void setRequester(UUID requester) {
        this.requester = requester;
    }

    public UUID getAccepter() {
        return accepter;
    }

    public void setAccepter(UUID accepter) {
        this.accepter = accepter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Location getCurrent() {
        return current;
    }

    public void setCurrent(Location current) {
        this.current = current;
    }

    public COMPASS getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(COMPASS currentDirection) {
        this.currentDirection = currentDirection;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public COMPASS getDestinationDirection() {
        return destinationDirection;
    }

    public void setDestinationDirection(COMPASS destinationDirection) {
        this.destinationDirection = destinationDirection;
    }

    public boolean isSubmarine() {
        return submarine;
    }

    public void setSubmarine(boolean submarine) {
        this.submarine = submarine;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
