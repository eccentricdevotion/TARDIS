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

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ElementInventory implements InventoryHolder {

    private final Inventory inventory;


    public ElementInventory(TARDIS plugin) {
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
        scroll_up.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Scroll up"));
        scroll_up.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.SCROLL_UP.key());
        stack[8] = scroll_up;
        // scroll down
        ItemStack scroll_down = ItemStack.of(GUIChemistry.SCROLL_DOWN.material(), 1);
        scroll_down.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Scroll down"));
        scroll_down.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.SCROLL_DOWN.key());
        stack[17] = scroll_down;
        // compounds
        ItemStack compounds = ItemStack.of(GUIChemistry.COMPOUNDS.material(), 1);
        compounds.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Compounds"));
        compounds.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.COMPOUNDS.key());
        stack[35] = compounds;
        // products
        ItemStack products = ItemStack.of(GUIChemistry.PRODUCTS.material(), 1);
        products.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Products"));
        products.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.PRODUCTS.key());
        stack[GUIChemistry.PRODUCTS.slot()] = products;
        // close
        stack[53] = GUIItemFactory.close();
        return stack;
    }
}
