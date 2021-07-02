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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChemistry;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class TARDISRecipeInventory {

    private final TARDIS plugin;
    private final ItemStack[] menu;
    private final RecipeCategory category;
    private final HashMap<String, String> recipeItems = new HashMap<>();

    TARDISRecipeInventory(TARDIS plugin, RecipeCategory category) {

        for (RecipeItem recipeItem : RecipeItem.values()) {
            if (recipeItem.getCategory() != RecipeCategory.UNUSED && recipeItem.getCategory() != RecipeCategory.UNCRAFTABLE) {
                recipeItems.put(recipeItem.toString(), recipeItem.toTabCompletionString());
            }
        }
        this.plugin = plugin;
        this.category = category;
        menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        // back
        ItemStack back = new ItemStack(Material.BOWL, 1);
        ItemMeta but = back.getItemMeta();
        assert but != null;
        but.setDisplayName("Back");
        but.setCustomModelData(GUIChameleonPresets.BACK.getCustomModelData());
        back.setItemMeta(but);
        stack[0] = back;
        // info
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta info_im = info.getItemMeta();
        assert info_im != null;
        info_im.setDisplayName("Info");
        info_im.setLore(Arrays.asList("Click a button below", "to see the recipe", "for that item"));
        info_im.setCustomModelData(GUIChemistry.INFO.getCustomModelData());
        info.setItemMeta(info_im);
        stack[4] = info;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        assert close_im != null;
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(GUIChemistry.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stack[8] = close;
        int i = 9;
        for (RecipeItem item : RecipeItem.values()) {
            if (item.getCategory() == category) {
                String arg = recipeItems.get(item.toString());
                String str = item.toRecipeString();
                if (arg != null) {
                    ItemStack result;
                    if (isShapeless(str)) {
                        ShapelessRecipe shapeless = plugin.getIncomposita().getShapelessRecipes().get(str);
                        result = shapeless.getResult();
                    } else {
                        ShapedRecipe shaped = plugin.getFigura().getShapedRecipes().get(str);
                        result = shaped.getResult();
                    }
                    ItemMeta im = result.getItemMeta();
                    assert im != null;
                    im.setDisplayName(str);
                    im.setLore(Collections.singletonList("/trecipe " + arg));
                    im.setCustomModelData(item.getCustomModelData());
                    im.addItemFlags(ItemFlag.values());
                    result.setItemMeta(im);
                    stack[i] = result;
                    i++;
                }
            }
        }
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }

    private boolean isShapeless(String s) {
        return !s.equals("Blank Storage Disk") && !s.equals("Authorised Control Disk") && (s.contains("Jelly") || s.contains("Disk") || s.equals("Bowl of Custard") || s.equals("TARDIS Schematic Wand") || s.contains("Upgrade"));
    }
}
