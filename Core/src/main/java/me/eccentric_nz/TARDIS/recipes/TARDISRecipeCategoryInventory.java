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
package me.eccentric_nz.TARDIS.recipes;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.custommodels.keys.KeyVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;
import java.util.Map;

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
        info_im.setLore(List.of("Click a button below", "to see the items", "in that recipe category"));
        info.setItemMeta(info_im);
        stack[0] = info;
        for (RecipeCategory category : RecipeCategory.values()) {
            if (!category.equals(RecipeCategory.UNUSED) && category != RecipeCategory.UNCRAFTABLE && category != RecipeCategory.CHEMISTRY) {
                ItemStack cat = new ItemStack(category.getMaterial(), 1);
                ItemMeta egory = cat.getItemMeta();
                egory.setDisplayName(category.getName());
                if (category == RecipeCategory.ITEMS) {
                    CustomModelDataComponent component = egory.getCustomModelDataComponent();
                    component.setFloats(KeyVariant.BRASS_STRING.getFloats());
                    egory.setCustomModelDataComponent(component);
                }
                if (category == RecipeCategory.SONIC_UPGRADES) {
                    CustomModelDataComponent component = egory.getCustomModelDataComponent();
                    component.setFloats(SonicVariant.NINTH.getFloats());
                    egory.setCustomModelDataComponent(component);
                }
                if (category == RecipeCategory.ROTORS) {
                    egory.setItemModel(category.getModel());
                }
                egory.addItemFlags(ItemFlag.values());
                egory.setAttributeModifiers(Multimaps.forMap(Map.of()));
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
