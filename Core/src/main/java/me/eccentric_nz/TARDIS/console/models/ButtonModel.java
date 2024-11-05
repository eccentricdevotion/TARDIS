package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Sound;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ButtonModel {

    public void setState(ItemDisplay display, TARDIS plugin) {
        if (display == null) {
            return;
        }
        display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON, 1, 1);
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        int cmd = im.getCustomModelData();
        if (cmd < 2000) {
            im.setCustomModelData(cmd + 1000);
            is.setItemMeta(im);
            display.setItemStack(is);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, 1, 1);
                im.setCustomModelData(cmd);
                is.setItemMeta(im);
                display.setItemStack(is);
            }, 10);
        } else {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, 1, 1);
                im.setCustomModelData(cmd - 1000);
                is.setItemMeta(im);
                display.setItemStack(is);
            }, 10);
        }
    }
}
