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
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-A-,ACA,RAR
easy_ingredients.A:NETHER_BRICK=Acid Battery
easy_ingredients.C:GLOWSTONE_DUST=Rift Circuit
easy_ingredients.R:REDSTONE
hard_shape:-A-,ACA,NAN
hard_ingredients.A:NETHER_BRICK=Acid Battery
hard_ingredients.C:GLOWSTONE_DUST=Rift Circuit
hard_ingredients.N:NETHER_STAR
result:BEACON
amount:1
*/

public class RiftManipulatorRecipe {

    private final TARDIS plugin;

    public RiftManipulatorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BEACON, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Rift Manipulator");
        im.setItemModel(RecipeItem.RIFT_MANIPULATOR.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "rift_manipulator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack rift = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = rift.getItemMeta();
        em.setDisplayName(ChatColor.WHITE + "Rift Circuit");
        em.setItemModel(RecipeItem.RIFT_CIRCUIT.getModel());
        rift.setItemMeta(em);
        ItemStack acid = new ItemStack(Material.NETHER_BRICK, 1);
        ItemMeta aim = acid.getItemMeta();
        aim.setDisplayName(ChatColor.WHITE + "Acid Battery");
        aim.setItemModel(RecipeItem.ACID_BATTERY.getModel());
        acid.setItemMeta(aim);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" A ", "ACA", "NAN");
            r.setIngredient('N', Material.NETHER_STAR);
        } else {
            r.shape(" A ", "ACA", "RAR");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('A', new RecipeChoice.ExactChoice(acid));
        r.setIngredient('C', new RecipeChoice.ExactChoice(rift));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Rift Manipulator", r);
    }
}
