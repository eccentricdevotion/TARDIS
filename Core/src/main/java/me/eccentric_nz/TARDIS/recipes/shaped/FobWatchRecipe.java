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
easy_shape:-C-,-W-,R-R
easy_ingredients.C:GLOWSTONE_DUST=TARDIS Chameleon Circuit
easy_ingredients.W:CLOCK
easy_ingredients.R:REDSTONE
hard_shape:-C-,IWI,R-R
hard_ingredients.C:GLOWSTONE_DUST=TARDIS Chameleon Circuit
hard_ingredients.I:IRON_INGOT
hard_ingredients.W:CLOCK
hard_ingredients.R:REDSTONE
result:CLOCK
amount:1
*/

public class FobWatchRecipe {

    private final TARDIS plugin;

    public FobWatchRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.CLOCK, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Fob Watch");
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "fob_watch");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName(ChatColor.WHITE + "TARDIS Chameleon Circuit");
        // set the second line of lore
        List<String> circuit;
        String uses = (plugin.getConfig().getString("circuits.uses.chameleon").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? ChatColor.YELLOW + "unlimited"
                : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.chameleon");
        circuit = List.of("Uses left", uses);
        em.setLore(circuit);
        exact.setItemMeta(em);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" C ", "IWI", "R R");
            r.setIngredient('I', Material.IRON_INGOT);
        } else {
            r.shape(" C ", " W ", "R R");
        }
        r.setIngredient('C', new RecipeChoice.ExactChoice(exact));
        r.setIngredient('W', Material.CLOCK);
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Fob Watch", r);
    }
}
