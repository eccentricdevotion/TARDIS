package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
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
        Husk husk = (Husk) entity;
        // set TWA data
        husk.getPersistentDataContainer().set(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER, 0);
        husk.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, uuid);
        EntityEquipment ee = husk.getEquipment();
        // set the helmet to the static monster model
        ee.setHelmet(head);
        // make sure the monster doesn't spawn with any armour
        ee.setChestplate(null);
        ee.setItemInMainHand(null);
        ee.setItemInOffHand(null);
        // make the entity invisible
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
            husk.addPotionEffect(invisibility);
            PotionEffect resistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, false, false);
            husk.addPotionEffect(resistance);
        });
        // make silent
        husk.setSilent(true);
        husk.setCollidable(true);
        // don't drop items when killed
        ee.setItemInMainHandDropChance(0);
        ee.setItemInOffHandDropChance(0);
        ee.setHelmetDropChance(0);
        // don't pickup items
        husk.setCanPickupItems(false);
        // make silent
    }
}
