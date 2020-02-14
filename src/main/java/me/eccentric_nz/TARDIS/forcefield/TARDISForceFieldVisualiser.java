package me.eccentric_nz.TARDIS.forcefield;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class TARDISForceFieldVisualiser {

    private final TARDIS plugin;

    public TARDISForceFieldVisualiser(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void showBorder(Location location) {
        plugin.debug("Showing force field border!");

        TARDISForceFieldLocation tffl = new TARDISForceFieldLocation(location, plugin.getConfig().getDouble("allow.force_field"));

        Vector topLeft = tffl.getTopBackLeft().subtract(tffl.getTopFrontLeft()).toVector();
        Vector topBack = tffl.getTopBackRight().subtract(tffl.getTopBackLeft()).toVector();
        Vector topRight = tffl.getTopBackRight().subtract(tffl.getTopFrontRight()).toVector();
        Vector topFront = tffl.getTopFrontRight().subtract(tffl.getTopFrontLeft()).toVector();
        Vector bottomLeft = tffl.getTopBackLeft().subtract(tffl.getBottomFrontLeft()).toVector();
        Vector bottomBack = tffl.getBottomBackRight().subtract(tffl.getBottomBackLeft()).toVector();
        Vector bottomRight = tffl.getBottomBackLeft().subtract(tffl.getBottomFrontRight()).toVector();
        Vector bottomFront = tffl.getBottomFrontRight().subtract(tffl.getBottomFrontLeft()).toVector();
        Vector cornerFrontLeft = tffl.getBottomFrontLeft().subtract(tffl.getTopFrontLeft()).toVector();
        Vector cornerBackLeft = tffl.getBottomBackLeft().subtract(tffl.getTopBackLeft()).toVector();
        Vector cornerBackRight = tffl.getBottomBackRight().subtract(tffl.getTopBackRight()).toVector();
        Vector cornerFrontRight = tffl.getBottomFrontRight().subtract(tffl.getTopFrontRight()).toVector();

        World world = location.getWorld();
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 204, 255), 1);
        for (double i = 0; i < 15.5d; i += 0.5d) {
            topLeft.multiply(i);
            tffl.getTopFrontLeft().add(topLeft);
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontLeft(), 1, dustOptions);
            tffl.getTopFrontLeft().subtract(topLeft);
            topLeft.normalize();
            //
            topBack.multiply(i);
            tffl.getTopBackLeft().add(topBack);
            world.spawnParticle(Particle.REDSTONE, tffl.getTopBackLeft(), 1, dustOptions);
            tffl.getTopBackLeft().subtract(topBack);
            topBack.normalize();
            //
            topRight.multiply(i);
            tffl.getTopFrontRight().add(topRight);
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontRight(), 1, dustOptions);
            tffl.getTopFrontRight().subtract(topRight);
            topRight.normalize();
            //
            topFront.multiply(i);
            tffl.getTopFrontLeft().add(topFront);
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontLeft(), 1, dustOptions);
            tffl.getTopFrontLeft().subtract(topFront);
            topFront.normalize();
            //
            bottomLeft.multiply(i);
            tffl.getBottomFrontLeft().add(bottomLeft);
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontLeft(), 1, dustOptions);
            tffl.getBottomFrontLeft().subtract(bottomLeft);
            bottomLeft.normalize();
            //
            bottomBack.multiply(i);
            tffl.getBottomBackLeft().add(bottomBack);
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomBackLeft(), 1, dustOptions);
            tffl.getBottomBackLeft().subtract(bottomBack);
            bottomBack.normalize();
            //
            bottomRight.multiply(i);
            tffl.getBottomFrontRight().add(bottomRight);
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontRight(), 1, dustOptions);
            tffl.getBottomFrontRight().subtract(bottomRight);
            bottomRight.normalize();
            //
            bottomFront.multiply(i);
            tffl.getBottomFrontLeft().add(bottomFront);
            world.spawnParticle(Particle.REDSTONE, tffl.getBottomFrontLeft(), 1, dustOptions);
            tffl.getBottomFrontLeft().subtract(bottomFront);
            bottomFront.normalize();
        }
        for (double i = 0; i < 7.5d; i += 0.5d) {
            cornerFrontLeft.multiply(i);
            tffl.getTopFrontLeft().add(cornerFrontLeft);
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontLeft(), 1, dustOptions);
            tffl.getTopFrontLeft().subtract(cornerFrontLeft);
            cornerFrontLeft.normalize();
            //
            cornerBackLeft.multiply(i);
            tffl.getTopBackLeft().add(cornerBackLeft);
            world.spawnParticle(Particle.REDSTONE, tffl.getTopBackLeft(), 1, dustOptions);
            tffl.getTopBackLeft().subtract(cornerBackLeft);
            cornerBackLeft.normalize();
            //
            cornerBackRight.multiply(i);
            tffl.getTopBackRight().add(cornerBackRight);
            world.spawnParticle(Particle.REDSTONE, tffl.getTopBackRight(), 1, dustOptions);
            tffl.getTopBackRight().subtract(cornerBackRight);
            cornerBackRight.normalize();
            //
            cornerFrontRight.multiply(i);
            tffl.getTopFrontRight().add(cornerFrontRight);
            world.spawnParticle(Particle.REDSTONE, tffl.getTopFrontRight(), 1, dustOptions);
            tffl.getTopFrontRight().subtract(cornerFrontRight);
            cornerFrontRight.normalize();
        }
    }
}
