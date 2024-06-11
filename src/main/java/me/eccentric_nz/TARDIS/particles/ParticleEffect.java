package me.eccentric_nz.TARDIS.particles;

import org.bukkit.Particle;

public enum ParticleEffect {

    ASH(Particle.ASH),
    BOOM(Particle.SONIC_BOOM, 8),
    BREATH(Particle.DRAGON_BREATH, 8),
    BUBBLES(Particle.BUBBLE_POP),
    CLOUDS(Particle.CLOUD, 4),
    DUST(Particle.DUST, 16),
    EFFECT(Particle.EFFECT, 24),
    FLAMES(Particle.FLAME, 16),
    GLOW(Particle.GLOW, 24),
    GUST(Particle.SMALL_GUST, 12),
    HEART(Particle.HEART, 16),
    HIT(Particle.ENCHANTED_HIT),
    HONEY(Particle.DRIPPING_HONEY, 8),
    INFESTED(Particle.INFESTED, 16),
    MUSIC(Particle.NOTE, 24),
    OMEN(Particle.TRIAL_OMEN, 16),
    PETALS(Particle.CHERRY_LEAVES),
    SCULK(Particle.SCULK_CHARGE, 12),
    SHRIEK(Particle.SHRIEK),
    SNOWY(Particle.ITEM_SNOWBALL, 8),
    SPARK(Particle.ELECTRIC_SPARK, 12),
    SPLASH(Particle.SPLASH, 8),
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
