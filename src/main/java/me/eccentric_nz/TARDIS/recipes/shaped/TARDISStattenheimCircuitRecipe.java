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
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

/*
easy_shape:---,LRM,QQQ
easy_ingredients.L:GLOWSTONE_DUST=TARDIS Locator Circuit
easy_ingredients.R:REDSTONE
easy_ingredients.M:GLOWSTONE_DUST=TARDIS Materialisation Circuit
easy_ingredients.Q:QUARTZ
hard_shape:---,LRM,QQQ
hard_ingredients.L:GLOWSTONE_DUST=TARDIS Locator Circuit
hard_ingredients.R:REDSTONE
hard_ingredients.M:GLOWSTONE_DUST=TARDIS Materialisation Circuit
hard_ingredients.Q:QUARTZ
result:GLOWSTONE_DUST
amount:1
*/

public class TARDISStattenheimCircuitRecipe {

    private final TARDIS plugin;

    public TARDISStattenheimCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Stattenheim Circuit"));
        is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(CircuitVariant.STATTENHEIM.getFloats())
                .build());
        NamespacedKey key = new NamespacedKey(plugin, "tardis_stattenheim_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        exact.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Materialisation Circuit"));
        exact.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(CircuitVariant.MATERIALISATION.getFloats())
                .build());
        // set the second line of lore
        Component uses = (plugin.getConfig().getString("circuits.uses.materialisation", "50").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? Component.text("unlimited", NamedTextColor.YELLOW)
                : Component.text(plugin.getConfig().getString("circuits.uses.materialisation", "50"), NamedTextColor.YELLOW);
        exact.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Uses left"),
                uses
        )));
        ItemStack locator = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        locator.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Locator Circuit"));
        locator.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(CircuitVariant.LOCATOR.getFloats())
                .build());
        r.shape("LRM", "QQQ");
        r.setIngredient('L', new RecipeChoice.ExactChoice(locator));
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('M', new RecipeChoice.ExactChoice(exact));
        r.setIngredient('Q', Material.QUARTZ);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Stattenheim Circuit", r);
    }
}
