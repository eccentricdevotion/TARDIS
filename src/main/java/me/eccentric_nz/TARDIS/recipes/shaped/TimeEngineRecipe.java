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
package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:LRL,GAG,GRG
easy_ingredients.L:LIGHT_BLUE_DYE
easy_ingredients.R:REDSTONE
easy_ingredients.A:ANVIL
easy_ingredients.G:GLASS_PANE
hard_shape:LRL,GAG,GRG
hard_ingredients.L:LIGHT_BLUE_DYE
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.A:ANVIL
hard_ingredients.G:GLASS_PANE
result:LIGHT_GRAY_DYE
amount:1
*/

public class TimeEngineRecipe {

    private final TARDIS plugin;

    public TimeEngineRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.LIGHT_GRAY_DYE, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Time Engine"));
        im.setItemModel(RecipeItem.TIME_ENGINE.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "time_engine");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("LRL", "GAG", "GRG");
        r.setIngredient('L', Material.LIGHT_BLUE_DYE);
        r.setIngredient('A', Material.ANVIL);
        r.setIngredient('G', Material.GLASS_PANE);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.setIngredient('R', Material.REDSTONE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Time Engine", r);
    }
}
