package me.eccentric_nz.TARDIS.chemistry.lab;

import me.eccentric_nz.TARDIS.custommodeldata.GUIChemistry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class LabInventory {

    private final ItemStack[] menu;

    public LabInventory() {
        menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        // info
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Info");
        info_im.setLore(Arrays.asList("Combine elements and compounds", "to create bleach, ice bombs", "heat blocks and fertiliser.", "To see a product formula", "use the " + ChatColor.GREEN + ChatColor.ITALIC + "/formula" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + " command.", "Place items in the bottom", "row from left to right."));
        info_im.setCustomModelData(GUIChemistry.INFO.getCustomModelData());
        info.setItemMeta(info_im);
        stack[8] = info;
        // check recipe
        ItemStack check = new ItemStack(Material.BOWL, 1);
        ItemMeta check_im = check.getItemMeta();
        check_im.setDisplayName("Check product");
        check_im.setCustomModelData(GUIChemistry.CHECK.getCustomModelData());
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
