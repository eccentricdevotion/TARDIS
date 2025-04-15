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
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:-B-,-R-,-L-
easy_ingredients.B:BUCKET
easy_ingredients.R:REDSTONE
easy_ingredients.L:LIME_STAINED_GLASS
hard_shape:-B-,LRL,-L-
hard_ingredients.B:BUCKET
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.L:LIME_STAINED_GLASS
result:BUCKET
amount:1
lore:Charge Level~0
*/

public class ArtronStorageCellRecipe {

    private final TARDIS plugin;

    public ArtronStorageCellRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BUCKET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Artron Storage Cell");
//        im.setItemModel(Whoniverse.ARTRON_BATTERY.getKey());
        im.setLore(List.of("Charge Level", "0"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "artron_storage_cell");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" B ", "LRL", " L ");
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.shape("B", "R", "L");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('B', Material.BUCKET);
        r.setIngredient('L', Material.LIME_STAINED_GLASS);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Artron Storage Cell", r);
    }
}
