package me.eccentric_nz.TARDIS.particles;

import org.bukkit.Location;
import org.bukkit.Particle;

public class Wave extends TARDISParticleRunnable implements Runnable {

    private final Particle particle;
    private final Location location;
    private final int density;

    public Wave(Particle particle, Location location, int density) {
        super();
        this.particle = particle;
        this.location = location;
        this.density = density;
    }

    @Override
    public void run() {
        t = t + 0.1 * Math.PI;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / density) {
            double x = t * Math.cos(theta) * 0.5;
            double y = 1.5 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
            double z = t * Math.sin(theta) * 0.5;
            location.add(x, y, z);
            // spawn particle
            spawnParticle(particle, location, 3);
            location.subtract(x, y, z);
        }
        if (t > 10) {
            cancel();
        }
    }
}
