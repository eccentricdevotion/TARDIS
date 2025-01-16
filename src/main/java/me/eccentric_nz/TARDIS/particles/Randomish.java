package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Randomish extends TARDISParticleRunnable implements Runnable {

    private final ParticleData data;
    private final Location location;

    public Randomish(TARDIS plugin, UUID uuid, ParticleData data, Location location) {
        super(plugin, uuid);
        this.data = data;
        this.location = location;
    }

    @Override
    public void run() {
        t = t + 0.5;
        for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 4) {
            for (Vector vector : generateVectors(true)) {
                double x = t * vector.getX();
                double y = t * vector.getY() + 1.5d;
                double z = t * vector.getZ();
                location.add(x, y, z);
                // spawn particle
                spawnParticle(location, 1, data);
                location.subtract(x, y, z);
            }
        }
        if (t > 10) {
            cancel();
        }
    }
}
