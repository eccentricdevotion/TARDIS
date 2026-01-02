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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class FollowerEquipper {

    public void setOptionsAndInvisibilty(OfflinePlayer player, LivingEntity entity, Monster monster) {
        UUID uuid;
        if (player != null) {
            uuid = player.getUniqueId();
        } else {
            uuid = TARDISWeepingAngels.UNCLAIMED;
        }
        // set TWA data
        entity.getPersistentDataContainer().set(TARDISWeepingAngels.PDC_KEYS.get(monster), TARDISWeepingAngels.PersistentDataTypeUUID, entity.getUniqueId());
        entity.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, uuid);
        // make the entity invisible
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
            entity.addPotionEffect(invisibility);
            PotionEffect resistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, false, false);
            entity.addPotionEffect(resistance);
        });
        // make silent
        entity.setSilent(true);
        entity.setCollidable(true);
        // don't remove when far away
        entity.setRemoveWhenFarAway(false);
        entity.setPersistent(true);
        // don't pick up items
        entity.setCanPickupItems(false);
    }
}
