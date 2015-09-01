/*
 * Copyright (C) 2015 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.CubeEffect;
import de.slikey.effectlib.effect.VortexEffect;
import de.slikey.effectlib.util.ParticleEffect;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;

/**
 *
 * @author eccentric_nz
 */
public class TARDISEffectLibHelper {

    private static final EffectManager effectManager;

    static {
        effectManager = new EffectManager(TARDIS.plugin);
    }

    public static void sendWaterParticle(Location l) {
        CubeEffect effect = new CubeEffect(effectManager);
        effect.particle = ParticleEffect.WATER_SPLASH;
        effect.particles = 2;
        effect.edgeLength = 0.25f;
        effect.speed = 0.25f;
        effect.iterations = 1;
        effect.asynchronous = true;
        effect.setLocation(l.add(0.5d, 1.1d, 0.5d));
        effect.start();
    }

    public static void sendVortexParticles(Location l) {
        Location m = l.clone().add(0.0d, 0.01d, 0.0d);
        VortexEffect vortexEffect = new VortexEffect(effectManager);
        vortexEffect.particle = ParticleEffect.SPELL;
        vortexEffect.radius = 3;
        vortexEffect.circles = 10;
        vortexEffect.helixes = 10;
        vortexEffect.setLocation(l);
        vortexEffect.setTarget(m);
        vortexEffect.start();
    }

    public static void close() {
        effectManager.disposeOnTermination();
    }
}
