package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;

public class Emitter extends TARDISParticleRunnable implements Runnable {

    private final TARDIS plugin;
    private final Location location;
    private final ParticleShape shape;
    private final ParticleEffect particle;
    private long loops;

    public Emitter(TARDIS plugin, Location location, ParticleShape shape, ParticleEffect particle, long end) {
        this.plugin = plugin;
        this.location = location.add(0.5d, shape.getY(), 0.5d);
        this.shape = shape;
        this.particle = particle;
        this.loops = end / shape.getPeriod() + 1;
    }

    @Override
    public void run() {
        if (t < loops) {
            TARDISParticleRunnable runnable;
            switch (shape) {
                case BEAM -> runnable = new Beam(particle.getParticle(), location);
                case HELIX -> runnable = new Helix(particle.getParticle(), location);
                case RINGS -> runnable = new Rings(particle.getParticle(), location, particle.getDensity());
                case VACUUM -> runnable = new Vacuum(particle.getParticle(), location, particle.getDensity());
                case WAVE -> runnable = new Wave(particle.getParticle(), location, particle.getDensity());
                default -> runnable = new Randomish(particle.getParticle(), location, particle.getDensity());
            }
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 0, 1);
            runnable.setTaskID(task);
            t++;
        } else {
            cancel();
        }
    }
}
