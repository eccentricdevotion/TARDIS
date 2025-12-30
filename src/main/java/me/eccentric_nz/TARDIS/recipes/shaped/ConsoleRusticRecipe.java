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
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/*
easy_shape:CBC,LWL,CBC
easy_ingredients.C:CONCRETE_POWDER
easy_ingredients.B:BAMBOO_BUTTON
easy_ingredients.L:LEVER
easy_ingredients.W:REDSTONE
hard_shape:CBC,ORO,CBC
hard_ingredients.C:CONCRETE_POWDER
hard_ingredients.B:BAMBOO_BUTTON
hard_ingredients.O:COMPARATOR
hard_ingredients.R:REDSTONE_BLOCK
result:GLOWSTONE_DUST
amount:1
*/

public class ConsoleRusticRecipe {

    private final TARDIS plugin;

    public ConsoleRusticRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ItemStack.of(Material.WAXED_OXIDIZED_COPPER, 1);
        ItemMeta im = is.getItemMeta();
        String dn = "Rustic Console";
        im.displayName(ComponentUtils.toWhite(dn));
        im.lore(List.of(Component.text("Integration with interaction")));
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, "console_rustic");
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "rustic_console");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("CBC", "LRL", "CBC");
            r.setIngredient('C', Material.COPPER_INGOT);
            r.setIngredient('L', Material.LEVER);
            r.setIngredient('R', Material.COMPARATOR);
        } else {
            r.shape("CBC", "ORO", "CBC");
            r.setIngredient('C', Material.COPPER_BLOCK);
            r.setIngredient('O', Material.COMPARATOR);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        }
        r.setIngredient('B', Material.BAMBOO_BUTTON);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put(dn, r);
    }
}
