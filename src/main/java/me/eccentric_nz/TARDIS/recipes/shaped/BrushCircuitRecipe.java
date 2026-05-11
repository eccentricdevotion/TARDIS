/*
 * Copyright (C) 2026 eccentric_nz
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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/*
easy_shape:-K-,RSR,-R-
easy_ingredients.K:FEATHER
easy_ingredients.R:REDSTONE
easy_ingredients.S:COPPER_INGOT
hard_shape:-K-,RSR,-R-
hard_ingredients.K:BRUSH
hard_ingredients.R:REDSTONE
hard_ingredients.S:COPPER_BLOCK
result:GLOWSTONE_DUST
amount:1
*/

public class BrushCircuitRecipe {

    private final TARDIS plugin;

    public BrushCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Brush Circuit"));
        is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(CircuitVariant.BRUSH.getFloats())
                .build());
        NamespacedKey key = new NamespacedKey(plugin, "brush_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" K ", "RSR", " R ");
            r.setIngredient('K', Material.BRUSH);
            r.setIngredient('S', Material.COPPER_BLOCK);
        } else {
            r.shape(" K ", "RSR", " R ");
            r.setIngredient('K', Material.FEATHER);
            r.setIngredient('S', Material.COPPER_INGOT);
        }
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Brush Circuit", r);
    }
}
