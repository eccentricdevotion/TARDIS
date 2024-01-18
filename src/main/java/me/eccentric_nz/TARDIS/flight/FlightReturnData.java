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
package me.eccentric_nz.TARDIS.flight;

import org.bukkit.Location;

import java.util.UUID;

/**
 *
 * @author eccentric_nz
 */
public class FlightReturnData {

    private final int id;
    private final Location location;
    private final int sound;
    private final int animation;
    private final UUID chicken;

    public FlightReturnData(int id, Location location, int sound, int animation, UUID chicken) {
        this.id = id;
        this.location = location;
        this.sound = sound;
        this.animation = animation;
        this.chicken = chicken;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public int getSound() {
        return sound;
    }

    public int getAnimation() {
        return animation;
    }

    public UUID getChicken() {
        return chicken;
    }
}
