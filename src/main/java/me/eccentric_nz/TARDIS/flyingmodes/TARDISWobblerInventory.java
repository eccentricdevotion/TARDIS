/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.flyingmodes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISWobblerInventory {

    private final ItemStack[] terminal;

    public TARDISWobblerInventory() {
        this.terminal = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] is = new ItemStack[54];
        for (int col = 0; col < 37; col += 9) {
            for (int row = 0; row < 5; row++) {
                int s = col + row;
                if (s != 20) {
                    is[s] = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
                }
            }
        }
        // direction pad up
        ItemStack pad_up = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta up = pad_up.getItemMeta();
        up.setDisplayName("Up");
        pad_up.setItemMeta(up);
        is[16] = pad_up;
        // wobbler
        ItemStack wobb = new ItemStack(Material.WOOL, 1, (byte) 11);
        ItemMeta ler = wobb.getItemMeta();
        ler.setDisplayName("Wobbler");
        wobb.setItemMeta(ler);
        is[20] = wobb;
        // direction pad left
        ItemStack pad_left = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta left = pad_left.getItemMeta();
        left.setDisplayName("Left");
        pad_left.setItemMeta(left);
        is[24] = pad_left;
        // direction pad right
        ItemStack pad_right = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta right = pad_right.getItemMeta();
        right.setDisplayName("Right");
        pad_right.setItemMeta(right);
        is[26] = pad_right;
        // direction pad down
        ItemStack pad_down = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta down = pad_down.getItemMeta();
        down.setDisplayName("Down");
        pad_down.setItemMeta(down);
        is[34] = pad_down;

        return is;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
