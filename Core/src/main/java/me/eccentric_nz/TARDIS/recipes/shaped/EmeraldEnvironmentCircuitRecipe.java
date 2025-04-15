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
easy_shape:-D-,LEL,-D-
easy_ingredients.E:EMERALD
easy_ingredients.D:DIRT
easy_ingredients.L:OAK_LEAVES
hard_shape:-S-,LEL,-S-
hard_ingredients.E:EMERALD
hard_ingredients.L:OAK_LEAVES
hard_ingredients.S:STONE
result:GLOWSTONE_DUST
amount:1
*/

public class EmeraldEnvironmentCircuitRecipe {

    private final TARDIS plugin;

    public EmeraldEnvironmentCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Emerald Environment Circuit");
//        im.setItemModel(RecipeItem.EMERALD_ENVIRONMENT_CIRCUIT.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "emerald_environment_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" S ", "LEL", " S ");
            r.setIngredient('S', Material.STONE);
        } else {
            r.shape(" D ", "LEL", " D ");
            r.setIngredient('D', Material.DIRT);
        }
        r.setIngredient('E', Material.EMERALD);
        r.setIngredient('L', Material.OAK_LEAVES);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Emerald Environment Circuit", r);
    }
}
