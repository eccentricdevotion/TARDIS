package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

class ComputerInventory {

    private final TARDIS plugin;

    ComputerInventory(TARDIS plugin) {
        this.plugin = plugin;
    }

    ItemStack[] getItems() {
        ItemStack[] stacks = new ItemStack[54];
        // make screens
        for (Screen screen : Screen.values()) {
            ItemStack is = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(screen.getName());
            im.setCustomModelData(9999);
            im.getPersistentDataContainer().set(plugin.getMicroscopeKey(), PersistentDataType.INTEGER, screen.getCustomModelData());
            is.setItemMeta(im);
            stacks[screen.ordinal()] = is;
        }
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName("Close");
        can.setCustomModelData(10000);
        close.setItemMeta(can);
        stacks[53] = close;
        return stacks;
    }
}
