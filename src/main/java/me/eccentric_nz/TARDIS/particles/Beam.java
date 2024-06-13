package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Beam extends TARDISParticleRunnable {

    private final ParticleEffect particle;
    private final Location location;
    private final Vector[] directions;

    public Beam(TARDIS plugin, UUID uuid, ParticleEffect particle, Location location) {
        super(plugin, uuid);
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
            spawnParticle(particle.getParticle(), location, 1, speed);
            location.subtract(x, y, z);
        }
        if (t > 6) {
            cancel();
        }
    }
}
