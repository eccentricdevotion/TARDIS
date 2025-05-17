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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

/*
easy_shape:RQR,RIR,DRL
easy_ingredients.R:REDSTONE
easy_ingredients.Q:QUARTZ
easy_ingredients.I:IRON_INGOT
easy_ingredients.D:REPEATER
easy_ingredients.L:BLUE_DYE
hard_shape:RQR,RIR,DRL
hard_ingredients.R:REDSTONE
hard_ingredients.Q:QUARTZ
hard_ingredients.I:IRON_INGOT
hard_ingredients.D:REPEATER
hard_ingredients.L:BLUE_DYE
result:GLOWSTONE_DUST
amount:1
*/

public class TARDISLocatorCircuitRecipe {

    private final TARDIS plugin;

    public TARDISLocatorCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Locator Circuit");
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(CircuitVariant.LOCATOR.getFloats());
        im.setCustomModelDataComponent(component);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_locator_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("RQR", "RIR", "DRL");
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('Q', Material.QUARTZ);
        r.setIngredient('I', Material.IRON_INGOT);
        r.setIngredient('D', Material.REPEATER);
        r.setIngredient('L', Material.BLUE_DYE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Locator Circuit", r);
    }
}
