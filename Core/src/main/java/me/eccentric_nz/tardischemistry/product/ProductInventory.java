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
package me.eccentric_nz.tardischemistry.product;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ProductInventory {

    private final ItemStack[] menu;
    private final TARDIS plugin;

    public ProductInventory(TARDIS plugin) {
        this.plugin = plugin;
        menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        // info
        ItemStack info = new ItemStack(GUIChemistry.INFO.material(), 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Info");
        info_im.setLore(List.of("Combine elements and compounds", "to create sparklers, balloons,", "lamps, and glow sticks.", "To see a product formula", "use the " + ChatColor.GREEN + ChatColor.ITALIC + "/tardischemistry formula" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + " command.", "Place items like you would", "in a crafting table", "in the 9 left slots."));
        info_im.setItemModel(GUIChemistry.INFO.key());
        info.setItemMeta(info_im);
        stack[GUIChemistry.INFO.slot()] = info;
        // craft recipe
        ItemStack craft = new ItemStack(GUIChemistry.CRAFT.material(), 1);
        ItemMeta craft_im = craft.getItemMeta();
        craft_im.setDisplayName("Craft");
        craft_im.setItemModel(GUIChemistry.CRAFT.key());
        craft.setItemMeta(craft_im);
        stack[GUIChemistry.CRAFT.slot()] = craft;
        // close
        ItemStack close = new ItemStack(GUIChemistry.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setItemModel(GUIChemistry.CLOSE.key());
        close.setItemMeta(close_im);
        stack[GUIChemistry.CLOSE.slot()] = close;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
