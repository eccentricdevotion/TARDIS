package me.eccentric_nz.TARDIS.console.models;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LightLevelModel {

    public void setState(ItemDisplay display, int state, boolean powered) {
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        int base = (powered) ? 10000 : 20000;
        im.setCustomModelData(state + base);
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
