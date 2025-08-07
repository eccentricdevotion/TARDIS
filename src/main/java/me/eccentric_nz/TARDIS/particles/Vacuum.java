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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import org.bukkit.Location;

import java.util.UUID;

public class Vacuum extends TARDISParticleRunnable {

    private final ParticleData data;
    private final Location location;
    double radius = 2.5d;

    public Vacuum(TARDIS plugin, UUID uuid, ParticleData data, Location location) {
        super(plugin, uuid);
        this.data = data;
        this.location = location;
    }

    @Override
    public void run() {
        t += Math.PI / 10;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / data.getDensity()) {
            double x = radius * Math.cos(theta) * Math.sin(t);
            double y = radius * Math.cos(t);
            double z = radius * Math.sin(theta) * Math.sin(t);
            location.add(x, y, z);
            // spawn particle
            spawnParticle(location, 1, data);
            location.subtract(x, y, z);
        }
        radius -= 1;
        if (t > Math.PI) {
            cancel();
        }
    }
}
