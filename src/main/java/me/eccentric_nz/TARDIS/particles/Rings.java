package me.eccentric_nz.TARDIS.particles;

import org.bukkit.Location;
import org.bukkit.Particle;

public class Rings extends TARDISParticleRunnable {

    private final Particle particle;
    private final Location location;
    private final int density;

    public Rings(Particle particle, Location location, int density) {
        super();
        this.particle = particle;
        this.location = location;
        this.density = density;
    }

    @Override
    public void run() {
        t += Math.PI / 10;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / density) {
            double radius = 2.5d;
//            double x = radius * Math.cos(theta) * Math.sin(t);
            double x = radius * Math.cos(theta);
            double y = radius * Math.cos(t);
//            double z = radius * Math.sin(theta) * Math.sin(t);
            double z = radius * Math.sin(theta);
            location.add(x, y, z);
            // spawn particle
            spawnParticle(particle, location, 1);
            location.subtract(x, y, z);
        }
        if (t > Math.PI) {
            cancel();
        }
    }
}
