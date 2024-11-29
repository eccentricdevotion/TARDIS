package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.GuiVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISIndexFileEntry {

    private final TARDIS plugin;
    private final TARDISInfoMenu tardisInfoMenu;
    private final ItemStack[] menu;

    public TARDISIndexFileEntry(TARDIS plugin, TARDISInfoMenu tardisInfoMenu) {
        this.plugin = plugin;
        this.tardisInfoMenu = tardisInfoMenu;
        menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        ItemStack entry = new ItemStack(Material.WRITTEN_BOOK, 1);
        ItemMeta entryMeta = entry.getItemMeta();
        entryMeta.setDisplayName(TARDISStringUtils.capitalise(tardisInfoMenu.toString()));
        entryMeta.addItemFlags(ItemFlag.values());
        entry.setItemMeta(entryMeta);
        stack[0] = entry;
        int i = 9;
        for (String key : TARDISInfoMenu.getChildren(tardisInfoMenu.toString()).keySet()) {
            ItemStack is = new ItemStack(Material.BOOK);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(key);
            is.setItemMeta(im);
            stack[i]=is;
            i++;
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setItemModel(GuiVariant.CLOSE.getKey());
        close.setItemMeta(close_im);
        stack[26] = close;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
