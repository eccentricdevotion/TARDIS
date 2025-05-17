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
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

/*
easy_shape:-N-,NFN,-N-
easy_ingredients.N:NETHERRACK
easy_ingredients.F:FLINT_AND_STEEL
hard_shape:LN-,NFN,-NL
hard_ingredients.N:NETHERRACK
hard_ingredients.F:FLINT_AND_STEEL
hard_ingredients.L:LAVA_BUCKET
result:GLOWSTONE_DUST
amount:1
*/

public class IgniteCircuitRecipe {

    private final TARDIS plugin;

    public IgniteCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Ignite Circuit");
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(CircuitVariant.IGNITE.getFloats());
        im.setCustomModelDataComponent(component);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "ignite_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("LN ", "NFN", " NL");
            r.setIngredient('L', Material.LAVA_BUCKET);
        } else {
            r.shape(" N ", "NFN", " N ");
        }
        r.setIngredient('N', Material.NETHERRACK);
        r.setIngredient('F', Material.FLINT_AND_STEEL);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Ignite Circuit", r);
    }
}
