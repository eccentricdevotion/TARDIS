/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:III,ISI,IRI
easy_ingredients.I:IRON_INGOT
easy_ingredients.S:SKELETON_SKULL
easy_ingredients.R:REDSTONE
hard_shape:IDI,ISI,IRI
hard_ingredients.I:IRON_INGOT
hard_ingredients.D:DIAMOND
hard_ingredients.S:SKELETON_SKULL
hard_ingredients.R:REDSTONE
result:BIRCH_BUTTON
amount:1
lore:Cyberhead from the~Maldovar Market
*/

public class HandlesRecipe {

    private final TARDIS plugin;

    public HandlesRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.BIRCH_BUTTON, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("Handles"));
        im.lore(List.of(
                Component.text("Cyberhead from the"),
                Component.text("Maldovar Market")
        ));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "handles");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("IDI", "ISI", "IRI");
            r.setIngredient('D', Material.DIAMOND);
        } else {
            r.shape("III", "ISI", "IRI");
        }
        r.setIngredient('I', Material.IRON_INGOT);
        r.setIngredient('S', Material.SKELETON_SKULL);
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Handles", r);
    }
}
