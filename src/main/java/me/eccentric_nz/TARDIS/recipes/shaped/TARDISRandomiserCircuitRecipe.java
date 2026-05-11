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
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

/*
easy_shape:-D-,NCE,-W-
easy_ingredients.D:DIRT
easy_ingredients.N:NETHERRACK
easy_ingredients.C:COMPASS
easy_ingredients.E:END_STONE
easy_ingredients.W:WATER_BUCKET
hard_shape:-D-,NCE,-W-
hard_ingredients.D:DIRT
hard_ingredients.N:NETHERRACK
hard_ingredients.C:COMPASS
hard_ingredients.E:END_STONE
hard_ingredients.W:WATER_BUCKET
result:GLOWSTONE_DUST
amount:1
lore:Uses left~50
*/

public class TARDISRandomiserCircuitRecipe {

    private final TARDIS plugin;

    public TARDISRandomiserCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Randomiser Circuit"));
        is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(CircuitVariant.RANDOM.getFloats())
                .build());
        Component uses = (plugin.getConfig().getString("circuits.uses.randomiser", "50").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? Component.text("unlimited", NamedTextColor.YELLOW)
                : Component.text(plugin.getConfig().getString("circuits.uses.randomiser", "50"), NamedTextColor.YELLOW);
        is.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Uses left"),
                uses
        )));
        NamespacedKey key = new NamespacedKey(plugin, "tardis_randomiser_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" D ", "NCE", " W ");
        r.setIngredient('D', Material.DIRT);
        r.setIngredient('N', Material.NETHERRACK);
        r.setIngredient('C', Material.COMPASS);
        r.setIngredient('E', Material.END_STONE);
        r.setIngredient('W', Material.WATER_BUCKET);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Randomiser Circuit", r);
    }
}
