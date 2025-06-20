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
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

/*
easy_shape:RGR,GSG,BGB
easy_ingredients.R:REDSTONE
easy_ingredients.G:GOLD_NUGGET
easy_ingredients.S:REPEATER
easy_ingredients.B:BLACKSTONE
hard_shape:RGR,GSG,BGB
hard_ingredients.R:REDSTONE
hard_ingredients.G:GOLD_INGOT
hard_ingredients.S:GLOWSTONE_DUST=Sonic Oscillator
hard_ingredients.B:BLACKSTONE
result:FLOWER_POT
amount:1
*/

public class SonicDockRecipe {

    private final TARDIS plugin;

    public SonicDockRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.FLOWER_POT, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Sonic Dock");
        im.setItemModel(RecipeItem.SONIC_DOCK.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "sonic_dock");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("RGR", "GSG", "BGB");
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('B', Material.BLACKSTONE);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('G', Material.GOLD_INGOT);
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName(ChatColor.WHITE + "Sonic Oscillator");
            CustomModelDataComponent component = em.getCustomModelDataComponent();
            component.setFloats(CircuitVariant.SONIC.getFloats());
            em.setCustomModelDataComponent(component);
            exact.setItemMeta(em);
            r.setIngredient('S', new RecipeChoice.ExactChoice(exact));
        } else {
            r.shape("RGR", "GSG", "BGB");
            r.setIngredient('G', Material.GOLD_NUGGET);
            r.setIngredient('S', Material.REPEATER);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Sonic Dock", r);
    }
}
