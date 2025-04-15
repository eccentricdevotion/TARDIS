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

import java.util.List;

/*
easy_shape:RCR,-K-,-T-
easy_ingredients.R:REDSTONE
easy_ingredients.C:COMPARATOR
easy_ingredients.K:GOLD_NUGGET
easy_ingredients.T:REDSTONE_TORCH
hard_shape:RCR,-K-,-T-
hard_ingredients.R:REDSTONE
hard_ingredients.C:COMPARATOR
hard_ingredients.K:GOLD_NUGGET
hard_ingredients.T:GLOWSTONE_DUST=TARDIS Materialisation Circuit
result:GOLD_NUGGET
amount:1
lore:Deadlock & unlock~Hide & rebuild
*/

public class TARDISRemoteKeyRecipe {

    private final TARDIS plugin;

    public TARDISRemoteKeyRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.OMINOUS_TRIAL_KEY, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Remote Key");
//        im.setItemModel(RecipeItem.TARDIS_REMOTE_KEY.getModel());
        im.setLore(List.of("Deadlock & unlock", "Hide & rebuild"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_remote_key");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("RCR", " K ", " T ");
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('C', Material.COMPARATOR);
        r.setIngredient('K', Material.GOLD_NUGGET);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName(ChatColor.WHITE + "TARDIS Materialisation Circuit");
//            em.setItemModel(RecipeItem.TARDIS_MATERIALISATION_CIRCUIT.getModel());
            // set the second line of lore
            List<String> circuit;
            String uses = (plugin.getConfig().getString("circuits.uses.materialisation").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                    ? ChatColor.YELLOW + "unlimited"
                    : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.materialisation");
            circuit = List.of("Uses left", uses);
            em.setLore(circuit);
            exact.setItemMeta(em);
            r.setIngredient('T', new RecipeChoice.ExactChoice(exact));
        } else {
            r.setIngredient('T', Material.REDSTONE_TORCH);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Remote Key", r);
    }
}
