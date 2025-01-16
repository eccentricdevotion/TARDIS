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
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class FollowerFinder {

    public static Entity getEntity(Player player, EntityType type) {
        Entity entity = null;
        // get the entity the player is looking at
        Location observerPos = player.getEyeLocation();
        TARDISVector3D observerDir = new TARDISVector3D(observerPos.getDirection());
        TARDISVector3D observerStart = new TARDISVector3D(observerPos);
        TARDISVector3D observerEnd = observerStart.add(observerDir.multiply(16));
        // get nearby entities
        for (Entity target : player.getNearbyEntities(8.0d, 8.0d, 8.0d)) {
            // bounding box of the entity
            TARDISVector3D targetPos = new TARDISVector3D(target.getLocation());
            TARDISVector3D minimum = targetPos.add(-0.5, 0, -0.5);
            TARDISVector3D maximum = targetPos.add(0.5, 1.67, 0.5);
            if (target.getType().equals(type) && TARDISSonicFreeze.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                if (entity == null || entity.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                    return target;
                }
            }
        }
        return null;
    }
}
