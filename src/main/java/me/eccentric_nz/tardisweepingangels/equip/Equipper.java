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
package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author eccentric_nz
 */
public class Equipper {

    private final Monster monster;
    private final LivingEntity le;
    private final boolean disguise;
    private final boolean bow;
    private final boolean trident;

    public Equipper(Monster monster, LivingEntity le, boolean disguise) {
        this.monster = monster;
        this.le = le;
        this.disguise = disguise;
        this.bow = false;
        this.trident = false;
    }

    public Equipper(Monster monster, LivingEntity le, boolean disguise, boolean bow) {
        this.monster = monster;
        this.le = le;
        this.disguise = disguise;
        this.bow = bow;
        this.trident = false;
    }

    public Equipper(Monster monster, LivingEntity le, boolean disguise, boolean bow, boolean trident) {
        this.monster = monster;
        this.le = le;
        this.disguise = disguise;
        this.bow = bow;
        this.trident = trident;
    }

    public void setHelmetAndInvisibilty() {
        // make a monster item
        ItemStack helmet = new ItemStack(monster.getMaterial(), 1);
        ItemMeta headMeta = helmet.getItemMeta();
        headMeta.setDisplayName(monster.getName() + " Head");
        // 405 = static model
        headMeta.setCustomModelData(405);
        helmet.setItemMeta(headMeta);
        // set equipment
        EntityEquipment ee = le.getEquipment();
        // make sure the monster doesn't spawn with any armour
        ee.setChestplate(null);
        ee.setLeggings(null);
        ee.setBoots(null);
        // set the helmet to the static monster model
        ee.setHelmet(helmet);
        // set age
        if (le instanceof Ageable ageable) {
            if (monster == Monster.EMPTY_CHILD) {
                ageable.setBaby();
            } else {
                ageable.setAdult();
            }
        }
        // make the entity invisible
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
            le.addPotionEffect(invisibility);
            PotionEffect resistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, false, false);
            le.addPotionEffect(resistance);
        });
        if (!disguise) {
            // make sure the monster doesn't spawn with items in hand unless should have a bow
            if (bow) {
                // invisible bow
                ItemStack b = new ItemStack(Material.BOW, 1);
                ItemMeta bim = b.getItemMeta();
                bim.setCustomModelData(1);
                b.setItemMeta(bim);
                ee.setItemInMainHand(b);
            } else if (trident) {
                // invisible trident
                ItemStack t = new ItemStack(Material.TRIDENT, 1);
                ItemMeta tim = t.getItemMeta();
                tim.setCustomModelData(1);
                t.setItemMeta(tim);
                ee.setItemInMainHand(t);
            } else {
                ee.setItemInMainHand(null);
            }
            ee.setItemInOffHand(null);
            // don't drop items when killed
            ee.setItemInMainHandDropChance(0);
            ee.setItemInOffHandDropChance(0);
            ee.setHelmetDropChance(0);
            // don't pickup items
            le.setCanPickupItems(false);
            // make silent
            le.setSilent(true);
            // set TWA data
            le.getPersistentDataContainer().set(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER, monster.getPersist());
        }
    }
}
