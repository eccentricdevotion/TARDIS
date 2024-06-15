package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import org.bukkit.Location;

import java.util.UUID;

public class Wave extends TARDISParticleRunnable implements Runnable {

    private final ParticleData particle;
    private final Location location;

    public Wave(TARDIS plugin, UUID uuid, ParticleData particle, Location location) {
        super(plugin, uuid);
        this.particle = particle;
        this.location = location;
    }

    @Override
    public void run() {
        t = t + 0.1 * Math.PI;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / particle.getDensity()) {
            double x = t * Math.cos(theta) * 0.5;
            double y = 1.5 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
            double z = t * Math.sin(theta) * 0.5;
            location.add(x, y, z);
            // spawn particle
            spawnParticle(particle.getEffect().getParticle(), location, 3, speed);
            location.subtract(x, y, z);
        }
        if (t > 10) {
            cancel();
        }
    }
}
