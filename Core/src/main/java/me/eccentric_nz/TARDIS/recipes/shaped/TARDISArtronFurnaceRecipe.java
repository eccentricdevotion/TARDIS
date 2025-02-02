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
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:---,OFO,RRR
easy_ingredients.O:OBSIDIAN
easy_ingredients.F:FURNACE
easy_ingredients.R:REDSTONE
hard_shape:---,OFO,RRR
hard_ingredients.O:OBSIDIAN
hard_ingredients.F:FURNACE
hard_ingredients.R:REDSTONE
result:FURNACE
amount:1
*/

public class TARDISArtronFurnaceRecipe {

    private final TARDIS plugin;

    public TARDISArtronFurnaceRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.FURNACE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Artron Furnace");
        im.setItemModel(RecipeItem.TARDIS_ARTRON_FURNACE.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_artron_furnace");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("OFO", "RRR");
        r.setIngredient('O', Material.OBSIDIAN);
        r.setIngredient('F', Material.FURNACE);
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Artron Furnace", r);
    }
}
