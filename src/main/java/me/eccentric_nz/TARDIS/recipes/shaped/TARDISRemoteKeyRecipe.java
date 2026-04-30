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
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
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
easy_shape:RCR,-K-,-T-
easy_ingredients.R:REDSTONE
easy_ingredients.C:COMPARATOR
easy_ingredients.K:GOLD_NUGGET
easy_ingredients.T:REDSTONE_TORCH
hard_shape:RCR,-K-,-T-
hard_ingredients.R:REDSTONE
hard_ingredients.C:COMPARATOR
hard_ingredients.K:GOLD_NUGGET
hard_ingredients.T:GLOWSTONE_DUST=TARDIS Materialisation Circuit
result:GOLD_NUGGET
amount:1
lore:Deadlock & unlock~Hide & rebuild
*/

public class TARDISRemoteKeyRecipe {

    private final TARDIS plugin;

    public TARDISRemoteKeyRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.OMINOUS_TRIAL_KEY, 1);
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Remote Key"));
        is.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Deadlock & unlock"),
                Component.text("Hide & rebuild")
        )));
        NamespacedKey key = new NamespacedKey(plugin, "tardis_remote_key");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("RCR", " K ", " T ");
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('C', Material.COMPARATOR);
        r.setIngredient('K', Material.GOLD_NUGGET);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
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
            r.setIngredient('T', new RecipeChoice.ExactChoice(exact));
        } else {
            r.setIngredient('T', Material.REDSTONE_TORCH);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Remote Key", r);
    }
}
