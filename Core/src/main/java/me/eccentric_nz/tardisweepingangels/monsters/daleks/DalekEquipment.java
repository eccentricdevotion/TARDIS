/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.keys.DalekVariant;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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

    public static void set(LivingEntity le, boolean disguise) {
        le.getPersistentDataContainer().set(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER, Monster.DALEK.ordinal());
        ItemStack helmet = new ItemStack(Material.SLIME_BALL, 1);
        ItemMeta headMeta = helmet.getItemMeta();
        headMeta.setDisplayName("Dalek Head");
        headMeta.setItemModel(getRandomModel());
        helmet.setItemMeta(headMeta);
        EntityEquipment ee = le.getEquipment();
        ee.setHelmet(helmet);
        ee.setChestplate(null);
        ee.setLeggings(null);
        ee.setBoots(null);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false);
            le.addPotionEffect(invisibility);
            PotionEffect resistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, true, false);
            le.addPotionEffect(resistance);
        });
        if (!disguise) {
            ee.setHelmetDropChance(0);
            ItemStack bow = new ItemStack(Material.BOW, 1);
            ItemMeta bim = bow.getItemMeta();
            bim.setItemModel(DalekVariant.DALEK_BOW.getKey());
            bow.setItemMeta(bim);
            ee.setItemInMainHand(bow);
            ee.setItemInMainHandDropChance(0);
            Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                PotionEffect resistance = new PotionEffect(PotionEffectType.RESISTANCE, 360000, 1, true, false);
                le.addPotionEffect(resistance);
                AttributeInstance attribute = le.getAttribute(Attribute.MAX_HEALTH);
                attribute.setBaseValue(30.0d);
                le.setHealth(30.0d);
                le.setCanPickupItems(false);
                le.setRemoveWhenFarAway(false);
            });
        }
    }

    private static NamespacedKey getRandomModel() {
        if (TARDISConstants.RANDOM.nextBoolean()) {
            return DalekVariant.DALEK_BRASS.getKey();
        } else {
            switch (TARDISConstants.RANDOM.nextInt(1,17)) {
                case 1 -> { return DalekVariant.DALEK_WHITE.getKey(); }
                case 2 -> { return DalekVariant.DALEK_ORANGE.getKey(); }
                case 3 -> { return DalekVariant.DALEK_MAGENTA.getKey(); }
                case 4 -> { return DalekVariant.DALEK_LIGHT_BLUE.getKey(); }
                case 5 -> { return DalekVariant.DALEK_YELLOW.getKey(); }
                case 6 -> { return DalekVariant.DALEK_LIME.getKey(); }
                case 7 -> { return DalekVariant.DALEK_PINK.getKey(); }
                case 8 -> { return DalekVariant.DALEK_GRAY.getKey(); }
                case 9 -> { return DalekVariant.DALEK_LIGHT_GRAY.getKey(); }
                case 10 -> { return DalekVariant.DALEK_CYAN.getKey(); }
                case 11 -> { return DalekVariant.DALEK_BLUE.getKey(); }
                case 12 -> { return DalekVariant.DALEK_PURPLE.getKey(); }
                case 13 -> { return DalekVariant.DALEK_GREEN.getKey(); }
                case 14 -> { return DalekVariant.DALEK_BROWN.getKey(); }
                case 15 -> { return DalekVariant.DALEK_RED.getKey(); }
                case 16 -> { return DalekVariant.DALEK_BLACK.getKey(); }
            }
        }
        return DalekVariant.DALEK_BRASS.getKey();
    }
}
