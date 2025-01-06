package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ButtonModel {

    public void setState(ItemDisplay display, TARDIS plugin, ConsoleInteraction interaction) {
        if (display == null) {
            return;
        }
        display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON, 1, 1);
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        NamespacedKey model = im.getItemModel();
        if (model == null) {
            model = interaction.getCustomModel();
        } else if (model.getKey().endsWith("_0")) {
            NamespacedKey pressed = new NamespacedKey(plugin, model.getKey().replace("_0", "_1"));
            im.setItemModel(pressed);
            is.setItemMeta(im);
            display.setItemStack(is);
        } else {
            model = new NamespacedKey(plugin, model.getKey().replace("_1", "_0"));
        }
        NamespacedKey released = model;
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            display.getWorld().playSound(display, Sound.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, 1, 1);
            im.setItemModel(released);
            is.setItemMeta(im);
            display.setItemStack(is);
        }, 10);
    }
}
