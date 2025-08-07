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
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:QQQ,Q-Q,QQQ
easy_ingredients.Q:QUARTZ
hard_shape:QQQ,Q-Q,QQQ
hard_ingredients.Q:QUARTZ
result:MUSIC_DISC_STRAD
amount:1
*/

public class BlankStorageDiskRecipe {

    private final TARDIS plugin;

    public BlankStorageDiskRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.MUSIC_DISC_STRAD, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Blank Storage Disk"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "blank_storage_disk");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("QQQ", "Q Q", "QQQ");
        r.setIngredient('Q', Material.QUARTZ);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Blank Storage Disk", r);
    }
}
