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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class HeadlessShootRunnable implements Runnable {

    private final Entity monk;
    private final Entity target;
    private final boolean fireball;
    private int taskID;

    public HeadlessShootRunnable(Entity monk, Entity target, boolean fireball) {
        this.monk = monk;
        this.target = target;
        this.fireball = fireball;
    }

    @Override
    public void run() {
        if (monk.isDead() || ((Monster) monk).getTarget() == null) {
            // cancel task
            Bukkit.getScheduler().cancelTask(taskID);
        }
        Vector direction = target.getLocation().toVector().subtract(monk.getLocation().toVector());
        direction.normalize();
        Vector bulletVelocity = direction.multiply(2.0);
        if (fireball) {
            ((LivingEntity) monk).launchProjectile(SmallFireball.class, bulletVelocity);
        } else {
            Snowball snowball = ((LivingEntity) monk).launchProjectile(Snowball.class, bulletVelocity);
            snowball.setItem(new ItemStack(Material.FIRE_CHARGE, 1));
        }
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}
