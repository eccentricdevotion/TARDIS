package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class FollowerEquipper {

    public void setOptionsAndInvisibilty(OfflinePlayer player, LivingEntity entity, Monster monster) {
        UUID uuid;
        if (player != null) {
            uuid = player.getUniqueId();
        } else {
            uuid = TARDISWeepingAngels.UNCLAIMED;
        }
        // set TWA data
        entity.getPersistentDataContainer().set(TARDISWeepingAngels.PDC_KEYS.get(monster), TARDISWeepingAngels.PersistentDataTypeUUID, entity.getUniqueId());
        entity.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, uuid);
        // make the entity invisible
        Bukkit.getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
            PotionEffect invisibility = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false);
            entity.addPotionEffect(invisibility);
            PotionEffect resistance = new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 360000, 3, false, false);
            entity.addPotionEffect(resistance);
        });
        // make silent
        entity.setSilent(true);
        entity.setCollidable(true);
        // don't remove when far away
        entity.setRemoveWhenFarAway(false);
        // don't pick up items
        entity.setCanPickupItems(false);
    }
}
