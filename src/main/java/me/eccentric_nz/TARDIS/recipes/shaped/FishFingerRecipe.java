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
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:-B-,-F-,-B-
easy_ingredients.B:BREAD
easy_ingredients.F:COD
hard_shape:-B-,-F-,-B-
hard_ingredients.B:BREAD
hard_ingredients.F:COD
result:COOKED_COD
amount:3
lore:Best eaten with custard!
*/

public class FishFingerRecipe {

    private final TARDIS plugin;

    public FishFingerRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.COOKED_COD, 3);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Fish Finger"));
        im.lore(List.of(Component.text("Best eaten with custard!")));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "fish_finger");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("B", "F", "B");
        r.setIngredient('B', Material.BREAD);
        r.setIngredient('F', Material.COD);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Fish Finger", r);
    }
}
