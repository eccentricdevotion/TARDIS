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
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class TARDISIndexFileEntry implements InventoryHolder {

    private final TARDIS plugin;
    private final TARDISInfoMenu tardisInfoMenu;
    private final Inventory inventory;

    public TARDISIndexFileEntry(TARDIS plugin, TARDISInfoMenu tardisInfoMenu) {
        this.plugin = plugin;
        this.tardisInfoMenu = tardisInfoMenu;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("TARDIS Info Entry", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        ItemStack entry = ItemStack.of(Material.WRITTEN_BOOK, 1);
        ItemMeta entryMeta = entry.getItemMeta();
        entryMeta.displayName(Component.text(TARDISStringUtils.capitalise(tardisInfoMenu.toString())));
        entryMeta.addItemFlags(ItemFlag.values());
        entryMeta.setAttributeModifiers(Multimaps.forMap(Map.of()));
        entry.setItemMeta(entryMeta);
        stack[0] = entry;
        int i = 9;
        for (String key : TARDISInfoMenu.getChildren(tardisInfoMenu.toString()).keySet()) {
            ItemStack is = ItemStack.of(Material.BOOK);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(key));
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
