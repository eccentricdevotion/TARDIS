package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import org.bukkit.Location;

import java.util.UUID;

public class Rings extends TARDISParticleRunnable {

    private final ParticleData data;
    private final Location location;

    public Rings(TARDIS plugin, UUID uuid, ParticleData data, Location location) {
        super(plugin, uuid);
        this.data = data;
        this.location = location;
    }

    @Override
    public void run() {
        t += Math.PI / 10;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / data.getDensity()) {
            double radius = 1.5d;
            double x = radius * Math.cos(theta);
            double y = radius * Math.cos(t);
            double z = radius * Math.sin(theta);
            location.add(x, y, z);
            // spawn particle
            spawnParticle(data.getEffect().getParticle(), location, 1, speed);
            location.subtract(x, y, z);
        }
        if (t > Math.PI) {
            cancel();
        }
    }
}
