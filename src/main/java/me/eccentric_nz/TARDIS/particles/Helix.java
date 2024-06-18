package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;

import java.util.UUID;

public class Helix extends TARDISParticleRunnable {

    private final ParticleEffect particle;
    private final Location location;

    public Helix(TARDIS plugin, UUID uuid, ParticleEffect particle, Location location) {
        super(plugin, uuid);
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
        spawnParticle(particle.getParticle(), location, 1, speed);
        location.subtract(x, y, z);
        if (t > Math.PI * 8) {
            cancel();
        }
    }
}
