/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import org.bukkit.Location;

import java.util.UUID;

public class Wave extends TARDISParticleRunnable implements Runnable {

    private final ParticleData data;
    private final Location location;

    public Wave(TARDIS plugin, UUID uuid, ParticleData data, Location location) {
        super(plugin, uuid);
        this.data = data;
        this.location = location;
    }

    @Override
    public void run() {
        t = t + 0.1 * Math.PI;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / data.getDensity()) {
            double x = t * Math.cos(theta) * 0.5;
            double y = 1.5 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
            double z = t * Math.sin(theta) * 0.5;
            location.add(x, y, z);
            // spawn particle
            spawnParticle(location, 3, data);
            location.subtract(x, y, z);
        }
        if (t > 10) {
            cancel();
        }
    }
}
