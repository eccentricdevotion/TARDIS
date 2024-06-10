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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicFreeze;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class FollowerFinder {

    public static Entity getEntity(Player player, EntityType type) {
//        Entity entity = null;
        // get the entity the player is looking at
        Location observerPos = player.getEyeLocation();
        Vector observerDir = observerPos.getDirection();
        Vector observerStart = observerPos.toVector();
        Vector observerEnd = observerStart.add(observerDir.multiply(16));
        // Get nearby entities
        for (Entity target : player.getNearbyEntities(8.0d, 8.0d, 8.0d)) {
            // Bounding box of the given player
            Vector targetPos = target.getLocation().toVector();
            Vector minimum = targetPos.add(new Vector(-0.5, 0, -0.5));
            Vector maximum = targetPos.add(new Vector(0.5, 1.67, 0.5));
            if (target.getType().equals(type) && TARDISSonicFreeze.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
//                if (entity == null || entity.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                return target;
//                }
            }
        }
        return null;
    }
}
