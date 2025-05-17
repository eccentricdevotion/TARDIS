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
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

/*
easy_shape:---,-C-,SDT
easy_ingredients.C:GLOWSTONE_DUST=Emerald Environment Circuit
easy_ingredients.S:SAND
easy_ingredients.D:DIRT
easy_ingredients.T:STONE
hard_shape:-C-,SDT,LWN
hard_ingredients.C:GLOWSTONE_DUST=Emerald Environment Circuit
hard_ingredients.S:SAND
hard_ingredients.D:DIRT
hard_ingredients.T:STONE
hard_ingredients.L:CLAY
hard_ingredients.W:SNOW_BLOCK
hard_ingredients.N:NETHERRACK
result:BRICK
amount:1
*/

public class TARDISBiomeReaderRecipe {

    private final TARDIS plugin;

    public TARDISBiomeReaderRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BRICK, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Biome Reader");
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_biome_reader");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName(ChatColor.WHITE + "Emerald Environment Circuit");
        CustomModelDataComponent ecomponent = em.getCustomModelDataComponent();
        ecomponent.setFloats(CircuitVariant.EMERALD.getFloats());
        em.setCustomModelDataComponent(ecomponent);
        exact.setItemMeta(em);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" C ", "SDT", "LWN");
            r.setIngredient('C', new RecipeChoice.ExactChoice(exact));
            r.setIngredient('L', Material.CLAY);
            r.setIngredient('W', Material.SNOW_BLOCK);
            r.setIngredient('N', Material.NETHERRACK);
        } else {
            r.shape(" C ", "SDT");
            r.setIngredient('C', new RecipeChoice.ExactChoice(exact));
        }
        r.setIngredient('S', Material.SAND);
        r.setIngredient('D', Material.DIRT);
        r.setIngredient('T', Material.STONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Biome Reader", r);
    }
}
