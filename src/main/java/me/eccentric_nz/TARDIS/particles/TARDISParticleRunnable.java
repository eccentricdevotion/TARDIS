/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetParticlePrefs;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISParticleRunnable implements Runnable {

    protected final UUID uuid;
    protected double t = 0;
    protected double speed = 0;
    int taskID;

    public TARDISParticleRunnable(TARDIS plugin, UUID uuid) {
        this.uuid = uuid;
        ResultSetParticlePrefs rs = new ResultSetParticlePrefs(plugin);
        if (rs.fromUUID(uuid.toString())) {
            this.speed = rs.getData().getSpeed();
        }
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public void spawnParticle(Particle particle, Location location, int count, double speed) {
        switch (particle) {
            case DUST -> {
                Particle.DustOptions options = new Particle.DustOptions(Color.BLUE, 1.0f);
                location.getWorld().spawnParticle(particle, location, count, speed, speed, speed, speed, options, false);
            }
            case SHRIEK -> location.getWorld().spawnParticle(particle, location, count, speed, speed, speed, speed, 1, false);
            case SCULK_CHARGE -> location.getWorld().spawnParticle(particle, location, count, speed, speed, speed, speed, 1.0f, false);
            case ENTITY_EFFECT -> location.getWorld().spawnParticle(particle, location, count, 0, 0, 0, 0, Color.WHITE, false);
            default -> location.getWorld().spawnParticle(particle, location, count, speed, speed, speed, speed, null, false);
        }
    }

    public void spawnParticle(Particle particle, Location location, int count, double speed, Color colour) {
        if (particle == Particle.DUST) {
            Particle.DustOptions options = new Particle.DustOptions(colour, 1.0f);
            location.getWorld().spawnParticle(particle, location, count, speed, speed, speed, speed, options, false);
        }
        if (particle == Particle.ENTITY_EFFECT) {
            location.getWorld().spawnParticle(particle, location, count, 0, 0, 0, 0, colour, false);
        }
    }

    public Vector[] generateVectors(boolean full) {
        Vector[] vectors = new Vector[10];
        for (int i = 0; i < 10; i++) {
            double vx = TARDISConstants.RANDOM.nextDouble(-1, 1);
            double vy = TARDISConstants.RANDOM.nextDouble(full ? -1 : 0, 1);
            double vz = TARDISConstants.RANDOM.nextDouble(-1, 1);
            vectors[i] = new Vector(vx, vy, vz);
        }
        return vectors;
    }

    public void cancel() {
        TARDIS.plugin.getServer().getScheduler().cancelTask(taskID);
        taskID = 0;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
