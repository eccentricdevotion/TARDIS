/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.chemistry.creative;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chemistry.compound.Compound;
import me.eccentric_nz.TARDIS.chemistry.compound.CompoundBuilder;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChemistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CompoundsCreativeInventory {

    private final TARDIS plugin;
    private final ItemStack[] menu;

    public CompoundsCreativeInventory(TARDIS plugin) {
        this.plugin = plugin;
        menu = getItemStack();
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
        ItemStack elements = new ItemStack(GUIChemistry.ELEMENTS.getMaterial(), 1);
        ItemMeta eim = elements.getItemMeta();
        eim.setDisplayName("Elements");
        eim.setCustomModelData(GUIChemistry.ELEMENTS.getCustomModelData());
        elements.setItemMeta(eim);
        stack[35] = elements;
        // products
        ItemStack products = new ItemStack(GUIChemistry.PRODUCTS.getMaterial(), 1);
        ItemMeta pim = products.getItemMeta();
        pim.setDisplayName("Products");
        pim.setCustomModelData(GUIChemistry.PRODUCTS.getCustomModelData());
        products.setItemMeta(pim);
        stack[44] = products;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(GUIChemistry.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stack[53] = close;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
