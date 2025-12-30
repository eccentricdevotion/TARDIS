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
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:---,WYW,---
easy_ingredients.W:WHEAT
easy_ingredients.Y:YELLOW_DYE
hard_shape:---,WYW,---
hard_ingredients.W:WHEAT
hard_ingredients.Y:YELLOW_DYE
result:COOKIE
amount:8
*/

public class CustardCreamRecipe {

    private final TARDIS plugin;

    public CustardCreamRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.COOKIE, 8);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Custard Cream"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "custard_cream");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("WYW");
        r.setIngredient('W', Material.WHEAT);
        r.setIngredient('Y', Material.YELLOW_DYE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Custard Cream", r);
    }
}
