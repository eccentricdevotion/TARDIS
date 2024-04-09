package me.eccentric_nz.TARDIS.console.models;

import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HandbrakeModel {

    public void setState(ItemDisplay display, int state) {
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        int cmd = im.getCustomModelData();
        if (state == 0) {
            cmd += 10000;
        } else {
            cmd -= 10000;
        }
        im.setCustomModelData(cmd);
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
