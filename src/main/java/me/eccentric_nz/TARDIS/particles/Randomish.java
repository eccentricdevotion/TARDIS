package me.eccentric_nz.TARDIS.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class Randomish extends TARDISParticleRunnable implements Runnable {

    private final Particle particle;
    private final Location location;
    private final int density;

    public Randomish(Particle particle, Location location, int density) {
        super();
        this.particle = particle;
        this.location = location;
        this.density = density;
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
                spawnParticle(particle, location, 1);
                location.subtract(x, y, z);
            }
        }
        if (t > 10) {
            cancel();
        }
    }
}
