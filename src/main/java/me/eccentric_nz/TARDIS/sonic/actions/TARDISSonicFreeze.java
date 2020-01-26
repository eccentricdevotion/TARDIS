package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TARDISSonicFreeze {

    public static Player getTargetPlayer(Player player) {
        Location observerPos = player.getEyeLocation();
        TARDISVector3D observerDir = new TARDISVector3D(observerPos.getDirection());
        TARDISVector3D observerStart = new TARDISVector3D(observerPos);
        TARDISVector3D observerEnd = observerStart.add(observerDir.multiply(16));
        Player hit = null;
        // Get nearby entities
        for (Player target : player.getWorld().getPlayers()) {
            // Bounding box of the given player
            TARDISVector3D targetPos = new TARDISVector3D(target.getLocation());
            TARDISVector3D minimum = targetPos.add(-0.5, 0, -0.5);
            TARDISVector3D maximum = targetPos.add(0.5, 1.67, 0.5);
            if (target != player && hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                if (hit == null || hit.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                    hit = target;
                }
            }
        }
        return hit;
    }

    public static void immobilise(TARDIS plugin, Player player, Player target) {
        // freeze the closest player
        long cool = System.currentTimeMillis();
        if ((!plugin.getTrackerKeeper().getCooldown().containsKey(player.getUniqueId()) || plugin.getTrackerKeeper().getCooldown().get(player.getUniqueId()) < cool)) {
            plugin.getTrackerKeeper().getCooldown().put(player.getUniqueId(), cool + (plugin.getConfig().getInt("preferences.freeze_cooldown") * 1000L));
            TARDISMessage.send(target, "FREEZE", player.getName());
            UUID uuid = target.getUniqueId();
            plugin.getTrackerKeeper().getFrozenPlayers().add(uuid);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getFrozenPlayers().remove(uuid), 100L);
        } else {
            TARDISMessage.send(player, "FREEZE_NO");
        }
    }

    private static boolean hasIntersection(TARDISVector3D p1, TARDISVector3D p2, TARDISVector3D min, TARDISVector3D max) {
        double epsilon = 0.0001f;
        TARDISVector3D d = p2.subtract(p1).multiply(0.5);
        TARDISVector3D e = max.subtract(min).multiply(0.5);
        TARDISVector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        TARDISVector3D ad = d.abs();
        if (Math.abs(c.x) > e.x + ad.x) {
            return false;
        }
        if (Math.abs(c.y) > e.y + ad.y) {
            return false;
        }
        if (Math.abs(c.z) > e.z + ad.z) {
            return false;
        }
        if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon) {
            return false;
        }
        if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon) {
            return false;
        }
        return Math.abs(d.x * c.y - d.y * c.x) <= e.x * ad.y + e.y * ad.x + epsilon;
    }
}
