package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.TARDIS.custommodeldata.keys.ChemistryEquipment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

class SlideInventory {

    private final TARDIS plugin;

    SlideInventory(TARDIS plugin) {
        this.plugin = plugin;
    }

    ItemStack[] getItems() {
        ItemStack[] stacks = new ItemStack[54];
        // make slides
        for (Slide slide : Slide.values()) {
            ItemStack is = new ItemStack(Material.GLASS, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(slide.getName());
            im.setItemModel(ChemistryEquipment.GLASS_SLIDE.getKey());
            im.getPersistentDataContainer().set(plugin.getMicroscopeKey(), PersistentDataType.INTEGER, slide.getCustomModelData());
            is.setItemMeta(im);
            stacks[slide.ordinal()] = is;
        }
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName("Close");
        can.setItemModel(Bowl.CLOSE.getKey());
        close.setItemMeta(can);
        stacks[53] = close;
        return stacks;
    }
}
