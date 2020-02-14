package me.eccentric_nz.TARDIS.forcefield;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public class TARDISForceFieldVisualiser {

    private final TARDIS plugin;
    private final double SPACE = 1.0d;

    public TARDISForceFieldVisualiser(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void showBorder(Location location, int d) {

        TARDISForceFieldLocation tffl = new TARDISForceFieldLocation(location, plugin.getConfig().getDouble("allow.force_field") - 1.0d);

        World world = location.getWorld();
        for (int i = 0; i < 14; i++) {
            // topLeft
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontLeft().add(0, 0, SPACE), 1, TARDISConstants.DUSTOPTIONS.get(d));
            // topBack
            world.spawnParticle(Particle.REDSTONE, tffl.getTopBackLeft().add(SPACE, 0, 0), 1, TARDISConstants.DUSTOPTIONS.get(d));
            // topRight
            world.spawnParticle(Particle.REDSTONE, tffl.getTopBackRight().add(0, 0, -SPACE), 1, TARDISConstants.DUSTOPTIONS.get(d));
            // topFront
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontRight().add(-SPACE, 0, 0), 1, TARDISConstants.DUSTOPTIONS.get(d));
            // bottomLeft
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontLeft().add(0, 0, SPACE), 1, TARDISConstants.DUSTOPTIONS.get(d));
            // bottomBack
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomBackLeft().add(SPACE, 0, 0), 1, TARDISConstants.DUSTOPTIONS.get(d));
            // bottomRight
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomBackRight().add(0, 0, -SPACE), 1, TARDISConstants.DUSTOPTIONS.get(d));
            // bottomFront
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontRight().add(-SPACE, 0, 0), 1, TARDISConstants.DUSTOPTIONS.get(d));
        }
    }
}
