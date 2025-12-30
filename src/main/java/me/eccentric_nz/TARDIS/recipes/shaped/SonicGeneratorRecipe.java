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
easy_shape:-S-,-F-,GRG
easy_ingredients.G:GOLD_NUGGET
easy_ingredients.R:REDSTONE
easy_ingredients.F:FLOWER_POT
easy_ingredients.S:BLAZE_ROD
hard_shape:-S-,-F-,GRG
hard_ingredients.G:GOLD_INGOT
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.F:FLOWER_POT
hard_ingredients.S:BLAZE_ROD
result:FLOWER_POT
amount:1
*/

public class SonicGeneratorRecipe {

    private final TARDIS plugin;

    public SonicGeneratorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.FLOWER_POT, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Sonic Generator"));
        im.setItemModel(RecipeItem.SONIC_GENERATOR.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "sonic_generator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" S ", " F ", "GRG");
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('G', Material.GOLD_INGOT);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.setIngredient('G', Material.GOLD_NUGGET);
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('F', Material.FLOWER_POT);
        r.setIngredient('S', Material.BLAZE_ROD);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Sonic Generator", r);
    }
}
