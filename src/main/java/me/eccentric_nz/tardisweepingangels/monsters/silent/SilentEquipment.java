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
package me.eccentric_nz.tardisweepingangels.monsters.silent;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SilentEquipment {

    public static void setGuardian(LivingEntity le) {
        LivingEntity g = (LivingEntity) le.getLocation().getWorld().spawnEntity(le.getLocation(), EntityType.GUARDIAN);
        g.setSilent(true);
        PotionEffect p = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false);
        g.addPotionEffect(p);
        g.getPersistentDataContainer().set(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER, Monster.SILENT.getPersist());
        g.setInvulnerable(true);
        le.addPassenger(g);
    }
}
