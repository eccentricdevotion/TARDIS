package me.eccentric_nz.TARDIS.chemistry.lab;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LabBuilder {

    public static ItemStack getLabProduct(Lab lab) {
        ItemStack is = new ItemStack(lab.getItemMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(lab.toString().replace("_", " "));
        im.setCustomModelData(10000001 + lab.ordinal());
        is.setItemMeta(im);
        return is;
    }
}
