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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

/*
easy_shape:OIO,ICI,OIO
easy_ingredients.O:GRAVEL
easy_ingredients.I:IRON_INGOT
easy_ingredients.C:RED_WOOL
hard_shape:OIO,ICI,OIO
hard_ingredients.O:OBSIDIAN
hard_ingredients.I:IRON_INGOT
hard_ingredients.C:GLOWSTONE_DUST=TARDIS Locator Circuit
result:COMPASS
amount:1
*/

public class TARDISLocatorRecipe {

    private final TARDIS plugin;

    public TARDISLocatorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Locator");
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_locator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("OIO", "ICI", "OIO");
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('O', Material.OBSIDIAN);
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName(ChatColor.WHITE + "TARDIS Locator Circuit");
            CustomModelDataComponent component = im.getCustomModelDataComponent();
            component.setFloats(CircuitVariant.LOCATOR.getFloats());
            im.setCustomModelDataComponent(component);
            exact.setItemMeta(em);
            r.setIngredient('C', new RecipeChoice.ExactChoice(exact));
        } else {
            r.setIngredient('O', Material.GRAVEL);
            r.setIngredient('C', Material.RED_WOOL);
        }
        r.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Locator", r);
    }
}
