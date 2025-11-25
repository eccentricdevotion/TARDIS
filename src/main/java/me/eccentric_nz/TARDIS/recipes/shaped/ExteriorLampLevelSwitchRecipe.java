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
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape: ---,LBM,CCC
easy_ingredients.L: LEVER
easy_ingredients.B: BAMBOO_BUTTON
easy_ingredients.M: MANGROVE_BUTTON
easy_ingredients.C: COPPER_INGOT
hard_shape: ---,LBM,CCC
hard_ingredients.L: LEVER
hard_ingredients.B: BAMBOO_BUTTON
hard_ingredients.M: MANGROVE_BUTTON
hard_ingredients.C: COPPER_BLOCK
result: LEVER
amount: 1
 */
public class ExteriorLampLevelSwitchRecipe {

    private final TARDIS plugin;

    public ExteriorLampLevelSwitchRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        // add exterior recipe
        ItemStack is = ItemStack.of(Material.LEVER, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Exterior Lamp Level Switch"));
        im.setItemModel(RecipeItem.EXTERIOR_LAMP_LEVEL_SWITCH.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "exterior_lamp_level_switch");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("LBM", "CCC");
        r.setIngredient('L', Material.LEVER);
        r.setIngredient('B', Material.BAMBOO_BUTTON);
        r.setIngredient('M', Material.MANGROVE_BUTTON);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('C', Material.COPPER_BLOCK);
        } else {
            r.setIngredient('C', Material.COPPER_INGOT);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Exterior Lamp Level Switch", r);
    }
}
