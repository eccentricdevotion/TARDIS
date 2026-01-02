/*
 * Copyright (C) 2026 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.sonic.actions;

import me.eccentric_nz.TARDIS.forcefield.TARDISForceField;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

/**
 * @author eccentric_nz
 */
public class TARDISSonicKnockback {

    public static Entity getTargetEntity(Player player) {
        Location observerPos = player.getEyeLocation();
        RayTraceResult result = observerPos.getWorld().rayTraceEntities(observerPos, observerPos.getDirection(), 8.0d);
        return result != null ? result.getHitEntity() : null;
    }

    public static void knockback(Player player, Entity target) {
        TARDISForceField.velocity(target, TARDISForceField.getTrajectory2d(player.getLocation(), target), 0.75d);
    }
}
