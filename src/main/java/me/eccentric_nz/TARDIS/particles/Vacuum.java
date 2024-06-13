package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;

import java.util.UUID;

public class Vacuum extends TARDISParticleRunnable {

    private final ParticleEffect particle;
    private final Location location;
    double radius = 3d;

    public Vacuum(TARDIS plugin, UUID uuid, ParticleEffect particle, Location location) {
        super(plugin, uuid);
        this.particle = particle;
        this.location = location;
    }

    @Override
    public void run() {
        t += Math.PI / 10;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / particle.getDensity()) {
            double x = radius * Math.cos(theta) * Math.sin(t);
            double y = radius * Math.cos(t);
            double z = radius * Math.sin(theta) * Math.sin(t);
            location.add(x, y, z);
            // spawn particle
            spawnParticle(particle.getParticle(), location, 1, speed);
            location.subtract(x, y, z);
        }
        radius -= 1;
        if (t > Math.PI) {
            cancel();
        }
    }
}
