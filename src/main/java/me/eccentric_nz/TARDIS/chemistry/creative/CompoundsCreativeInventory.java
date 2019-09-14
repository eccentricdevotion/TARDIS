package me.eccentric_nz.TARDIS.chemistry.creative;

import me.eccentric_nz.TARDIS.chemistry.compound.Compound;
import me.eccentric_nz.TARDIS.chemistry.compound.CompoundBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CompoundsCreativeInventory {

    private final ItemStack[] menu;

    public CompoundsCreativeInventory() {
        menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        for (Compound entry : Compound.values()) {
            if (i > 52) {
                break;
            }
            ItemStack is = CompoundBuilder.getCompound(entry);
            stack[i] = is;
            i++;
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close_im.setCustomModelData(10000002);
        close.setItemMeta(close_im);
        stack[53] = close;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
