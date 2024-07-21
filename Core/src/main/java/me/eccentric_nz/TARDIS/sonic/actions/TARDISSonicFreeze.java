/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class TARDISSonicFreeze {

    public static Player getTargetPlayer(Player player) {
        Location observerPos = player.getEyeLocation();
        Vector observerDir = observerPos.getDirection();
        Vector observerStart = observerPos.toVector();
        Vector observerEnd = observerStart.add(observerDir.multiply(16));
        Player hit = null;
        // Get nearby players
        for (Player target : player.getWorld().getPlayers()) {
            // Bounding box of the given player
            Vector targetPos = target.getLocation().toVector();
            Vector minimum = targetPos.add(new Vector(-0.5, 0, -0.5));
            Vector maximum = targetPos.add(new Vector(0.5, 1.67, 0.5));
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
            plugin.getTrackerKeeper().getCooldown().put(player.getUniqueId(), cool + (plugin.getConfig().getInt("sonic.freeze_cooldown") * 1000L));
            plugin.getMessenger().send(target, TardisModule.TARDIS, "FREEZE", player.getName());
            UUID uuid = target.getUniqueId();
            plugin.getTrackerKeeper().getFrozenPlayers().add(uuid);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getTrackerKeeper().getFrozenPlayers().remove(uuid), 100L);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "FREEZE_NO");
        }
    }

    public static boolean hasIntersection(Vector p1, Vector p2, Vector min, Vector max) {
        double epsilon = 0.0001f;
        Vector d = p2.subtract(p1).multiply(0.5);
        Vector e = max.subtract(min).multiply(0.5);
        Vector c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector ad = abs(d);
        if (Math.abs(c.getX()) > e.getX() + ad.getX()) {
            return false;
        }
        if (Math.abs(c.getY()) > e.getY() + ad.getY()) {
            return false;
        }
        if (Math.abs(c.getZ()) > e.getZ() + ad.getZ()) {
            return false;
        }
        if (Math.abs(d.getY() * c.getZ() - d.getZ() * c.getY()) > e.getY() * ad.getZ() + e.getZ() * ad.getY() + epsilon) {
            return false;
        }
        if (Math.abs(d.getZ() * c.getX() - d.getX() * c.getZ()) > e.getZ() * ad.getX() + e.getX() * ad.getZ() + epsilon) {
            return false;
        }
        return Math.abs(d.getX() * c.getY() - d.getY() * c.getX()) <= e.getX() * ad.getY() + e.getY() * ad.getX() + epsilon;
    }

    private static Vector abs(Vector v) {
        return new Vector(Math.abs(v.getX()), Math.abs(v.getY()), Math.abs(v.getZ()));
    }
}
