package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TARDISIndexFileInventory {

    private final TARDIS plugin;
    private final ItemStack[] menu;

    public TARDISIndexFileInventory(TARDIS plugin) {
        this.plugin = plugin;
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the TARDIS Index File GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        int i = 0;
        // categories
        for (TISCategory category : TISCategory.values()) {
            ItemStack is = new ItemStack(Material.BOOKSHELF, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(category.getName()));
            List<TextComponent> lore = new ArrayList<>();
            for (String s : category.getLore().split("~")) {
                lore.add(Component.text(s));
            }
            im.lore(lore);
            is.setItemMeta(im);
            stack[i] = is;
            i++;
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE")));
        close_im.setItemModel(GuiVariant.CLOSE.getKey());
        close.setItemMeta(close_im);
        stack[26] = close;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
