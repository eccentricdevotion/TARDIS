/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.flyingmodes;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISWobblerSlot {

    public final List<Integer> bounds = Arrays.asList(new Integer[]{
        0, 1, 2, 3, 4,
        9, 10, 11, 12, 13,
        18, 19, 20, 21, 22,
        27, 28, 29, 30, 31,
        36, 37, 38, 39, 40
    });
    public final ItemStack box;
    public final ItemStack vortex;

    public TARDISWobblerSlot() {
        this.box = new ItemStack(Material.WOOL, 1, (byte) 11);
        ItemMeta ler = this.box.getItemMeta();
        ler.setDisplayName("Wobbler");
        this.box.setItemMeta(ler);
        this.vortex = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
    }

    public int upSlot(int current_slot) {
        return current_slot - 9;
    }

    public int leftSlot(int current_slot) {
        return current_slot - 1;
    }

    public int rightSlot(int current_slot) {
        return current_slot + 1;
    }

    public int downSlot(int current_slot) {
        return current_slot + 9;
    }
}
