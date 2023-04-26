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
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;

/**
 *
 * @author macgeek
 */
public class HeadlessProjectileListener implements Listener {
    
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
          if (event.getHitEntity() instanceof Player player && event.getEntity() instanceof Snowball snowball) {
            ProjectileSource source = snowball.getShooter();
            if (source instanceof Skeleton skeleton && skeleton.getPersistentDataContainer().has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)) {
                player.damage(3.0, skeleton);
            }
        }
    }
    
}
