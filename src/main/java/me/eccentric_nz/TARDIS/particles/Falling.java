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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Falling extends TARDISParticleRunnable {

    public final Set<Vector> coords = new HashSet<>();
    private final ParticleData data;
    private final Location location;

    public Falling(TARDIS plugin, UUID uuid, ParticleData data, Location location) {
        super(plugin, uuid);
        this.data = data;
        this.location = location;
        // cache four corners
        init();
    }

    private void init() {
        coords.add(new Vector(-1.33, 3.5, -1.33));
        coords.add(new Vector(1.33, 3.5, -1.33));
        coords.add(new Vector(-1.33, 3.5, 1.33));
        coords.add(new Vector(1.33, 3.5, 1.33));
    }

    @Override
    public void run() {
        t += Math.PI / 10;
        for (Vector v : coords) {
            location.add(v.getX(), v.getY(), v.getZ());
            // spawn particle
            spawnParticle(location, 2, data);
            location.subtract(v.getX(), v.getY(), v.getZ());
        }
        if (t > Math.PI) {
            cancel();
        }
    }
}
