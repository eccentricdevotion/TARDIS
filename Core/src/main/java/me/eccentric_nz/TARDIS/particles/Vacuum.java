package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import org.bukkit.Location;

import java.util.UUID;

public class Vacuum extends TARDISParticleRunnable {

    private final ParticleData data;
    private final Location location;
    double radius = 2.5d;

    public Vacuum(TARDIS plugin, UUID uuid, ParticleData data, Location location) {
        super(plugin, uuid);
        this.data = data;
        this.location = location;
    }

    @Override
    public void run() {
        t += Math.PI / 10;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / data.getDensity()) {
            double x = radius * Math.cos(theta) * Math.sin(t);
            double y = radius * Math.cos(t);
            double z = radius * Math.sin(theta) * Math.sin(t);
            location.add(x, y, z);
            // spawn particle
            spawnParticle(location, 1, data);
            location.subtract(x, y, z);
        }
        radius -= 1;
        if (t > Math.PI) {
            cancel();
        }
    }
}
