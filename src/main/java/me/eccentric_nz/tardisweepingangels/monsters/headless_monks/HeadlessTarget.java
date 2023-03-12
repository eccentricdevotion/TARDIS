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
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class HeadlessTarget implements Listener {

    private final TARDISWeepingAngels plugin;

    public HeadlessTarget(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onHeadlessMonkTarget(EntityTargetLivingEntityEvent event) {
        Entity attacker = event.getEntity();
        LivingEntity target = event.getTarget();
        if (!(attacker instanceof Skeleton skeleton)) {
            return;
        }
        if (!(target instanceof Player)) {
            return;
        }
        if (!skeleton.getPersistentDataContainer().has(TARDISWeepingAngels.HEADLESS_TASK, PersistentDataType.INTEGER)) {
            return;
        }
        int taskID = skeleton.getPersistentDataContainer().get(TARDISWeepingAngels.HEADLESS_TASK, PersistentDataType.INTEGER);
        int flameID = skeleton.getPersistentDataContainer().get(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER);
        EntityTargetEvent.TargetReason reason = event.getReason();
        if (reason == EntityTargetEvent.TargetReason.CLOSEST_PLAYER || reason == EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY) {
            if (taskID == -1) {
                // start projectile runnable
                HeadlessShootRunnable shooter = new HeadlessShootRunnable(attacker, target, false);
                taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeadlessShootRunnable(attacker, target, plugin.getConfig().getString("headless_monks.projectile").equals("SMALL_FIREBALL")), 1, 40);
                shooter.setTaskID(taskID);
                skeleton.getPersistentDataContainer().set(TARDISWeepingAngels.HEADLESS_TASK, PersistentDataType.INTEGER, taskID);
            }
            if (flameID != -1) {
                // stop flame runnable
                plugin.getServer().getScheduler().cancelTask(flameID);
                skeleton.getPersistentDataContainer().set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, -1);
            }
        } else {
            // stop projectile runnable
            if (taskID != -1) {
                plugin.getServer().getScheduler().cancelTask(taskID);
                skeleton.getPersistentDataContainer().set(TARDISWeepingAngels.HEADLESS_TASK, PersistentDataType.INTEGER, -1);
            }
            if (plugin.getConfig().getBoolean("headless_monks.particles") && flameID == -1) {
                // start flame runnable
                flameID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeadlessFlameRunnable((LivingEntity) attacker), 1, 20);
                skeleton.getPersistentDataContainer().set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, flameID);
            }
        }
    }
}
