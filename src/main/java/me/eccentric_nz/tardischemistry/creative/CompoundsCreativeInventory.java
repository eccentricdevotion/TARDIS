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
package me.eccentric_nz.tardischemistry.creative;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.tardischemistry.compound.Compound;
import me.eccentric_nz.tardischemistry.compound.CompoundBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CompoundsCreativeInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public CompoundsCreativeInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Molecular compounds", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        for (Compound entry : Compound.values()) {
            if (i > 52) {
                break;
            }
            ItemStack is = CompoundBuilder.getCompound(entry);
            stack[i] = is;
            if (i % 9 == 7) {
                i += 2;
            } else {
                i++;
            }
        }
        // elements
        ItemStack elements = ItemStack.of(GUIChemistry.ELEMENTS.material(), 1);
        elements.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Elements"));
        elements.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.ELEMENTS.key());
        stack[GUIChemistry.ELEMENTS.slot()] = elements;
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
