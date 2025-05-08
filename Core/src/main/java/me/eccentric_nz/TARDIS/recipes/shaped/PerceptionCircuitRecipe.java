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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

/*
easy_shape:IGI,CEC,DTD
easy_ingredients.I:IRON_INGOT
easy_ingredients.G:GOLDEN_CARROT
easy_ingredients.C:COMPARATOR
easy_ingredients.E:FERMENTED_SPIDER_EYE
easy_ingredients.D:REPEATER
easy_ingredients.T:REDSTONE_TORCH
hard_shape:IGI,CEC,DTD
hard_ingredients.I:IRON_INGOT
hard_ingredients.G:GOLDEN_CARROT
hard_ingredients.C:COMPARATOR
hard_ingredients.E:FERMENTED_SPIDER_EYE
hard_ingredients.D:REPEATER
hard_ingredients.T:REDSTONE_TORCH
result:GLOWSTONE_DUST
amount:1
*/

public class PerceptionCircuitRecipe {

    private final TARDIS plugin;

    public PerceptionCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Perception Circuit");
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(CircuitVariant.PERCEPTION.getFloats());
        im.setCustomModelDataComponent(component);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "perception_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("IGI", "CEC", "DTD");
        r.setIngredient('I', Material.IRON_INGOT);
        r.setIngredient('G', Material.GOLDEN_CARROT);
        r.setIngredient('C', Material.COMPARATOR);
        r.setIngredient('E', Material.FERMENTED_SPIDER_EYE);
        r.setIngredient('D', Material.REPEATER);
        r.setIngredient('T', Material.REDSTONE_TORCH);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Perception Circuit", r);
    }
}
