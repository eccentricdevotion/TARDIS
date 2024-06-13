package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Randomish extends TARDISParticleRunnable implements Runnable {

    private final ParticleEffect particle;
    private final Location location;

    public Randomish(TARDIS plugin, UUID uuid, ParticleEffect particle, Location location) {
        super(plugin, uuid);
        this.particle = particle;
        this.location = location;
    }

    @Override
    public void run() {
        t = t + 0.5;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 4) {
            for (Vector vector : generateVectors(true)) {
                double x = t * vector.getX() + 0.5d;
                double y = t * vector.getY() + 1.5d;
                double z = t * vector.getZ() + 0.5d;
                location.add(x, y, z);
                // spawn particle
                spawnParticle(particle.getParticle(), location, 1, speed);
                location.subtract(x, y, z);
            }
        }
        if (t > 10) {
            cancel();
        }
    }
}
