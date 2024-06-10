package me.eccentric_nz.TARDIS.particles;

import org.bukkit.Location;
import org.bukkit.Particle;

public class Helix extends TARDISParticleRunnable {

    private final Particle particle;
    private final Location location;

    public Helix(Particle particle, Location location) {
        super();
        this.particle = particle;
        this.location = location;
    }

    @Override
    public void run() {
        t = t + Math.PI / 16;
        double radius = 2;
        double x = radius * Math.cos(t);
        double y = 0.25 * t;
        double z = radius * Math.sin(t);
        location.add(x, y, z);
        // spawn particle
        spawnParticle(particle, location, 1);
        location.subtract(x, y, z);
        if (t > Math.PI * 8) {
            cancel();
        }
    }
}
