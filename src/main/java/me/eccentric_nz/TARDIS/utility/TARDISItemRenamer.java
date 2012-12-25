package me.eccentric_nz.TARDIS.utility;

import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISItemRenamer {

    private final ItemStack itemStack;

    public TARDISItemRenamer(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void setName(String name, boolean setlore) {
        ItemMeta im = this.itemStack.getItemMeta();
        im.setDisplayName(name);
        if (setlore) {
            ArrayList<String> lore = new ArrayList<String>();
            lore.add("Enter and exit your TARDIS");
            im.setLore(lore);
        }
        this.itemStack.setItemMeta(im);
    }
}