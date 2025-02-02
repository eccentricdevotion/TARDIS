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
easy_shape:MRM,RGR,MRM
easy_ingredients.M:MAP
easy_ingredients.G:GLASS_PANE
easy_ingredients.R:REDSTONE
hard_shape:MRM,RGR,MRM
hard_ingredients.M:MAP
hard_ingredients.G:TINTED_GLASS
hard_ingredients.R:REDSTONE_BLOCK
result:MAP
amount:1
*/

public class TARDISMonitorRecipe {

    private final TARDIS plugin;

    public TARDISMonitorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MAP, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Monitor");
        im.setItemModel(RecipeItem.TARDIS_MONITOR.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_monitor");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("MRM", "RGR", "MRM");
        r.setIngredient('M', Material.MAP);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('G', Material.TINTED_GLASS);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.setIngredient('G', Material.GLASS_PANE);
            r.setIngredient('R', Material.REDSTONE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Monitor", r);
    }
}
