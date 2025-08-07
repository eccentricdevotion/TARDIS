package me.eccentric_nz.TARDIS.mobfarming;

import org.bukkit.inventory.ItemStack;

public class TARDISBoat extends TARDISMob {

    private ItemStack[] items;

    public ItemStack[] getItems() {
        return items;
    }

    public void setItems(ItemStack[] items) {
        this.items = items;
    }
}
