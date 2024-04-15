package me.eccentric_nz.TARDIS.console.models;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FlightModeModel {

    public void setState(ItemDisplay display, int state) {
        if (display == null) {
            return;
        }
        if (state < 4) {
            state = 1;
        }
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        im.setCustomModelData(state + 6000);
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
