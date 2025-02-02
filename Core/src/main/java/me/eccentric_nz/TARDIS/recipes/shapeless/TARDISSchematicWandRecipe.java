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
package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
recipe:BONE,REDSTONE
result:BONE
amount:1
lore:Right-click start~Left-click end
*/

public class TARDISSchematicWandRecipe {

    private final TARDIS plugin;

    public TARDISSchematicWandRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BONE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Schematic Wand");
        im.setItemModel(RecipeItem.TARDIS_SCHEMATIC_WAND.getModel());
        im.setLore(List.of("Right-click start", "Left-click end"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_schematic_wand");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.BONE);
        r.addIngredient(Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("TARDIS Schematic Wand", r);
    }
}
