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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-E-,BRB,-E-
easy_ingredients.R:REDSTONE
easy_ingredients.E:SPIDER_EYE
easy_ingredients.B:BONE
hard_shape:-E-,BRB,-E-
hard_ingredients.R:REPEATER
hard_ingredients.E:SPIDER_EYE
hard_ingredients.B:BONE
result:GLOWSTONE_DUST
amount:1
*/

public class BioscannerCircuitRecipe {

    private final TARDIS plugin;

    public BioscannerCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Bio-scanner Circuit");
//        im.setItemModel(RecipeItem.BIO_SCANNER_CIRCUIT.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "bio-scanner_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" E ", "BRB", " E ");
            r.setIngredient('R', Material.REPEATER);
        } else {
            r.shape(" E ", "BRB", " E ");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('E', Material.SPIDER_EYE);
        r.setIngredient('B', Material.BONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Bio-scanner Circuit", r);
    }
}
