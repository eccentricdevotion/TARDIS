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
package me.eccentric_nz.tardischemistry.compound;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CompoundInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public CompoundInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("Chemical compounds", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        // info
        ItemStack info = ItemStack.of(GUIChemistry.INFO.material(), 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.displayName(Component.text("Info"));
        info_im.lore(List.of(
                Component.text("Combine elements to create"),
                Component.text("chemical compounds."),
                Component.text("To see a compound formula"),
                Component.text("use the ")
                    .append(Component.text("/tardischemistry formula", NamedTextColor.GREEN).decorate(TextDecoration.ITALIC))
                    .append(Component.text(" command.", NamedTextColor.DARK_PURPLE).decorate(TextDecoration.ITALIC)),
                Component.text("Place items in the bottom"),
                Component.text("row from left to right.")
        ));
        info_im.setItemModel(GUIChemistry.INFO.key());
        info.setItemMeta(info_im);
        stack[GUIChemistry.INFO.slot()] = info;
        // check formula
        ItemStack check = ItemStack.of(GUIChemistry.CHECK.material(), 1);
        ItemMeta check_im = check.getItemMeta();
        check_im.displayName(Component.text("Check formula"));
        check_im.setItemModel(GUIChemistry.CHECK.key());
        check.setItemMeta(check_im);
        stack[GUIChemistry.CHECK.slot()] = check;
        // close
        ItemStack close = ItemStack.of(GUIChemistry.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close_im.setItemModel(GUIChemistry.CLOSE.key());
        close.setItemMeta(close_im);
        stack[GUIChemistry.CLOSE.slot()] = close;
        return stack;
    }
}
