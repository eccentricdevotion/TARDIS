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
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

/*
easy_shape:---,P-P,CPM
easy_ingredients.P:PAPER
easy_ingredients.C:CYAN_STAINED_GLASS_PANE
easy_ingredients.M:MAGENTA_STAINED_GLASS_PANE
hard_shape:R-T,P-P,CPM
hard_ingredients.R:COMPARATOR
hard_ingredients.T:REDSTONE_TORCH
hard_ingredients.P:PAPER
hard_ingredients.C:CYAN_STAINED_GLASS_PANE
hard_ingredients.M:MAGENTA_STAINED_GLASS_PANE
result:LEATHER_HELMET
amount:1
*/

public class ThreeDGlassesRecipe {

    private final TARDIS plugin;

    public ThreeDGlassesRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "3-D Glasses");
//        im.setItemModel(RecipeItem.THREE_D_GLASSES.getModel());
        EquippableComponent equippable = im.getEquippable();
        equippable.setCameraOverlay(Whoniverse.THREE_D_GLASSES_OVERLAY.getKey());
        equippable.setSlot(EquipmentSlot.HEAD);
        equippable.setDispensable(true);
        im.setEquippable(equippable);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "3-d_glasses");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("R T", "P P", "CPM");
            r.setIngredient('R', Material.COMPARATOR);
            r.setIngredient('T', Material.REDSTONE_TORCH);
        } else {
            r.shape("P P", "CPM");
        }
        r.setIngredient('P', Material.PAPER);
        r.setIngredient('C', Material.CYAN_STAINED_GLASS_PANE);
        r.setIngredient('M', Material.MAGENTA_STAINED_GLASS_PANE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("3-D Glasses", r);
    }
}
