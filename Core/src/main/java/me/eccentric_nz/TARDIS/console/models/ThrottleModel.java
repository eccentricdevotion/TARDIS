package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodels.keys.ModelledControl;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ThrottleModel {

    public void setState(ItemDisplay display, int state) {
        if (display == null) {
            return;
        }
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        switch (state) {
            case 1 -> im.setItemModel(ModelledControl.THROTTLE_WARP.getKey());
            case 2 -> im.setItemModel(ModelledControl.THROTTLE_RAPID.getKey());
            case 3 -> im.setItemModel(ModelledControl.THROTTLE_FASTER.getKey());
            default -> im.setItemModel(ModelledControl.THROTTLE_NORMAL.getKey());
        }
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
