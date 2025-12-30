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
package me.eccentric_nz.tardischemistry.element;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ElementInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;


    public ElementInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Atomic elements", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get elements
        for (Element entry : Element.values()) {
            if (i > 52) {
                break;
            }
            ItemStack is = ElementBuilder.getElement(entry);
            stack[i] = is;
            if (i % 9 == 7) {
                i += 2;
            } else {
                i++;
            }
        }
        // scroll up
        ItemStack scroll_up = ItemStack.of(GUIChemistry.SCROLL_UP.material(), 1);
        ItemMeta uim = scroll_up.getItemMeta();
        uim.displayName(Component.text("Scroll up"));
        uim.setItemModel(GUIChemistry.SCROLL_UP.key());
        scroll_up.setItemMeta(uim);
        stack[8] = scroll_up;
        // scroll down
        ItemStack scroll_down = ItemStack.of(GUIChemistry.SCROLL_DOWN.material(), 1);
        ItemMeta dim = scroll_down.getItemMeta();
        dim.displayName(Component.text("Scroll down"));
        dim.setItemModel(GUIChemistry.SCROLL_DOWN.key());
        scroll_down.setItemMeta(dim);
        stack[17] = scroll_down;
        // compounds
        ItemStack compounds = ItemStack.of(GUIChemistry.COMPOUNDS.material(), 1);
        ItemMeta cim = compounds.getItemMeta();
        cim.displayName(Component.text("Compounds"));
        cim.setItemModel(GUIChemistry.COMPOUNDS.key());
        compounds.setItemMeta(cim);
        stack[35] = compounds;
        // products
        ItemStack products = ItemStack.of(GUIChemistry.PRODUCTS.material(), 1);
        ItemMeta pim = products.getItemMeta();
        pim.displayName(Component.text("Products"));
        pim.setItemModel(GUIChemistry.PRODUCTS.key());
        products.setItemMeta(pim);
        stack[GUIChemistry.PRODUCTS.slot()] = products;
        // close
        ItemStack close = ItemStack.of(GUIChemistry.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close_im.setItemModel(GUIChemistry.CLOSE.key());
        close.setItemMeta(close_im);
        stack[53] = close;
        return stack;
    }
}
