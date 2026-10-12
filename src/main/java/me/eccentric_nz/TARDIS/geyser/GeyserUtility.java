package me.eccentric_nz.TARDIS.geyser;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Slime;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class GeyserUtility {


    public static void setSlime(TARDIS plugin, Location spawn, int id, UUID uuid) {
        spawn.getWorld().spawn(spawn.clone().add(0.5, 2, 0.5), Slime.class, (slime) -> {
            slime.setSize(2);
            slime.setAI(false);
            slime.setGravity(false);
            slime.setSilent(true);
            slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
            slime.setInvulnerable(true);
            slime.setCollidable(false);
            slime.setPersistent(true);
            slime.setRemoveWhenFarAway(false);
            slime.getPersistentDataContainer().set(plugin.getTardisIdKey(), PersistentDataType.INTEGER, id);
            slime.getPersistentDataContainer().set(plugin.getStandUuidKey(), plugin.getPersistentDataTypeUUID(), uuid);
        });
    }

    public static void removeSlime(TARDIS plugin, Location location) {
        for (Entity e : location.getWorld().getNearbyEntities(location, 1.5, 3, 1.5)) {
            if (e instanceof Slime slime && slime.getPersistentDataContainer().has(plugin.getStandUuidKey(), plugin.getPersistentDataTypeUUID())) {
                e.remove();
            }
        }
    }
}
