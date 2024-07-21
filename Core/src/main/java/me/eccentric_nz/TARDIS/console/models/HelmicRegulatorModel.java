package me.eccentric_nz.TARDIS.console.models;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HelmicRegulatorModel {

    public void setState(ItemDisplay display, int state) {
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        im.setCustomModelData(state + 2000);
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
