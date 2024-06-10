package me.eccentric_nz.TARDIS.particles;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class Beam extends TARDISParticleRunnable {

    private final Particle particle;
    private final Location location;
    private final Vector[] directions;

    public Beam(Particle particle, Location location) {
        super();
        this.particle = particle;
        this.location = location;
        this.directions = generateVectors(false);
    }

    @Override
    public void run() {
        t = t + 0.1;
        for (int v = 0; v < 10; v++) {
            double x = t * directions[v].getX() + 0.5d;
            double y = t * directions[v].getY();
            double z = t * directions[v].getZ() + 0.5d;
            location.add(x, y, z);
            // spawn particle
            spawnParticle(particle, location, 1);
            location.subtract(x, y, z);
        }
        if (t > 6) {
            cancel();
        }
    }
}
