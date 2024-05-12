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
package me.eccentric_nz.TARDIS.enumeration;

import java.util.HashMap;

/**
 * The Space Time Throttle controls the effective "speed" of the TARDIS by
 * altering the "length" of the route (and thus shortening the perceived travel
 * time) through the Time Vortex.
 */
public enum SpaceTimeThrottle {

    NORMAL(4, 500, 18, 10, 1.0f),
    FASTER(3, 375, 12, 7, 1.5f),
    RAPID(2, 250, 9, 5, 2.0f),
    WARP(1, 125, 6, 4, 3.0f),
    REBUILD(0, 80, 3, -1, 1.0f),
    JUNK(-1, 600, 25, -1, 1.0f);

    private static final HashMap<Integer, SpaceTimeThrottle> BY_DELAY = new HashMap<>();

    static {
        for (SpaceTimeThrottle spt : values()) {
            BY_DELAY.put(spt.delay, spt);
        }
    }

    private final int delay;
    private final long flightTime;
    private final int loops;
    private final int rescue;
    private final float artronMultiplier;

    SpaceTimeThrottle(int delay, long flightTime, int loops, int rescue, float artronMultiplier) {
        this.delay = delay;
        this.flightTime = flightTime;
        this.loops = loops;
        this.rescue = rescue;
        this.artronMultiplier = artronMultiplier;
    }

    public static HashMap<Integer, SpaceTimeThrottle> getByDelay() {
        return BY_DELAY;
    }

    public int getDelay() {
        return delay;
    }

    public long getFlightTime() {
        return flightTime;
    }

    public int getLoops() {
        return loops;
    }

    public int getRescue() {
        return rescue;
    }

    public float getArtronMultiplier() {
        return artronMultiplier;
    }
}
