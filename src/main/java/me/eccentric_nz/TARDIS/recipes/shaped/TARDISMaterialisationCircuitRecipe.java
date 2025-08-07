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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

/*
easy_shape:IRI,-L-,QRQ
easy_ingredients.I:IRON_INGOT
easy_ingredients.R:REDSTONE
easy_ingredients.L:BLUE_DYE
easy_ingredients.Q:QUARTZ
hard_shape:IDI,DLD,QRQ
hard_ingredients.I:ENDER_EYE
hard_ingredients.D:REPEATER
hard_ingredients.R:REDSTONE
hard_ingredients.L:BLUE_DYE
hard_ingredients.Q:QUARTZ
result:GLOWSTONE_DUST
amount:1
lore:Uses left~50
*/

public class TARDISMaterialisationCircuitRecipe {

    private final TARDIS plugin;

    public TARDISMaterialisationCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("TARDIS Materialisation Circuit"));
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(CircuitVariant.MATERIALISATION.getFloats());
        im.setCustomModelDataComponent(component);
        Component uses = (plugin.getConfig().getString("circuits.uses.materialisation", "50").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? Component.text("unlimited", NamedTextColor.YELLOW)
                : Component.text(plugin.getConfig().getString("circuits.uses.materialisation", "50"), NamedTextColor.YELLOW);
        im.lore(List.of(Component.text("Uses left"), uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_materialisation_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("IDI", "DLD", "QRQ");
            r.setIngredient('I', Material.ENDER_EYE);
            r.setIngredient('D', Material.REPEATER);
        } else {
            r.shape("IRI", " L ", "QRQ");
            r.setIngredient('I', Material.IRON_INGOT);
        }
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('L', Material.BLUE_DYE);
        r.setIngredient('Q', Material.QUARTZ);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Materialisation Circuit", r);
    }
}
