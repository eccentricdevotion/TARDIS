package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.BambooButton;
import org.bukkit.Sound;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WXYZModel {

    /*
    W => 1
    X => 3
    Y => 2
    Z => 4
     */
    public void setState(ItemDisplay display, TARDIS plugin, int which) {
        if (display == null) {
            return;
        }
        display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON, 1, 1);
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        switch (which) {
            case 4 -> im.setItemModel(BambooButton.WXYZ_Z.getKey());
            case 3 -> im.setItemModel(BambooButton.WXYZ_X.getKey());
            case 2 -> im.setItemModel(BambooButton.WXYZ_Y.getKey());
            case 1 -> im.setItemModel(BambooButton.WXYZ_W.getKey());
            default -> im.setItemModel(BambooButton.WXYZ_0.getKey());
        }
        is.setItemMeta(im);
        display.setItemStack(is);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, 1, 1);
            im.setItemModel(BambooButton.WXYZ_0.getKey());
            is.setItemMeta(im);
            display.setItemStack(is);
        }, 10);
    }
}
