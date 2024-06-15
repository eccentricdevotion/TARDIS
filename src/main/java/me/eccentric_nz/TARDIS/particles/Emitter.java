package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import org.bukkit.Location;

import java.util.UUID;

public class Emitter extends TARDISParticleRunnable implements Runnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Location location;
    private final ParticleData particle;
    private final long loops;

    public Emitter(TARDIS plugin, UUID uuid, Location location, ParticleData particle, long end) {
        super(plugin, uuid);
        this.plugin = plugin;
        this.uuid = uuid;
        this.location = location.add(0.5d, particle.getShape().getY(), 0.5d);
        this.particle = particle;
        this.loops = end / particle.getShape().getPeriod() + 1;
    }

    @Override
    public void run() {
        if (t < loops) {
            TARDISParticleRunnable runnable;
            switch (particle.getShape()) {
                case BEAM -> runnable = new Beam(plugin, uuid, particle.getEffect(), location);
                case HELIX -> runnable = new Helix(plugin, uuid, particle.getEffect(), location);
                case RINGS -> runnable = new Rings(plugin, uuid, particle, location);
                case VACUUM -> runnable = new Vacuum(plugin, uuid, particle, location);
                case WAVE -> runnable = new Wave(plugin, uuid, particle, location);
                default -> runnable = new Randomish(plugin, uuid, particle.getEffect(), location);
            }
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 0, 1);
            runnable.setTaskID(task);
            t++;
        } else {
            cancel();
        }
    }
}
