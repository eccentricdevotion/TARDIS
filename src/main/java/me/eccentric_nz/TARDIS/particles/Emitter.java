package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import org.bukkit.Location;

import java.util.UUID;

public class Emitter extends TARDISParticleRunnable implements Runnable {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Location location;
    private final ParticleData data;
    private final long loops;

    public Emitter(TARDIS plugin, UUID uuid, Location location, ParticleData data, long end) {
        super(plugin, uuid);
        this.plugin = plugin;
        this.uuid = uuid;
        this.location = location.add(0, data.getShape().getY(), 0);
        this.data = data;
        this.loops = end / data.getShape().getPeriod() + 1;
    }

    @Override
    public void run() {
        if (t < loops) {
            TARDISParticleRunnable runnable;
            switch (data.getShape()) {
                case BEAM -> runnable = new Beam(plugin, uuid, data, location);
                case HELIX -> runnable = new Helix(plugin, uuid, data, location);
                case RINGS -> runnable = new Rings(plugin, uuid, data, location);
                case VACUUM -> runnable = new Vacuum(plugin, uuid, data, location);
                case WAVE -> runnable = new Wave(plugin, uuid, data, location);
                default -> runnable = new Randomish(plugin, uuid, data, location);
            }
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 0, 1);
            runnable.setTaskID(task);
            t++;
        } else {
            cancel();
        }
    }
}
