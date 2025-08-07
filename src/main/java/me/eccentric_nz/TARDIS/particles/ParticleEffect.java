/*
 * Copyright (C) 2025 eccentric_nz
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
    LEAVES(Particle.TINTED_LEAVES),
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
