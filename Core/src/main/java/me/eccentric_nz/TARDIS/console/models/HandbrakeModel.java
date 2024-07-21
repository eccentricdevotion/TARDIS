package me.eccentric_nz.TARDIS.console.models;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HandbrakeModel {

    public void setState(ItemDisplay display, int state) {
        if (display == null) {
            return;
        }
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        int cmd;
        if (state == 0) {
            cmd = 5001;
        } else {
            cmd = 5002;
        }
        im.setCustomModelData(cmd);
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
