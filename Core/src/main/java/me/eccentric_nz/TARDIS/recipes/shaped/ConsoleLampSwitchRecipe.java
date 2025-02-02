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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape: ---,WLA,CCC
easy_ingredients.W: WARPED_BUTTON
easy_ingredients.L: LEVER
easy_ingredients.A: ACACIA_BUTTON
easy_ingredients.C: COPPER_INGOT
hard_shape: ---,WLA,CCC
hard_ingredients.W: WARPED_BUTTON
hard_ingredients.L: LEVER
hard_ingredients.A: ACACIA_BUTTON
hard_ingredients.C: COPPER_BLOCK
result: LEVER
amount: 1
 */

public class ConsoleLampSwitchRecipe {

    private final TARDIS plugin;

    public ConsoleLampSwitchRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        // add interior recipe
        ItemStack is = new ItemStack(Material.LEVER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Console Lamp Switch");
        im.setItemModel(RecipeItem.CONSOLE_LAMP_SWITCH.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "console_lamp_switch");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("RTB", "CCC");
        r.setIngredient('R', Material.CRIMSON_BUTTON);
        r.setIngredient('T', Material.TRIPWIRE_HOOK);
        r.setIngredient('B', Material.BAMBOO_BUTTON);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('C', Material.COPPER_BLOCK);
        } else {
            r.setIngredient('C', Material.COPPER_INGOT);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Console Lamp Switch", r);
    }
}
