package me.eccentric_nz.TARDIS.chemistry.element;

import me.eccentric_nz.TARDIS.custommodeldata.GUIChemistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ElementInventory {

    private final ItemStack[] menu;

    public ElementInventory() {
        menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get elements
        for (Element entry : Element.values()) {
            if (i > 52) {
                break;
            }
            ItemStack is = ElementBuilder.getElement(entry);
            stack[i] = is;
            if (i % 9 == 7) {
                i += 2;
            } else {
                i++;
            }
        }
        // scroll up
        ItemStack scroll_up = new ItemStack(Material.ARROW, 1);
        ItemMeta uim = scroll_up.getItemMeta();
        uim.setDisplayName("Scroll up");
        uim.setCustomModelData(GUIChemistry.SCROLL_UP.getCustomModelData());
        scroll_up.setItemMeta(uim);
        stack[8] = scroll_up;
        // scroll down
        ItemStack scroll_down = new ItemStack(Material.ARROW, 1);
        ItemMeta dim = scroll_down.getItemMeta();
        dim.setDisplayName("Scroll down");
        dim.setCustomModelData(GUIChemistry.SCROLL_DOWN.getCustomModelData());
        scroll_down.setItemMeta(dim);
        stack[35] = scroll_down;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close_im.setCustomModelData(GUIChemistry.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stack[53] = close;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
