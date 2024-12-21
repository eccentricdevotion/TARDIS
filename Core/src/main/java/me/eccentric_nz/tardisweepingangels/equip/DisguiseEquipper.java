package me.eccentric_nz.tardisweepingangels.equip;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DisguiseEquipper {

    public void setHelmetAndInvisibilty(Entity entity, ItemStack head) {
        Player p = (Player) entity;
        if (head != null) {
            p.getInventory().setHelmet(head);
        }
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false);
        p.addPotionEffect(potionEffect);
    }
}
