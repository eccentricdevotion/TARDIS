package me.eccentric_nz.tardisregeneration;

import org.bukkit.inventory.ItemStack;

public interface ComponentSetter {

    ItemStack setConsumable(ItemStack is);

    void setWeapon(ItemStack is);
}
