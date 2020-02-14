package me.eccentric_nz.TARDIS.forcefield;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class TARDISForceFieldVisualiser {

    private final TARDIS plugin;
    private final double SPACE = 1.0d;

    public TARDISForceFieldVisualiser(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void showBorder(Location location) {

        TARDISForceFieldLocation tffl = new TARDISForceFieldLocation(location, plugin.getConfig().getDouble("allow.force_field"));

        World world = location.getWorld();
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 204, 255), 1);
        for (int i = 0; i < 16; i++) {
            // topLeft
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontLeft().add(0, 0, SPACE), 5, dustOptions);
            // topBack
            world.spawnParticle(Particle.REDSTONE, tffl.getTopBackLeft().add(SPACE, 0, 0), 5, dustOptions);
            // topRight
            world.spawnParticle(Particle.REDSTONE, tffl.getTopBackRight().add(0, 0, -SPACE), 5, dustOptions);
            // topFront
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontRight().add(-SPACE, 0, 0), 5, dustOptions);
            // bottomLeft
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontLeft().add(0, 0, SPACE), 5, dustOptions);
            // bottomBack
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomBackLeft().add(SPACE, 0, 0), 5, dustOptions);
            // bottomRight
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomBackRight().add(0, 0, -SPACE), 5, dustOptions);
            // bottomFront
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontRight().add(-SPACE, 0, 0), 5, dustOptions);
        }
        for (int i = 0; i < 7; i++) {
            // cornerFrontLeft
            world.spawnParticle(Particle.REDSTONE, tffl.getCornerFrontLeft().add(0, SPACE, 0), 5, dustOptions);
            // cornerBackLeft
            world.spawnParticle(Particle.REDSTONE, tffl.getCornerBackLeft().add(0, SPACE, 0), 5, dustOptions);
            // cornerBackRight
            world.spawnParticle(Particle.REDSTONE, tffl.getCornerBackRight().add(0, SPACE, 0), 5, dustOptions);
            // cornerFrontRight
            world.spawnParticle(Particle.REDSTONE, tffl.getCornerFrontRight().add(0, SPACE, 0), 5, dustOptions);
        }
    }
}
