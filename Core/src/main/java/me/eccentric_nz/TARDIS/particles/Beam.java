package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Beam extends TARDISParticleRunnable {

    private final ParticleData data;
    private final Location location;
    private final Vector[] directions;

    public Beam(TARDIS plugin, UUID uuid, ParticleData data, Location location) {
        super(plugin, uuid);
        this.data = data;
        this.location = location;
        this.directions = generateVectors(false);
    }

    @Override
    public void run() {
        t = t + 0.1;
        for (int v = 0; v < 10; v++) {
            double x = t * directions[v].getX();
            double y = t * directions[v].getY();
            double z = t * directions[v].getZ();
            location.add(x, y, z);
            // spawn particle
            spawnParticle(location, 1, data);
            location.subtract(x, y, z);
        }
        if (t > 6) {
            cancel();
        }
    }
}
