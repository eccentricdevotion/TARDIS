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
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:CRC,GWG,GRG
easy_ingredients.C:<configured material>
easy_ingredients.R:REDSTONE
easy_ingredients.W:CLOCK
easy_ingredients.G:GLASS_PANE
hard_shape:CRC,GWG,GRG
hard_ingredients.C:<configured material>
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.W:CLOCK
hard_ingredients.G:GLASS_PANE
result:LIGHT_GRAY_DYE
amount:1
*/

public class TimeRotorCustomRecipe {

    private final TARDIS plugin;

    public TimeRotorCustomRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipes() {
        for (String r : plugin.getCustomRotorsConfig().getKeys(false)) {
            try {
                Material material = Material.valueOf(plugin.getCustomRotorsConfig().getString(r + ".animated_material"));
                ItemStack is = ItemStack.of(Material.LIGHT_GRAY_DYE, 1);
                ItemMeta im = is.getItemMeta();
                String dn = TARDISStringUtils.capitalise(r);
                im.displayName(ComponentUtils.toWhite("Time Rotor " + dn));
                im.setItemModel(new NamespacedKey(plugin, "time_rotor_" + r + "_off"));
                is.setItemMeta(im);
                NamespacedKey key = new NamespacedKey(plugin, "time_rotor_" + r);
                ShapedRecipe recipe = new ShapedRecipe(key, is);
                recipe.shape("CRC", "GWG", "GRG");
                recipe.setIngredient('C', material);
                recipe.setIngredient('W', Material.CLOCK);
                recipe.setIngredient('G', Material.GLASS_PANE);
                if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
                    recipe.setIngredient('R', Material.REDSTONE_BLOCK);
                } else {
                    recipe.setIngredient('R', Material.REDSTONE);
                }
                plugin.getServer().addRecipe(recipe);
                plugin.getFigura().getShapedRecipes().put("Time Rotor " + dn, recipe);
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid custom rotor item material for " + r + "!");
            }
        }
    }
}
