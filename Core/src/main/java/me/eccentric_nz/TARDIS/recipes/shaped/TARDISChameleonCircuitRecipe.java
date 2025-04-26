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
easy_shape:-G-,RER,-G-
easy_ingredients.G:GOLD_INGOT
easy_ingredients.E:DETECTOR_RAIL
easy_ingredients.R:REDSTONE
hard_shape:DGD,GEG,RMR
hard_ingredients.D:REPEATER
hard_ingredients.G:GOLD_INGOT
hard_ingredients.E:DETECTOR_RAIL
hard_ingredients.R:REDSTONE
hard_ingredients.M:GLOWSTONE_DUST=TARDIS Materialisation Circuit
result:GLOWSTONE_DUST
amount:1
lore:Uses left~25
*/

public class TARDISChameleonCircuitRecipe {

    private final TARDIS plugin;

    public TARDISChameleonCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Chameleon Circuit");
        String uses = (plugin.getConfig().getString("circuits.uses.chameleon").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? ChatColor.YELLOW + "unlimited"
                : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.chameleon");
        im.setLore(List.of("Uses left", uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_chameleon_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("DGD", "GEG", "RMR");
            r.setIngredient('D', Material.REPEATER);
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName(ChatColor.WHITE + "TARDIS Materialisation Circuit");
            // set the second line of lore
            List<String> circuit;
            String mat_uses = (plugin.getConfig().getString("circuits.uses.materialisation").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                    ? ChatColor.YELLOW + "unlimited"
                    : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.materialisation");
            circuit = List.of("Uses left", mat_uses);
            em.setLore(circuit);
            exact.setItemMeta(em);
            r.setIngredient('M', new RecipeChoice.ExactChoice(exact));
        } else {
            r.shape(" G ", "RER", " G ");
        }
        r.setIngredient('G', Material.GOLD_INGOT);
        r.setIngredient('E', Material.DETECTOR_RAIL);
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Chameleon Circuit", r);
    }
}
