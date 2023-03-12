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
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MonsterTranformListener implements Listener {

    private final TARDISWeepingAngels plugin;

    public MonsterTranformListener(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMonsterDrowned(EntityTransformEvent event) {
        if (event.getTransformReason().equals(EntityTransformEvent.TransformReason.DROWNED) && MonsterEquipment.isMonster(event.getEntity())) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> event.getTransformedEntity().remove(), 2L);
        } else if (event.getTransformReason().equals(EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED) && event.getEntity().getPersistentDataContainer().has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)) {
            Entity zombified = event.getTransformedEntity();
            if (zombified instanceof PigZombie piglin) {
                // make the entity invisible
                Bukkit.getScheduler().scheduleSyncDelayedTask(TARDISWeepingAngels.plugin, () -> {
                    PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false);
                    piglin.addPotionEffect(invisibility);
                    piglin.setAngry(true);
                    piglin.setAnger(Integer.MAX_VALUE);
                });
            }
        }
    }
}
