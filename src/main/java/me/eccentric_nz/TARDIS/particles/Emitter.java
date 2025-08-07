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

public class Emitter extends TARDISParticleRunnable implements Runnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Location location;
    private final ParticleData data;
    private final long loops;

    public Emitter(TARDIS plugin, UUID uuid, Location location, ParticleData data, long end) {
        super(plugin, uuid);
        this.plugin = plugin;
        this.uuid = uuid;
        this.location = location.clone().add(0.5, data.getShape().getY(), 0.5);
        this.data = data;
        this.loops = end / data.getShape().getPeriod() + 1;
    }

    @Override
    public void run() {
        if (t < loops) {
            TARDISParticleRunnable runnable;
            switch (data.getShape()) {
                case BEAM -> runnable = new Beam(plugin, uuid, data, location);
                case HELIX -> runnable = new Helix(plugin, uuid, data, location);
                case RINGS -> runnable = new Rings(plugin, uuid, data, location);
                case VACUUM -> runnable = new Vacuum(plugin, uuid, data, location);
                case WAVE -> runnable = new Wave(plugin, uuid, data, location);
                case FALLING -> runnable = new Falling(plugin, uuid, data, location);
                default -> runnable = new Randomish(plugin, uuid, data, location);
            }
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 0, 1);
            runnable.setTaskID(task);
            t++;
        } else {
            cancel();
        }
    }
}
