package me.eccentric_nz.tardis.enumeration;

import java.util.HashMap;

/**
 * The Space Time Throttle controls the effective "speed" of the tardis by altering the "length" of the route (and thus
 * shorten the perceived travel time) through the Time Vortex.
 */
public enum SpaceTimeThrottle {

	NORMAL(4, 500, 18, 1.0f), FASTER(3, 375, 12, 1.5f), RAPID(2, 250, 9, 2.0f), WARP(1, 125, 6, 3.0f), REBUILD(0, 80, 3, 1.0f), JUNK(-1, 600, 25, 1.0f);

	private static final HashMap<Integer, SpaceTimeThrottle> BY_DELAY = new HashMap<>();

	static {
		for (SpaceTimeThrottle spt : values()) {
			BY_DELAY.put(spt.delay, spt);
		}
	}

	private final int delay;
	private final long flightTime;
	private final int loops;
	private final float artronMultiplier;

	SpaceTimeThrottle(int delay, long flightTime, int loops, float artronMultiplier) {
		this.delay = delay;
		this.flightTime = flightTime;
		this.loops = loops;
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

	public float getArtronMultiplier() {
		return artronMultiplier;
	}
}
