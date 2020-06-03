package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.forcefield.TARDISForceField;
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class TARDISSonicKnockback {

    public static Entity getTargetEntity(Player player) {
        Location observerPos = player.getEyeLocation();
        TARDISVector3D observerDir = new TARDISVector3D(observerPos.getDirection());
        TARDISVector3D observerStart = new TARDISVector3D(observerPos);
        TARDISVector3D observerEnd = observerStart.add(observerDir.multiply(16));
        Entity hit = null;
        // Get nearby players
        for (Entity target : player.getWorld().getNearbyEntities(observerPos, 8.0d, 8.0d, 8.0d)) {
            // only monsters
            if (target instanceof Monster) {
                // Bounding box of the given player
                TARDISVector3D targetPos = new TARDISVector3D(target.getLocation());
                TARDISVector3D minimum = targetPos.add(-0.5, 0, -0.5);
                TARDISVector3D maximum = targetPos.add(0.5, 1.67, 0.5);
                if (target != player && TARDISSonicFreeze.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                    if (hit == null || hit.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                        hit = target;
                    }
                }
            }
        }
        return hit;
    }

    public static void knockback(Player player, Entity target) {
        TARDISForceField.velocity(target, TARDISForceField.getTrajectory2d(player.getLocation(), target), 0.75d);
    }
}
