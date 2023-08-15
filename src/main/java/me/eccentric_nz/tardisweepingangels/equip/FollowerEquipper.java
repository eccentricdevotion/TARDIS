package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class FollowerEquipper {

    public void setHelmetAndInvisibilty(Player player, Entity entity, Monster monster, ItemStack head) {
        UUID uuid;
        if (player != null) {
            uuid = player.getUniqueId();
        } else {
            uuid = TARDISWeepingAngels.UNCLAIMED;
        }
        Skeleton skeleton = (Skeleton) entity;
        // set TWA data
        skeleton.getPersistentDataContainer().set(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER, 0);
        skeleton.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, uuid);
        EntityEquipment ee = skeleton.getEquipment();
        // set the helmet to the static monster model
        ee.setHelmet(head);
        // make sure the monster doesn't spawn with any armour
        ee.setChestplate(null);
        ee.setItemInMainHand(null);
        ee.setItemInOffHand(null);
        // make the entity invisible
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
            skeleton.addPotionEffect(invisibility);
            PotionEffect resistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, false, false);
            skeleton.addPotionEffect(resistance);
        });
        // make silent
        skeleton.setSilent(true);
        skeleton.setCollidable(true);
        // don't drop items when killed
        ee.setItemInMainHandDropChance(0);
        ee.setItemInOffHandDropChance(0);
        ee.setHelmetDropChance(0);
        // don't pickup items
        skeleton.setCanPickupItems(false);
        // make silent
    }
}
