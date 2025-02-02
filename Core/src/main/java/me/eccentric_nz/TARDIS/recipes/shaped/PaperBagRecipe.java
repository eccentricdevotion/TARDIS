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

import java.util.List;

/*
easy_shape:---,PLP,-P-
easy_ingredients.P:PAPER
easy_ingredients.L:LAPIS_BLOCK
hard_shape:-LC,PSP,-P-
hard_ingredients.L:LAPIS_BLOCK
hard_ingredients.C:COMPARATOR
hard_ingredients.P:PAPER
hard_ingredients.S:SHULKER_SHELL
result:PAPER
amount:1
lore:Smaller on the outside
*/

public class PaperBagRecipe {

    private final TARDIS plugin;

    public PaperBagRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Paper Bag");
        im.setItemModel(RecipeItem.PAPER_BAG.getModel());
        im.setLore(List.of("Smaller on the outside"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "paper_bag");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" LC", "PSP", " P ");
            r.setIngredient('C', Material.COMPARATOR);
            r.setIngredient('S', Material.SHULKER_SHELL);
        } else {
            r.shape("PLP", " P ");
        }
        r.setIngredient('P', Material.PAPER);
        r.setIngredient('L', Material.LAPIS_BLOCK);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Paper Bag", r);
    }
}
