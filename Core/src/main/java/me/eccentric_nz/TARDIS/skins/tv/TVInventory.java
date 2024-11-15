package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.custommodeldata.GUIData;
import me.eccentric_nz.TARDIS.custommodeldata.GUITelevision;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TVInventory {

    private final ItemStack[] menu;

    public TVInventory() {
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Television GUI.
     *
     * @return an Array of item stacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[36];
        /*
        Doctors,
        Companions,
        Characters,
        Monsters,
        Close
         */
        for (GUIData tv : GUITelevision.values()) {
            ItemStack is = new ItemStack(tv.material(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(tv.name());
            im.setItemModel(tv.key());
            is.setItemMeta(im);
            stack[tv.slot()] = is;
        }
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
