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
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-R-,DSD,-R-
easy_ingredients.R:REDSTONE
easy_ingredients.D:DIAMOND
easy_ingredients.S:SHEARS
hard_shape:-O-,DSD,-O-
hard_ingredients.O:OBSIDIAN
hard_ingredients.D:DIAMOND
hard_ingredients.S:SHEARS
result:GLOWSTONE_DUST
amount:1
*/

public class DiamondDisruptorCircuitRecipe {

    private final TARDIS plugin;

    public DiamondDisruptorCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Diamond Disruptor Circuit");
        im.setItemModel(RecipeItem.DIAMOND_DISRUPTOR_CIRCUIT.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "diamond_disruptor_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" O ", "DSD", " O ");
            r.setIngredient('O', Material.OBSIDIAN);
        } else {
            r.shape(" R ", "DSD", " R ");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('D', Material.DIAMOND);
        r.setIngredient('S', Material.SHEARS);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Diamond Disruptor Circuit", r);
    }
}
