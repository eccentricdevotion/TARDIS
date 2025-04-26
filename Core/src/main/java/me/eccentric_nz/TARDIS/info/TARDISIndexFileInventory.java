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

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            im.setDisplayName(category.getName());
            im.setLore(List.of(category.getLore().split("~")));
            is.setItemMeta(im);
            stack[i] = is;
            i++;
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(close_im);
        stack[26] = close;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
