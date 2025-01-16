package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Regeneration extends TARDISParticleRunnable implements Runnable {

    private final Location location;
    private final float yaw;
    private final Particle particle = Particle.DUST_COLOR_TRANSITION;
    private final Particle.DustTransition transition = new Particle.DustTransition(Color.fromRGB(255, 182, 27), Color.fromRGB(255, 208, 27), 1.0f);
    private final List<Location> coords = new ArrayList<>();
    private final List<Vector> angles = new ArrayList<>();

    public Regeneration(TARDIS plugin, Player player, Location location, float yaw) {
        super(plugin, player.getUniqueId());
        this.location = location;
        this.yaw = yaw;
        init();
    }

    private void init() {
        double angle = yawToAngle(yaw);
        double r = 1.25d;
        // calculate positions based on armor stand yaw
        double x = Math.cos(angle) * r;
        double z = Math.sin(angle) * r;
        // head
        Location head = location.clone().add(0, 1.85d, 0);
        coords.add(head);
        // left hand
        Location left = location.clone().add(x, 0.8d, z);
        coords.add(left);
        // right hand
        Location right = location.clone().add(-x, 0.8d, -z);
        coords.add(right);
        // calculate direction vectors
        // head - straight up
        angles.add(new Vector(0, 1, 0));
        // left hand
        Vector vl = left.clone().subtract(head).toVector().normalize();
        angles.add(vl);
        // right hand
        Vector vr = right.clone().subtract(head).toVector().normalize();
        angles.add(vr);
    }

    private void spawnTransitionParticles(Location location) {
        location.getWorld().spawnParticle(particle, location, 3, 0.15d, 0, 0.15d, 0, transition, false);
    }

    private double yawToAngle(float yaw) {
        if (yaw >= 360.0f) {
            yaw -= 360.0f;
        } else if (yaw < 0.0f) {
            yaw = 180.0f + (180 + yaw);
        }
        // will return the angle perpendicular to the original yaw (the direction the armour stand is facing)
        // as the unit circle is rotated 90 degrees from Minecraft yaw
        return Math.toRadians(yaw);
    }

    @Override
    public void run() {
        t = t + 0.1;
        int i = 0;
        for (Location l : coords) {
            double x = t * angles.get(i).getX();
            double y = t * angles.get(i).getY();
            double z = t * angles.get(i).getZ();
            i++;
            if (i > 2) {
                i = 0;
            }
            l.add(x, y, z);
            // spawn particle
            spawnTransitionParticles(l);
            l.subtract(x, y, z);
        }
        if (t > 2) {
            cancel();
        }
    }
}
