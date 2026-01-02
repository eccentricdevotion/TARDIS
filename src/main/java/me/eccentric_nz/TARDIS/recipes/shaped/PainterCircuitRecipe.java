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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

/*
easy_shape:-I-,DGD,-I-
easy_ingredients.I:BLACK_DYE
easy_ingredients.D:PURPLE_DYE
easy_ingredients.G:GOLD_NUGGET
hard_shape:-I-,DGD,-I-
hard_ingredients.I:BLACK_DYE
hard_ingredients.D:PURPLE_DYE
hard_ingredients.G:GOLD_BLOCK
result:GLOWSTONE_DUST
amount:1
*/

public class PainterCircuitRecipe {

    private final TARDIS plugin;

    public PainterCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Painter Circuit"));
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(CircuitVariant.PAINTER.getFloats());
        im.setCustomModelDataComponent(component);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "painter_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" I ", "DGD", " I ");
        r.setIngredient('I', Material.BLACK_DYE);
        r.setIngredient('D', Material.PURPLE_DYE);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('G', Material.GOLD_BLOCK);
        } else {
            r.setIngredient('G', Material.GOLD_NUGGET);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Painter Circuit", r);
    }
}
