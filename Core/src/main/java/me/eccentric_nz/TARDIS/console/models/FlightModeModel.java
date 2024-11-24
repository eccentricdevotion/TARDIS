package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodeldata.keys.Lever;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FlightModeModel {

    public void setState(ItemDisplay display, int state) {
        if (display == null) {
            return;
        }
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        switch (state) {
            case 1 -> im.setItemModel(Lever.RELATIVITY_DIFFERENTIATOR_0A.getKey());
            case 2 -> im.setItemModel(Lever.RELATIVITY_DIFFERENTIATOR_1A.getKey());
            case 3 -> im.setItemModel(Lever.RELATIVITY_DIFFERENTIATOR_2A.getKey());
            default -> im.setItemModel(Lever.RELATIVITY_DIFFERENTIATOR_3A.getKey());
        }
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
