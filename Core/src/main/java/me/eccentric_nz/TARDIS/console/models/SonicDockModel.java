package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodels.keys.SonicItem;
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
        im.setItemModel(activate ? SonicItem.SONIC_DOCK_CHARGING.getKey() : SonicItem.SONIC_DOCK.getKey());
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
