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
import me.eccentric_nz.TARDIS.custommodels.keys.*;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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

    public Equipper(Monster monster, LivingEntity le, boolean disguise) {
        this.monster = monster;
        this.le = le;
        this.disguise = disguise;
    }

    public void setHelmetAndInvisibility() {
        // set custom armour!
        EntityEquipment ee = setArmour(le, monster);
        // set age
        if (le instanceof Ageable ageable) {
            if (monster == Monster.EMPTY_CHILD) {
                ageable.setBaby();
            } else {
                ageable.setAdult();
            }
        }
        if (!(le instanceof Player)) {
            // make the entity invisible
            Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
                le.addPotionEffect(invisibility);
                PotionEffect resistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, false, false);
                le.addPotionEffect(resistance);
            });
        }
        // no boots
        ee.setBoots(null);
        // don't drop items when killed
        ee.setItemInMainHandDropChance(0);
        ee.setItemInOffHandDropChance(0);
        ee.setHelmetDropChance(0);
        ee.setChestplateDropChance(0);
        ee.setLeggingsDropChance(0);
        // don't pick up items
        le.setCanPickupItems(false);
        // make silent
        le.setSilent(true);
        le.setCollidable(true);
        le.setPersistent(true);
        // set TWA data
        le.getPersistentDataContainer().set(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER, monster.ordinal());
        if (disguise) {
            new DisguiseEquipper().setHelmetAndInvisibilty(le, null);
        }
    }

    private EntityEquipment setArmour(LivingEntity entity, Monster monster) {
        EntityEquipment ee = entity.getEquipment();
        new ArmourEquipper().dress(entity, monster);
        // hands / weapons if needed
        ItemStack hand = null;
        ItemStack offhand = null;
        switch (monster) {
            case ANGEL_OF_LIBERTY -> {
                // torch
                hand = new ItemStack(Material.TORCH);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(Features.ANGEL_OF_LIBERTY_TORCH.getKey());
                hand.setItemMeta(tim);
            }
            case CLOCKWORK_DROID -> {
                // key
                hand = new ItemStack(Material.GOLD_NUGGET);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(DroidVariant.CLOCKWORK_DROID_KEY.getKey());
                hand.setItemMeta(tim);
            }
            case CYBERMAN -> {
                // weapon
                hand = new ItemStack(Material.IRON_NUGGET);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(CybermanVariant.CYBER_WEAPON.getKey());
                hand.setItemMeta(tim);
            }
            case HATH -> {
                // weapon
                hand = new ItemStack(monster.getMaterial());
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(HathVariant.HATH_WEAPON.getKey());
                hand.setItemMeta(tim);
            }
            case HEADLESS_MONK -> {
                // sword
                hand = new ItemStack(Material.GOLDEN_SWORD);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(MonkVariant.HEADLESS_MONK_SWORD.getKey());
                hand.setItemMeta(tim);
            }
            case ICE_WARRIOR -> {
                // dagger
                hand = new ItemStack(Material.IRON_SWORD);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(IceWarriorVariant.ICE_WARRIOR_DAGGER.getKey());
                hand.setItemMeta(tim);
            }
            case JUDOON -> {
                // weapon
                hand = new ItemStack(Material.END_ROD);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(JudoonVariant.JUDOON_WEAPON_RESTING.getKey());
                hand.setItemMeta(tim);
            }
            case MIRE -> {
                // both hands/arms
                hand = new ItemStack(Material.NETHERITE_SCRAP);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(MireVariant.MIRE_RIGHT_ARM.getKey());
                hand.setItemMeta(tim);
                offhand = new ItemStack(Material.NETHERITE_SCRAP);
                ItemMeta oim = offhand.getItemMeta();
                oim.setItemModel(MireVariant.MIRE_LEFT_ARM.getKey());
                offhand.setItemMeta(oim);
            }
            case SEA_DEVIL -> {
                // invisible trident
                hand = new ItemStack(Material.TRIDENT, 1);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(DalekVariant.DALEK_BOW.getKey());
                hand.setItemMeta(tim);
            }
            case SILENT -> {
                // both hands
                hand = new ItemStack(Material.END_STONE_BRICK_SLAB);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(SilentVariant.SILENCE_HAND.getKey());
                hand.setItemMeta(tim);
                offhand = new ItemStack(Material.END_STONE_BRICK_SLAB);
                ItemMeta oim = offhand.getItemMeta();
                oim.setItemModel(SilentVariant.SILENCE_OFFHAND.getKey());
                offhand.setItemMeta(oim);
            }
            case SILURIAN -> {
                // gun
                hand = new ItemStack(Material.BOW, 1);
                ItemMeta bim = hand.getItemMeta();
                bim.setItemModel(SilurianVariant.SILURIAN_GUN.getKey());
                hand.setItemMeta(bim);
            }
            case SLITHEEN -> {
                // both hands
                hand = new ItemStack(Material.TURTLE_EGG);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(SlitheenVariant.SLITHEEN_CLAW_RIGHT.getKey());
                hand.setItemMeta(tim);
                offhand = new ItemStack(Material.TURTLE_EGG);
                ItemMeta oim = offhand.getItemMeta();
                oim.setItemModel(SlitheenVariant.SLITHEEN_CLAW_LEFT.getKey());
                offhand.setItemMeta(oim);
            }
            case SONTARAN -> {
                // weapon
                hand = new ItemStack(Material.END_ROD);
                ItemMeta tim = hand.getItemMeta();
                tim.setItemModel(SontaranVariant.SONTARAN_WEAPON.getKey());
                hand.setItemMeta(tim);
            }
            default -> {
            }
        }
        ee.setItemInMainHand(hand);
        ee.setItemInOffHand(offhand);
        return ee;
    }
}
