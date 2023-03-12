/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardisweepingangels.monsters.headless_monks;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class HeadlessFlameRunnable implements Runnable {

    private final LivingEntity monk;

    public HeadlessFlameRunnable(LivingEntity monk) {
        this.monk = monk;
    }

    @Override
    public void run() {
        if (monk.isDead()) {
            Bukkit.getScheduler().cancelTask(monk.getPersistentDataContainer().get(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER));
            monk.getPersistentDataContainer().set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, -1);
            return;
        }
        // get centre location of entity
        double angle = Math.abs((monk.getLocation().getYaw() % 360) - 180) - 180;
        if (angle < 0) { angle += 360; }
        double addX = 0.4 * Math.sin(Math.PI * 2 * angle / 360);
        double addZ = 0.4 * Math.cos(Math.PI * 2 * angle / 360);
        // get start location
        Location start = monk.getLocation().clone().add(addX, 1.4, addZ);
        // get end location
        Location end = start.clone().add(0, 0.9, 0);
        // spawn particles between the two locations
        spawnFlameAlongLine(start, end);
    }

    public void spawnFlameAlongLine(Location start, Location end) {
        double d = start.distance(end) / 8;
        for (int i = 0; i < 8; i++) {
            Location l = start.clone();
            Vector direction = end.toVector().subtract(start.toVector()).normalize();
            Vector v = direction.multiply(i * d);
            l.add(v.getX(), v.getY(), v.getZ());
            start.getWorld().spawnParticle(Particle.FLAME, l, 1, 0, 0, 0, 0.005, null, false);
        }
    }
}
