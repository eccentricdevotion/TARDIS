package me.eccentric_nz.TARDIS.particles;

import org.bukkit.Particle;

public enum ParticleEffect {

    ASH(Particle.ASH),
    BLOCK(Particle.BLOCK),
    BOOM(Particle.SONIC_BOOM),
    BREATH(Particle.DRAGON_BREATH),
    BUBBLES(Particle.BUBBLE_POP),
    CLOUDS(Particle.CLOUD),
    DUST(Particle.DUST),
    EFFECT(Particle.ENTITY_EFFECT),
    FLAMES(Particle.FLAME),
    GLOW(Particle.GLOW),
    GUST(Particle.SMALL_GUST),
    HEART(Particle.HEART),
    HIT(Particle.ENCHANTED_HIT),
    HONEY(Particle.DRIPPING_HONEY),
    INFESTED(Particle.INFESTED),
    MUSIC(Particle.NOTE),
    NAUTILUS(Particle.NAUTILUS),
    OMEN(Particle.TRIAL_OMEN),
    PETALS(Particle.CHERRY_LEAVES),
    SCULK(Particle.SCULK_CHARGE),
    SHRIEK(Particle.SHRIEK),
    SNOWY(Particle.ITEM_SNOWBALL),
    SPARK(Particle.ELECTRIC_SPARK),
    SPLASH(Particle.SPLASH),
    SPAWNER(Particle.TRIAL_SPAWNER_DETECTION),
    SPELL(Particle.WITCH),
    SPORE(Particle.CRIMSON_SPORE),
    TOTEM(Particle.TOTEM_OF_UNDYING);

    private final Particle particle;

    ParticleEffect(Particle particle) {
        this.particle = particle;
    }

    public Particle getParticle() {
        return particle;
    }
}
