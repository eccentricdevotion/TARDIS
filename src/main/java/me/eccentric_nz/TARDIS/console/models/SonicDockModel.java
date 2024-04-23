package me.eccentric_nz.TARDIS.console.models;

import org.bukkit.Sound;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SonicDockModel {

    public void setState(ItemDisplay display, boolean activate) {
        if (display == null) {
            return;
        }
        display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON, 1, 1);
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        int cmd = im.getCustomModelData();
        im.setCustomModelData(activate?2001:2000);
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
