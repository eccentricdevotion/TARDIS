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
package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

/*
easy_shape:GPG,-G-,-G-
easy_ingredients.G:GOLD_NUGGET
easy_ingredients.P:POTION>AWKWARD
hard_shape:GPG,-G-,GGG
hard_ingredients.G:GOLD_NUGGET
hard_ingredients.P:POTION>AWKWARD
result:GOLD_INGOT
amount:1
*/

public class ElixirOfLifeRecipe {

    private final TARDIS plugin;

    public ElixirOfLifeRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ElixirOfLife.create();
        NamespacedKey key = new NamespacedKey(plugin, "elixir_of_life");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("GPG", " G ", "GGG");
        } else {
            r.shape("GPG", " G ", " G ");
        }
        ItemStack potion = ItemStack.of(Material.POTION, 1);
        PotionMeta pm = (PotionMeta) potion.getItemMeta();
        pm.setBasePotionType(PotionType.AWKWARD);
        potion.setItemMeta(pm);
        r.setIngredient('G', Material.GOLD_NUGGET);
        r.setIngredient('P', new RecipeChoice.ExactChoice(potion));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Elixir of Life", r);
    }
}
