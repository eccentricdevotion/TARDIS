package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Falling extends TARDISParticleRunnable {

    private final ParticleData data;
    private final Location location;
    public Set<Vector> coords = new HashSet<>();

    public Falling(TARDIS plugin, UUID uuid, ParticleData data, Location location) {
        super(plugin, uuid);
        this.data = data;
        this.location = location;
        // cache four corners
        init();
    }

    private void init() {
        coords.add(new Vector(-1.33, 3.5, -1.33));
        coords.add(new Vector(1.33, 3.5, -1.33));
        coords.add(new Vector(-1.33, 3.5, 1.33));
        coords.add(new Vector(1.33, 3.5, 1.33));
    }

    @Override
    public void run() {
        t += Math.PI / 10;
        for (Vector v : coords) {
            location.add(v.getX(), v.getY(), v.getZ());
            // spawn particle
            spawnParticle(location, 2, data);
            location.subtract(v.getX(), v.getY(), v.getZ());
        }
        if (t > Math.PI) {
            cancel();
        }
    }
}
