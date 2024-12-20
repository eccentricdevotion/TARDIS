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
package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.DalekVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SilurianVariant;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
    private final boolean variant;
    private final boolean trident;

    public Equipper(Monster monster, LivingEntity le, boolean disguise) {
        this.monster = monster;
        this.le = le;
        this.disguise = disguise;
        this.variant = false;
        this.trident = false;
    }

    public Equipper(Monster monster, LivingEntity le, boolean disguise, boolean variant) {
        this.monster = monster;
        this.le = le;
        this.disguise = disguise;
        this.variant = variant;
        this.trident = false;
    }

    public Equipper(Monster monster, LivingEntity le, boolean disguise, boolean variant, boolean trident) {
        this.monster = monster;
        this.le = le;
        this.disguise = disguise;
        this.variant = variant;
        this.trident = trident;
    }

    public void setHelmetAndInvisibility() {
        setHelmetAndInvisibility(monster.getModel());
    }

    public void setHelmetAndInvisibility(NamespacedKey variant) {
        // make a monster item
        ItemStack helmet = new ItemStack(monster.getMaterial(), 1);
        ItemMeta headMeta = helmet.getItemMeta();
        headMeta.setDisplayName(monster.getName() + " Head");
        // static model
        headMeta.setItemModel(variant);
        helmet.setItemMeta(headMeta);
        // set equipment
        // TODO set custom armour!
        EntityEquipment ee = setArmour(le, monster, this.variant);
//        ee.setChestplate(null);
//        ee.setLeggings(null);
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
            // TODO move this to setArmour() method
            // make sure the monster doesn't spawn with items in hand unless should have a bow / trident
            ee.setItemInMainHand(null);
            ee.setItemInOffHand(null);
            // don't drop items when killed
            ee.setItemInMainHandDropChance(0);
            ee.setItemInOffHandDropChance(0);
            ee.setHelmetDropChance(0);
            // don't pick up items
            le.setCanPickupItems(false);
            // make silent
            le.setSilent(true);
            le.setCollidable(true);
            le.setPersistent(true);
            // set TWA data
            le.getPersistentDataContainer().set(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER, monster.getPersist());
        } else {
            new DisguiseEquipper().setHelmetAndInvisibilty(le, helmet);
        }
    }

    private EntityEquipment setArmour(LivingEntity entity, Monster monster, boolean female) {
        EntityEquipment ee = entity.getEquipment();
        // head
        ItemStack helmet = new ItemStack(monster.getMaterial(), 1);
        ItemMeta headMeta = helmet.getItemMeta();
        headMeta.setDisplayName(monster.getName() + " Head");
        headMeta.setItemModel(monster.getHeadModel());
        helmet.setItemMeta(headMeta);
        // chest / arms

        // legs
        
        switch (monster) {
            case CLOCKWORK_DROID -> {
                if (female) {
                    // use female skin
                    ee.setChestplate(null);
                    ee.setLeggings(null);
                } else {
                    ee.setChestplate(null);
                    ee.setLeggings(null);
                }
            }
            case CYBERMAN -> {
                // choose a random variant
            }
            case DALEK -> {
            }
            case EMPTY_CHILD -> {
            }
            case HATH -> {
            }
            case HEADLESS_MONK -> {
            }
            case ICE_WARRIOR -> {
            }
            case MIRE -> {
            }
            case OSSIFIED -> {
            }
            case RACNOSS -> {
            }
            case SCARECROW -> {
            }
            case SEA_DEVIL -> {
                ItemStack t = new ItemStack(Material.TRIDENT, 1);
                ItemMeta tim = t.getItemMeta();
                tim.setItemModel(DalekVariant.DALEK_BOW.getKey());
                t.setItemMeta(tim);
                ee.setItemInMainHand(t);
            }
            case SILENT -> {
            }
            case SILURIAN -> {
                // invisible bow
                ItemStack b = new ItemStack(Material.BOW, 1);
                ItemMeta bim = b.getItemMeta();
                bim.setItemModel(SilurianVariant.SILURIAN_GUN.getKey());
                b.setItemMeta(bim);
                ee.setItemInMainHand(b);
            }
            case SLITHEEN -> {
            }
            case SONTARAN -> {
            }
            case STRAX -> {
            }
            case SYCORAX -> {
            }
            case VASHTA_NERADA -> {
            }
            case WEEPING_ANGEL -> {
            }
            case ZYGON -> {
            }
        }
        ee.setHelmet(null);
        ee.setChestplate(null);
        ee.setLeggings(null);
        return ee;
    }
}
