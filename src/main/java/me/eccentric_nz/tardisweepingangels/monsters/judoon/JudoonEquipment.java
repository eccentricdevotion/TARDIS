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

import java.util.UUID;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JudoonEquipment {

    public static void set(Player player, Entity entity, boolean disguise) {
        ItemStack head = new ItemStack(Material.YELLOW_DYE);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("Judoon Head");
        headMeta.setCustomModelData((disguise) ? 10 : 2);
        head.setItemMeta(headMeta);
        if (!disguise) {
            UUID uuid;
            if (player != null) {
                uuid = player.getUniqueId();
            } else {
                uuid = TARDISWeepingAngels.UNCLAIMED;
            }
            ArmorStand armorStand = (ArmorStand) entity;
            armorStand.getPersistentDataContainer().set(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER, 0);
            armorStand.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, uuid);
            ItemStack arm = new ItemStack(Material.YELLOW_DYE);
            ItemMeta armMeta = arm.getItemMeta();
            armMeta.setDisplayName("Judoon Arm");
            armMeta.setCustomModelData(3);
            arm.setItemMeta(armMeta);
            ItemStack weapon_arm = new ItemStack(Material.YELLOW_DYE);
            ItemMeta weaponMeta = weapon_arm.getItemMeta();
            weaponMeta.setDisplayName("Judoon Weapon Arm");
            weaponMeta.setCustomModelData(4);
            weapon_arm.setItemMeta(weaponMeta);
            EntityEquipment ee = armorStand.getEquipment();
            ee.setHelmet(head);
            ee.setItemInMainHand(weapon_arm);
            ee.setItemInOffHand(arm);
            armorStand.setVisible(false);
            armorStand.setSilent(true);
            armorStand.setCollidable(true);
        } else {
            Player p = (Player) entity;
            p.getInventory().setHelmet(head);
            PotionEffect potionEffect = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false);
            p.addPotionEffect(potionEffect);
        }
    }
}
