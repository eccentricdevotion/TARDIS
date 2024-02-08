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
package me.eccentric_nz.tardisweepingangels.monsters.judoon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import me.eccentric_nz.tardisweepingangels.nms.TWAJudoon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.UUID;

public class JudoonGuardRunnable implements Runnable {

    private final TARDIS plugin;

    // TODO - delete
    public JudoonGuardRunnable(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!TARDISWeepingAngels.getGuards().isEmpty()) {
            for (UUID uuid : TARDISWeepingAngels.getGuards()) {
                Entity entity = Bukkit.getEntity(uuid);
                if (entity == null || !entity.getPersistentDataContainer().has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                    return;
                }
                Location origin = entity.getLocation();
                for (Entity e : entity.getWorld().getNearbyEntities(origin, 8.0d, 8.0d, 8.0d, (d) -> d instanceof Monster)) {
                    if (e == entity) {
                        continue;
                    }
                    if (((CraftEntity) e).getHandle() instanceof TWAFollower) {
                        continue;
                    }
                    Damageable damageable = (Damageable) e;
                    double health = damageable.getHealth();
                    net.minecraft.world.entity.Entity husk = ((CraftEntity) entity).getHandle();
                    if (husk instanceof TWAJudoon judoon) {
                        int ammo = judoon.getAmmo();
                        if (ammo > 0 && health > 0) {
                            entity.getWorld().playSound(origin, "judoon_fire", 1.0f, 1.0f);
                            Snowball snowball = ((LivingEntity) entity).launchProjectile(Snowball.class);
                            Vector start = origin.toVector();
                            Vector direction = damageable.getLocation().toVector().subtract(start);
                            direction.normalize();
                            Vector bulletVelocity = direction.multiply(3.0d);
                            snowball.setVelocity(bulletVelocity);
                            // set judoon yaw
                            origin.setDirection(direction.subtract(start)); //set the origin's direction to be the direction vector between point A and B.
                            float yaw = origin.getYaw();
                            plugin.debug("yaw = " + yaw);
                            entity.setRotation(yaw,0);
                            ammo -= 1;
                            if (ammo >= 0) {
                                entity.setCustomName("Ammunition: " + ammo);
                                entity.setCustomNameVisible(true);
                                judoon.setAmmo(ammo);
                            }
                            // check there are no blocks in the way
                            BlockIterator iterator = new BlockIterator(entity.getWorld(), start, direction, 0, 16);
                            while (iterator.hasNext()) {
                                if (!iterator.next().getType().isAir()) {
                                    return;
                                }
                            }
                            damageable.damage(plugin.getMonstersConfig().getDouble("judoon.damage"), entity);
                        }
                    }
                }
            }
        }
    }
}
