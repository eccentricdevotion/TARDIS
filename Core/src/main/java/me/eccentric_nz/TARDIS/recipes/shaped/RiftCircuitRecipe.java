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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-D-,DND,-D-
easy_ingredients.D:DIAMOND
easy_ingredients.N:NETHER_STAR
hard_shape:-D-,DND,-D-
hard_ingredients.D:DIAMOND
hard_ingredients.N:NETHER_STAR
result:GLOWSTONE_DUST
amount:1
*/

public class RiftCircuitRecipe {

    private final TARDIS plugin;

    public RiftCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Rift Circuit");
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "rift_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" D ", "DND", " D ");
        r.setIngredient('D', Material.DIAMOND);
        r.setIngredient('N', Material.NETHER_STAR);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Rift Circuit", r);
    }
}
