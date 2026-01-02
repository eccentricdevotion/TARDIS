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

public class Beam extends TARDISParticleRunnable {

    private final ParticleData data;
    private final Location location;
    private final Vector[] directions;

    public Beam(TARDIS plugin, UUID uuid, ParticleData data, Location location) {
        super(plugin, uuid);
        this.data = data;
        this.location = location;
        this.directions = generateVectors(false);
    }

    @Override
    public void run() {
        t = t + 0.1;
        for (int v = 0; v < 10; v++) {
            double x = t * directions[v].getX();
            double y = t * directions[v].getY();
            double z = t * directions[v].getZ();
            location.add(x, y, z);
            // spawn particle
            spawnParticle(location, 1, data);
            location.subtract(x, y, z);
        }
        if (t > 6) {
            cancel();
        }
    }
}
