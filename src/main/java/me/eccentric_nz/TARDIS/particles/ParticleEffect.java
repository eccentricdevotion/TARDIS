package me.eccentric_nz.TARDIS.particles;

import org.bukkit.Particle;

public enum ParticleEffect {

    ASH(Particle.ASH),
    BOOM(Particle.SONIC_BOOM, 8),
    BUBBLES(Particle.BUBBLE_POP),
    DUST(Particle.DUST, 16),
    EFFECT(Particle.EFFECT, 24),
    GLOW(Particle.GLOW, 24),
    GUST(Particle.GUST, 8),
    HEART(Particle.HEART, 16),
    HIT(Particle.ENCHANTED_HIT),
    MUSIC(Particle.NOTE, 24),
    OMEN(Particle.TRIAL_OMEN, 16),
    PETALS(Particle.CHERRY_LEAVES),
    SCULK(Particle.SCULK_CHARGE, 12),
    SHRIEK(Particle.SHRIEK),
    SMOKE(Particle.CAMPFIRE_SIGNAL_SMOKE, 4),
    SPAWNER(Particle.TRIAL_SPAWNER_DETECTION, 8),
    SPELL(Particle.WITCH, 24),
    SPORE(Particle.CRIMSON_SPORE);

    private final Particle particle;
    private final int density;

    ParticleEffect(Particle particle, int density) {
        this.particle = particle;
        this.density = density;
    }

    ParticleEffect(Particle particle) {
        this.particle = particle;
        this.density = 32;
    }

    public Particle getParticle() {
        return particle;
    }

    public int getDensity() {
        return density;
    }
}
