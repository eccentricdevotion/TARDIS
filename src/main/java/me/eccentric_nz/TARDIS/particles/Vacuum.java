package me.eccentric_nz.TARDIS.particles;

import org.bukkit.Location;
import org.bukkit.Particle;

public class Vacuum extends TARDISParticleRunnable {

    private final Particle particle;
    private final Location location;
    private final int density;
    double radius = 3d;

    public Vacuum(Particle particle, Location location, int density) {
        super();
        this.particle = particle;
        this.location = location;
        this.density = density;
    }

    @Override
    public void run() {
        t += Math.PI / 10;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / density) {
            double x = radius * Math.cos(theta) * Math.sin(t);
            double y = radius * Math.cos(t);
            double z = radius * Math.sin(theta) * Math.sin(t);
            location.add(x, y, z);
            // spawn particle
            spawnParticle(particle, location, 1);
            location.subtract(x, y, z);
        }
        radius -= 1;
        if (t > Math.PI) {
            cancel();
        }
    }
}
