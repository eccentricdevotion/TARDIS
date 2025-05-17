package me.eccentric_nz.TARDIS.spigot;

import me.eccentric_nz.tardisregeneration.ComponentSetter;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.WeaponComponent;
import org.bukkit.inventory.meta.components.consumable.ConsumableComponent;

public class ComponentSetterSpigot implements ComponentSetter {
    @Override
    public ItemStack setConsumable(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        ConsumableComponent consumableComponent = im.getConsumable();
        consumableComponent.setAnimation(ConsumableComponent.Animation.DRINK);
        consumableComponent.setConsumeSeconds(1.6f);
        consumableComponent.setConsumeParticles(false);
        consumableComponent.setSound(Sound.ENTITY_GENERIC_DRINK);
        im.setConsumable(consumableComponent);
        is.setItemMeta(im);
        return is;
    }

    public void setWeapon(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        WeaponComponent component = im.getWeapon();
        component.setItemDamagePerAttack(8);
        im.setWeapon(component);
        is.setItemMeta(im);
    }
}
