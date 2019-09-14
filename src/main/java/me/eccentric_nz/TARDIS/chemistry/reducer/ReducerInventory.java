package me.eccentric_nz.TARDIS.chemistry.reducer;

import me.eccentric_nz.TARDIS.custommodeldata.GUIChemistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ReducerInventory {

    private final ItemStack[] menu;

    public ReducerInventory() {
        menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        // info
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Info");
        info_im.setLore(Arrays.asList("Reduce a substance to its", "component elements.", "Place an item in the first slot,", "then click the reduce button."));
        info_im.setCustomModelData(GUIChemistry.INFO.getCustomModelData());
        info.setItemMeta(info_im);
        stack[8] = info;
        // check formula
        ItemStack check = new ItemStack(Material.BOWL, 1);
        ItemMeta check_im = check.getItemMeta();
        check_im.setDisplayName("Reduce");
        check_im.setCustomModelData(GUIChemistry.REDUCE.getCustomModelData());
        check.setItemMeta(check_im);
        stack[17] = check;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close_im.setCustomModelData(GUIChemistry.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stack[26] = close;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
