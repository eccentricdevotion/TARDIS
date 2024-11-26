/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.custommodeldata.GUIChemistry;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TARDISRecipeCategoryInventory {

    private final ItemStack[] menu;

    public TARDISRecipeCategoryInventory() {
        menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[36];
        // info
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Info");
        info_im.setLore(Arrays.asList("Click a button below", "to see the items", "in that recipe category"));
        info_im.setItemModel(GUIChemistry.INFO.key());
        info.setItemMeta(info_im);
        stack[0] = info;
        for (RecipeCategory category : RecipeCategory.values()) {
            if (!category.equals(RecipeCategory.UNUSED) && category != RecipeCategory.UNCRAFTABLE && category != RecipeCategory.CHEMISTRY) {
                ItemStack cat = new ItemStack(category.getMaterial(), 1);
                ItemMeta egory = cat.getItemMeta();
                egory.setDisplayName(category.getName());
                if (category.getModel() != null) {
                    egory.setItemModel(category.getModel());
                }
                egory.addItemFlags(ItemFlag.values());
                cat.setItemMeta(egory);
                stack[category.getSlot()] = cat;
            }
        }
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
