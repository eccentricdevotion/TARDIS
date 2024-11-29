package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodeldata.keys.ModelledControl;
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
        if (state == 0) {
            im.setItemModel(ModelledControl.HANDBRAKE_0.getKey());
        } else {
            im.setItemModel(ModelledControl.HANDBRAKE_1.getKey());
        }
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
