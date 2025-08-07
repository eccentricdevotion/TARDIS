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
package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;

/*
easy_shape:-A-,RER,GGG
easy_ingredients.A:AMETHYST_SHARD
easy_ingredients.R:REDSTONE
easy_ingredients.E:equipment.material
easy_ingredients.G:GLASS_PANE
hard_shape:-A-,RER,GGG
hard_ingredients.A:SPYGLASS
hard_ingredients.R:REDSTONE
hard_ingredients.E:equipment.material
hard_ingredients.G:GLASS
result:equipment.material
amount:1
*/

public class MicroscopeRecipes {

    private final TARDIS plugin;

    public MicroscopeRecipes(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipes() {
        for (LabEquipment equipment : LabEquipment.values()) {
            String name = equipment.getName();
            ItemStack is = ItemStack.of(equipment.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(ComponentUtils.toWhite(name));
            im.setItemModel(equipment.getModel());
            is.setItemMeta(im);
            NamespacedKey key = new NamespacedKey(plugin, equipment.toString().toLowerCase(Locale.ROOT));
            ShapedRecipe r = new ShapedRecipe(key, is);
            r.shape(" A ", "RER", "GGG");
            if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
                r.setIngredient('A', Material.SPYGLASS);
                r.setIngredient('G', Material.GLASS);
            } else {
                r.setIngredient('A', Material.AMETHYST_SHARD);
                r.setIngredient('G', Material.GLASS_PANE);
            }
            r.setIngredient('R', Material.REDSTONE);
            r.setIngredient('E', equipment.getMaterial());
            plugin.getServer().addRecipe(r);
            plugin.getFigura().getShapedRecipes().put(name, r);
        }
    }
}
