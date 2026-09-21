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
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

/*
easy_shape:GRG,RGR,GRG
easy_ingredients.G:GLASS_PANE
easy_ingredients.R:REDSTONE
hard_shape:GRG,RER,GRG
hard_ingredients.G:GLASS
hard_ingredients.E:SPIDER_EYE
hard_ingredients.R:REDSTONE
result:GLOWSTONE_DUST
amount:1
lore:Uses left~20
*/

public class TARDISScannerCircuitRecipe {

    private final TARDIS plugin;

    public TARDISScannerCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite("TARDIS Scanner Circuit"));
        is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(CircuitVariant.SCANNER.getFloats())
                .build());
        Component uses = (plugin.getConfig().getString("circuits.uses.scanner", "20").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? Component.text("unlimited", NamedTextColor.YELLOW)
                : Component.text(plugin.getConfig().getString("circuits.uses.scanner", "20"), NamedTextColor.YELLOW);
        is.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Uses left"),
                uses
        )));
        NamespacedKey key = new NamespacedKey(plugin, "tardis_scanner_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("GRG", "RER", "GRG");
            r.setIngredient('G', Material.GLASS);
            r.setIngredient('E', Material.SPIDER_EYE);
        } else {
            r.shape("GRG", "RGR", "GRG");
            r.setIngredient('G', Material.GLASS_PANE);
        }
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Scanner Circuit", r);
    }
}
