package me.eccentric_nz.TARDIS.enumeration;

import java.util.HashMap;

/**
 * The Space Time Throttle controls the effective "speed" of the TARDIS by altering the "length" of the route (and thus
 * shorten the perceived travel time) through the Time Vortex.
 */
public enum SpaceTimeThrottle {

    NORMAL(4, 500, 18),
    FASTER(3, 375, 12),
    RAPID(2, 250, 9),
    WARP(1, 125, 6),
    REBUILD(0, 80, 3), // TODO ??
    JUNK(-1, 600, 25); // ??

    private static final HashMap<Integer, SpaceTimeThrottle> BY_DELAY = new HashMap<>();

    static {
        for (SpaceTimeThrottle spt : values()) {
            BY_DELAY.put(spt.delay, spt);
        }
    }

    private final int delay;
    private final long flightTime;
    private final int loops;

    SpaceTimeThrottle(int delay, long flightTime, int loops) {
        this.delay = delay;
        this.flightTime = flightTime;
        this.loops = loops;
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
}
