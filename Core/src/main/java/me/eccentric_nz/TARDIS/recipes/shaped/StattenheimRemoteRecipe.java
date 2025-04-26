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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:OBO,-L-,RRR
easy_ingredients.O:OBSIDIAN
easy_ingredients.B:STONE_BUTTON
easy_ingredients.L:BLUE_DYE
easy_ingredients.R:REDSTONE
hard_shape:OBO,OLO,RRR
hard_ingredients.O:OBSIDIAN
hard_ingredients.B:STONE_BUTTON
hard_ingredients.L:GLOWSTONE_DUST=TARDIS Stattenheim Circuit
hard_ingredients.R:REDSTONE
result:FLINT
amount:1
lore:Right-click block~to call TARDIS
*/

public class StattenheimRemoteRecipe {

    private final TARDIS plugin;

    public StattenheimRemoteRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.FLINT, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Stattenheim Remote");
//        im.setItemModel(RecipeItem.STATTENHEIM_REMOTE.getModel());
        String uses = plugin.getConfig().getString("circuits.uses.stattenheim", "15");
        if (uses.equals("0")) {
            uses = "1000";
        }
        im.setLore(List.of("Right-click block", "to call TARDIS", "Uses left", uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "stattenheim_remote");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("OBO", "OLO", "RRR");
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName(ChatColor.WHITE + "TARDIS Stattenheim Circuit");
//            em.setItemModel(RecipeItem.TARDIS_STATTENHEIM_CIRCUIT.getModel());
            exact.setItemMeta(em);
            r.setIngredient('L', new RecipeChoice.ExactChoice(exact));
        } else {
            r.shape("OBO", " L ", "RRR");
            r.setIngredient('L', Material.BLUE_DYE);
        }
        r.setIngredient('O', Material.OBSIDIAN);
        r.setIngredient('B', Material.STONE_BUTTON);
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Stattenheim Remote", r);
    }
}
