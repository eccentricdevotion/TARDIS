package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodeldata.keys.Rail;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DirectionModel {

    public void setState(ItemDisplay display, int state) {
        if (display == null) {
            return;
        }
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        switch (state) {
            case 0 -> im.setItemModel(Rail.DIRECTION_NORTH.getKey());
            case 1 -> im.setItemModel(Rail.DIRECTION_NORTH_EAST.getKey());
            case 2 -> im.setItemModel(Rail.DIRECTION_EAST.getKey());
            case 3 -> im.setItemModel(Rail.DIRECTION_SOUTH_EAST.getKey());
            case 4 -> im.setItemModel(Rail.DIRECTION_SOUTH.getKey());
            case 5 -> im.setItemModel(Rail.DIRECTION_SOUTH_WEST.getKey());
            case 6 -> im.setItemModel(Rail.DIRECTION_WEST.getKey());
            default -> im.setItemModel(Rail.DIRECTION_NORTH_WEST.getKey());
        }
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
