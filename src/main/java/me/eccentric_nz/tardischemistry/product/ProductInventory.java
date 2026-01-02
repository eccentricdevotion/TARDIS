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
package me.eccentric_nz.tardischemistry.product;

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

public class ProductInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public ProductInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("Product crafting", NamedTextColor.DARK_RED));
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
                Component.text("Combine elements and compounds"),
                Component.text("to create sparklers, balloons,"),
                Component.text("lamps, and glow sticks."),
                Component.text("To see a product formula"),
                Component.text("use the ")
                    .append(Component.text("/tardischemistry formula", NamedTextColor.GREEN).decorate(TextDecoration.ITALIC)
                    .append(Component.text(" command.", NamedTextColor.DARK_PURPLE).decorate(TextDecoration.ITALIC))
                ),
                Component.text("Place items like you would"),
                Component.text("in a crafting table"),
                Component.text("in the 9 left slots.")
        ));
        info_im.setItemModel(GUIChemistry.INFO.key());
        info.setItemMeta(info_im);
        stack[GUIChemistry.INFO.slot()] = info;
        // craft recipe
        ItemStack craft = ItemStack.of(GUIChemistry.CRAFT.material(), 1);
        ItemMeta craft_im = craft.getItemMeta();
        craft_im.displayName(Component.text("Craft"));
        craft_im.setItemModel(GUIChemistry.CRAFT.key());
        craft.setItemMeta(craft_im);
        stack[GUIChemistry.CRAFT.slot()] = craft;
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
