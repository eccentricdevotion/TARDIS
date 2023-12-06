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
package me.eccentric_nz.tardisweepingangels.monsters.judoon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import me.eccentric_nz.tardisweepingangels.nms.TWAJudoon;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.*;
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
                for (Entity e : entity.getNearbyEntities(8.0d, 8.0d, 8.0d)) {
                    if (!(e instanceof Monster) || ((CraftEntity) e).getHandle() instanceof TWAFollower) {
                        return;
                    }
                    Damageable damageable = (Damageable) e;
                    double health = damageable.getHealth();
                    TWAJudoon judoon = (TWAJudoon) ((CraftEntity) entity).getHandle();
                    int ammo = judoon.getAmmo();
                    if (ammo > 0 && health > 0) {
                        damageable.damage(plugin.getMonstersConfig().getDouble("judoon.damage"), entity);
                        entity.getWorld().playSound(entity.getLocation(), "judoon_fire", 1.0f, 1.0f);
                        Snowball snowball = ((LivingEntity) entity).launchProjectile(Snowball.class);
                        Vector direction = damageable.getLocation().toVector().subtract(entity.getLocation().toVector());
                        direction.normalize();
                        Vector bulletVelocity = direction.multiply(3.0d);
                        snowball.setVelocity(bulletVelocity);
                        ammo -= 1;
                        if (ammo >= 0) {
                            entity.setCustomName("Ammunition: " + ammo);
                            entity.setCustomNameVisible(true);
                            judoon.setAmmo(ammo);
                        }
                    }
                }
            }
        }
    }
}
