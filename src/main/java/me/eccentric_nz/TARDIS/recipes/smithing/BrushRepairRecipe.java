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
package me.eccentric_nz.TARDIS.recipes.smithing;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

/*
base:BLAZE_ROD
addition:GLOWSTONE_DUST=Brush Circuit
result:BLAZE_ROD

*/

public class BrushRepairRecipe {

    private final TARDIS plugin;

    public BrushRepairRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        // result
        ItemStack result = ItemStack.of(Material.BLAZE_ROD, 1);
        // template
        RecipeChoice template = RecipeChoice.itemType(ItemType.REDSTONE);
        // base material to upgrade
        RecipeChoice base = RecipeChoice.itemType(ItemType.BLAZE_ROD);
        // addition
        ItemStack is = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("Brush Circuit"));
        is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(CircuitVariant.BRUSH.getFloats())
                .build());
        RecipeChoice addition = new RecipeChoice.ExactChoice(is);
        NamespacedKey key = new NamespacedKey(plugin, "brush_repair");
        SmithingRecipe r = new SmithingTransformRecipe(key, result, template, base, addition);
        plugin.getServer().addRecipe(r);
    }
}
