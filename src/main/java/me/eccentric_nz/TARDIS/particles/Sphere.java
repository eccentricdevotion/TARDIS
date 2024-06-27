package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Sphere extends TARDISParticleRunnable {

    private final Location location;
    private final Particle particle;
    public Set<Vector> coords = new HashSet<>();

    public Sphere(TARDIS plugin, UUID uuid, Location location, Particle particle) {
        super(plugin, uuid);
        this.location = location;
        this.particle = particle;
        init();
    }

    public void init() {
        for (double i = 0; i <= Math.PI; i += Math.PI / 18) {
            double radius = Math.sin(i) * 1.25;
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a += Math.PI / 18) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                coords.add(new Vector(x, y, z));
            }
        }
    }

    @Override
    public void run() {
        t += 0.25;
        for (Vector v : coords) {
            location.add(v.getX(), v.getY(), v.getZ());
            switch (particle) {
                case DUST_COLOR_TRANSITION -> {
                    Particle.DustTransition transition = new Particle.DustTransition(Color.YELLOW, Color.ORANGE, 1.0f);
                    location.getWorld().spawnParticle(particle, location, 3, 0, 0, 0, 0, transition, false);
                }
                case BLOCK -> {
                    BlockData data = TARDISConstants.RANDOM.nextBoolean() ? Material.ORANGE_CONCRETE.createBlockData() : Material.YELLOW_CONCRETE.createBlockData();
                    location.getWorld().spawnParticle(particle, location, 3, 0, 0, 0, 0, data, false);
                }
                default -> spawnParticle(Particle.ENTITY_EFFECT, location, 3, 0, TARDISConstants.RANDOM.nextBoolean() ? Color.ORANGE : Color.YELLOW);
            }
            location.subtract(v.getX(), v.getY(), v.getZ());
        }
        if (t > 6) {
            cancel();
        }
    }
}
