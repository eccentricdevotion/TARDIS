package me.eccentric_nz.TARDIS.database.data;

import me.eccentric_nz.TARDIS.particles.ParticleEffect;
import me.eccentric_nz.TARDIS.particles.ParticleShape;

public class ParticleData {

    private final ParticleEffect effect;
    private final ParticleShape shape;
    private final int density;
    private final double speed;
    private final boolean on;

    public ParticleData(ParticleEffect effect, ParticleShape shape, int density, double speed, boolean on) {
        this.effect = effect;
        this.shape = shape;
        this.density = density;
        this.speed = speed;
        this.on = on;
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    public ParticleShape getShape() {
        return shape;
    }

    public int getDensity() {
        return density;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isOn() {
        return on;
    }
}
