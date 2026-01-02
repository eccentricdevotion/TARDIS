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
import org.bukkit.util.Vector;

import java.util.UUID;

public class Randomish extends TARDISParticleRunnable implements Runnable {

    private final ParticleData data;
    private final Location location;

    public Randomish(TARDIS plugin, UUID uuid, ParticleData data, Location location) {
        super(plugin, uuid);
        this.data = data;
        this.location = location;
    }

    @Override
    public void run() {
        t = t + 0.5;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 4) {
            for (Vector vector : generateVectors(true)) {
                double x = t * vector.getX();
                double y = t * vector.getY() + 1.5d;
                double z = t * vector.getZ();
                location.add(x, y, z);
                // spawn particle
                spawnParticle(location, 1, data);
                location.subtract(x, y, z);
            }
        }
        if (t > 10) {
            cancel();
        }
    }
}
