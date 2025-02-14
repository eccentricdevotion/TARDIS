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
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:BBB,BOB,BBB
easy_ingredients.B:BEDROCK
easy_ingredients.O:GLOWSTONE_DUST=Sonic Oscillator
hard_shape:BBB,BOB,BBB
hard_ingredients.B:BEDROCK
hard_ingredients.O:GLOWSTONE_DUST=Sonic Oscillator
result:GLOWSTONE_DUST
amount:1
*/

public class ServerAdminCircuitRecipe {

    private final TARDIS plugin;

    public ServerAdminCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Server Admin Circuit");
        im.setItemModel(RecipeItem.SERVER_ADMIN_CIRCUIT.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "server_admin_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName(ChatColor.WHITE + "Sonic Oscillator");
        em.setItemModel(RecipeItem.SONIC_OSCILLATOR.getModel());
        exact.setItemMeta(em);
        r.shape("BBB", "BOB", "BBB");
        r.setIngredient('B', Material.BEDROCK);
        r.setIngredient('O', new RecipeChoice.ExactChoice(exact));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Server Admin Circuit", r);
    }
}
