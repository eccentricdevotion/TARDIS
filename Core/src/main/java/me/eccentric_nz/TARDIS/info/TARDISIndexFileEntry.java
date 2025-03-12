/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.info;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

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
        entryMeta.setAttributeModifiers(Multimaps.forMap(Map.of()));
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
