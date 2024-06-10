package me.eccentric_nz.TARDIS.particles;

public enum ParticleShape {

    BEAM(25L, 0.5d),
    HELIX(60L, 0),
    RANDOM(20L, 1.0d),
    RINGS(10L, 2.0d),
    VACUUM(12L, 0.5d),
    WAVE(32L, 0.5d);

    private final long period;
    private final double y;

    ParticleShape(long period, double y) {
        this.period = period;
        this.y = y;
    }

    public long getPeriod() {
        return period;
    }

    public double getY() {
        return y;
    }
}
