/*
 * Copyright (C) 2026 eccentric_nz
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TARDISIndexFileInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISIndexFileInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("TARDIS Index File", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
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
            ItemStack is = ItemStack.of(Material.BOOKSHELF, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(category.getName()));
            List<Component> lore = new ArrayList<>();
            for (String s : category.getLore().split("~")) {
                lore.add(Component.text(s));
            }
            im.lore(lore);
            is.setItemMeta(im);
            stack[i] = is;
            i++;
        }
        // close
        ItemStack close = ItemStack.of(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(close_im);
        stack[26] = close;
        return stack;
    }
}
