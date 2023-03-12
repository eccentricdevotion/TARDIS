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
package me.eccentric_nz.tardisweepingangels.monsters.daleks;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DalekEquipment {

    private static final WeightedChoice<Integer> weightedChoice = new WeightedChoice<Integer>().add(48, 0).add(3, 1).add(3, 2).add(3, 3).add(3, 4).add(3, 5).add(3, 6).add(3, 7).add(3, 8).add(3, 9).add(3, 10).add(3, 11).add(3, 12).add(3, 13).add(3, 14).add(3, 15).add(3, 16);

    public static void set(LivingEntity le, boolean disguise) {
        le.getPersistentDataContainer().set(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER, Monster.DALEK.getPersist());
        ItemStack helmet = new ItemStack(Material.SLIME_BALL, 1);
        ItemMeta headMeta = helmet.getItemMeta();
        headMeta.setDisplayName("Dalek Head");
        headMeta.setCustomModelData(10000005 + weightedChoice.next());
        helmet.setItemMeta(headMeta);
        EntityEquipment ee = le.getEquipment();
        ee.setHelmet(helmet);
        ee.setChestplate(null);
        ee.setLeggings(null);
        ee.setBoots(null);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDISWeepingAngels.plugin, () -> {
            PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false);
            le.addPotionEffect(invisibility);
        });
        if (!disguise) {
            ee.setHelmetDropChance(0);
            ItemStack bow = new ItemStack(Material.BOW, 1);
            ItemMeta bim = bow.getItemMeta();
            bim.setCustomModelData(1);
            bow.setItemMeta(bim);
            ee.setItemInMainHand(bow);
            ee.setItemInMainHandDropChance(0);
            Bukkit.getScheduler().scheduleSyncDelayedTask(TARDISWeepingAngels.plugin, () -> {
                PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 360000, 1, true, false);
                le.addPotionEffect(resistance);
                AttributeInstance attribute = le.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                attribute.setBaseValue(30.0d);
                le.setHealth(30.0d);
                le.setCanPickupItems(false);
                le.setRemoveWhenFarAway(false);
            });
        }
    }
}
