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
easy_shape:RDR,LWL,RCR
easy_ingredients.R:REDSTONE
easy_ingredients.D:DISPENSER
easy_ingredients.L:LEAD
easy_ingredients.W:WATER_BUCKET
easy_ingredients.C:COMPARATOR
hard_shape:RDR,PWP,RCR
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.D:DISPENSER
hard_ingredients.W:WATER_BUCKET
hard_ingredients.P:PHANTOM_MEMBRANE
hard_ingredients.C:COMPARATOR
result:GLOWSTONE_DUST
amount:1
*/

public class CharmCircuitRecipe {

    private final TARDIS plugin;

    public CharmCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Charm Circuit"));
        is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(CircuitVariant.CHARM.getFloats())
                .addString("Charm Circuit")
                .build());
        NamespacedKey key = new NamespacedKey(plugin, "charm_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("RDR", "LWL", "RCR");
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('L', Material.LEAD);
        r.setIngredient('D', Material.DANDELION);
        r.setIngredient('W', Material.WHEAT);
        r.setIngredient('C', Material.CORNFLOWER);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Charm Circuit", r);
    }
}
