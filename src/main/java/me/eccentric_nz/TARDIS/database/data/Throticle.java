package me.eccentric_nz.TARDIS.database.data;

import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;

public class Throticle {

    private final SpaceTimeThrottle  throttle;
    private final boolean particles;

    public Throticle(SpaceTimeThrottle throttle, boolean particles) {
        this.throttle = throttle;
        this.particles = particles;
    }

    public SpaceTimeThrottle getThrottle() {
        return throttle;
    }

    public boolean getParticles() {
        return particles;
    }
}
