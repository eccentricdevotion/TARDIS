package me.eccentric_nz.TARDIS.database.data;

import me.eccentric_nz.TARDIS.particles.ParticleEffect;
import me.eccentric_nz.TARDIS.particles.ParticleShape;

public class ParticlePrefs {

    private final ParticleEffect particle;
    private final ParticleShape shape;
    private final double speed;
    private final int density;
    private final boolean on;

    public ParticlePrefs(ParticleEffect particle, ParticleShape shape, double speed, int density, boolean on) {
        this.particle = particle;
        this.shape = shape;
        this.speed = speed;
        this.density = density;
        this.on = on;
    }

    public ParticleEffect getParticle() {
        return particle;
    }

    public ParticleShape getShape() {
        return shape;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDensity() {
        return density;
    }

    public boolean isOn() {
        return on;
    }
}
