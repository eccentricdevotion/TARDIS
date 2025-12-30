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
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-A-,ARA,-A-
easy_ingredients.A:WATER_BUCKET=Acid Bucket
easy_ingredients.R:REDSTONE
hard_shape:-A-,ARA,-A-
hard_ingredients.A:WATER_BUCKET=Acid Bucket
hard_ingredients.R:REDSTONE_BLOCK
result:NETHER_BRICK
amount:1
*/

public class AcidBatteryRecipe {

    private final TARDIS plugin;

    public AcidBatteryRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.NETHER_BRICK, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Acid Battery"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "acid_battery");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = ItemStack.of(Material.WATER_BUCKET, 1);
        ItemMeta em = exact.getItemMeta();
        em.displayName(ComponentUtils.toWhite("Acid Bucket"));
        exact.setItemMeta(em);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" A ", "ARA", " A ");
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.shape(" A ", "ARA", " A ");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('A', new RecipeChoice.ExactChoice(exact));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Acid Battery", r);
    }
}
