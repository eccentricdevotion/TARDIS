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
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:-R-,-S-,-R-
easy_ingredients.R:REDSTONE
easy_ingredients.S:OAK_SIGN
hard_shape:-R-,-S-,-R-
hard_ingredients.R:REDSTONE
hard_ingredients.S:OAK_SIGN
result:GLOWSTONE_DUST
amount:1
lore:Uses left~50
*/

public class TARDISInputCircuitRecipe {

    private final TARDIS plugin;

    public TARDISInputCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Input Circuit");
        im.setItemModel(RecipeItem.TARDIS_INPUT_CIRCUIT.getModel());
        String uses = (plugin.getConfig().getString("circuits.uses.input").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? ChatColor.YELLOW + "unlimited"
                : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.input");
        im.setLore(List.of("Uses left", uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_input_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("R", "S", "R");
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('S', Material.OAK_SIGN);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Input Circuit", r);
    }
}
