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
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

/*
easy_shape:S-S,-S-,RGC
easy_ingredients.S:STRING
easy_ingredients.R:REDSTONE
easy_ingredients.G:GOLD_NUGGET
easy_ingredients.C:COMPARATOR
hard_shape:S-S,-S-,RGC
hard_ingredients.S:STRING
hard_ingredients.R:REDSTONE
hard_ingredients.G:GOLD_NUGGET
hard_ingredients.C:GLOWSTONE_DUST=Perception Circuit
result:GOLD_NUGGET
amount:1
*/

public class PerceptionFilterRecipe {

    private final TARDIS plugin;

    public PerceptionFilterRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.OMINOUS_TRIAL_KEY, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Perception Filter"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "perception_filter");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("S S", " S ", "RGC");
        r.setIngredient('S', Material.STRING);
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('G', Material.GOLD_NUGGET);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            ItemStack exact = ItemStack.of(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.displayName(ComponentUtils.toWhite("Perception Circuit"));
            CustomModelDataComponent component = em.getCustomModelDataComponent();
            component.setFloats(CircuitVariant.PERCEPTION.getFloats());
            em.setCustomModelDataComponent(component);
            exact.setItemMeta(em);
            r.setIngredient('C', new RecipeChoice.ExactChoice(exact));
        } else {
            r.setIngredient('C', Material.COMPARATOR);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Perception Filter", r);
    }
}
