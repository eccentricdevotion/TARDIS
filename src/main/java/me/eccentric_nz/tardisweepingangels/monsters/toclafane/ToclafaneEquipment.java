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
package me.eccentric_nz.tardisweepingangels.monsters.toclafane;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ToclafaneEquipment {

    public static void set(Entity entity, boolean disguise) {
        ItemStack head = new ItemStack(Material.GUNPOWDER);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("Toclafane");
        headMeta.setCustomModelData((disguise) ? 3 : 2);
        head.setItemMeta(headMeta);
        if (!disguise) {
            ArmorStand armorStand = (ArmorStand) entity;
            Location location = armorStand.getLocation();
            int difficulty = (location.getWorld().getDifficulty().ordinal() * 6) + 1;
            armorStand.getPersistentDataContainer().set(TARDISWeepingAngels.TOCLAFANE, PersistentDataType.INTEGER, difficulty);
            armorStand.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, TARDISWeepingAngels.UNCLAIMED);
            armorStand.getEquipment().setHelmet(head);
            armorStand.setVisible(false);
            armorStand.setSilent(true);
            armorStand.setCollidable(true);
            Bee bee = (Bee) location.getWorld().spawnEntity(location, EntityType.BEE);
            bee.setCannotEnterHiveTicks(Integer.MAX_VALUE);
            PotionEffect p = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false);
            bee.addPotionEffect(p);
            bee.addPassenger(entity);
            bee.setSilent(true);
        } else {
            Player p = (Player) entity;
            p.getInventory().setHelmet(head);
            PotionEffect potionEffect = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false);
            p.addPotionEffect(potionEffect);
        }
    }
}
