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
package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:OOO,EEE,RBC
easy_ingredients.O:COPPER_INGOT
easy_ingredients.E:BUCKET:Artron Storage Cell
easy_ingredients.R:REPEATER
easy_ingredients.B:REDSTONE
easy_ingredients.C:COMPARATOR
hard_shape:OOO,EEE,RBC
hard_ingredients.O:COPPER_BLOCK
hard_ingredients.E:BUCKET:Artron Storage Cell
hard_ingredients.R:REPEATER
hard_ingredients.B:REDSTONE_BLOCK
hard_ingredients.C:COMPARATOR
result:BUCKET
amount:1
lore:Charge Level~0
*/

public class ArtronCapacitorRecipe {

    private final TARDIS plugin;

    public ArtronCapacitorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BUCKET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Artron Capacitor");
//        im.setItemModel(RecipeItem.ARTRON_CAPACITOR.getModel());
        is.setItemMeta(im);
        // exact choice
        ItemStack storage = new ItemStack(Material.BUCKET, 1);
        ItemMeta cell = storage.getItemMeta();
        cell.setDisplayName(ChatColor.WHITE + "Artron Storage Cell");
//        cell.setItemModel(RecipeItem.ARTRON_STORAGE_CELL.getModel());
        cell.setLore(List.of("Charge Level", "0"));
        storage.setItemMeta(cell);
        NamespacedKey key = new NamespacedKey(plugin, "artron_capacitor");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("OOO", "EEE", "RBC");
        r.setIngredient('E', new RecipeChoice.ExactChoice(storage));
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('O', Material.COPPER_BLOCK);
            r.setIngredient('B', Material.REDSTONE_BLOCK);
        } else {
            r.setIngredient('O', Material.COPPER_INGOT);
            r.setIngredient('B', Material.REDSTONE);
        }
        r.setIngredient('R', Material.REPEATER);
        r.setIngredient('C', Material.COMPARATOR);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Artron Capacitor", r);
    }
}
