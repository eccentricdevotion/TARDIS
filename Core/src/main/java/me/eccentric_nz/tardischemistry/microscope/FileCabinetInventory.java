package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

class FileCabinetInventory {

    private final TARDIS plugin;

    FileCabinetInventory(TARDIS plugin) {
        this.plugin = plugin;
    }

    ItemStack[] getItems() {
        ItemStack[] stacks = new ItemStack[54];
        // make screens
        for (ScopeView view : ScopeView.values()) {
            ItemStack is = new ItemStack(Material.GRAY_STAINED_GLASS, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(view.getName());
            im.setCustomModelData(9999);
            im.getPersistentDataContainer().set(plugin.getMicroscopeKey(), PersistentDataType.INTEGER, view.getCustomModelData());
            is.setItemMeta(im);
            stacks[view.ordinal()] = is;
        }
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName("Close");
        can.setCustomModelData(1);
        close.setItemMeta(can);
        stacks[53] = close;
        return stacks;
    }
}
