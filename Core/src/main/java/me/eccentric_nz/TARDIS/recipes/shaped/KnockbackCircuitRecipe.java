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
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

/*
easy_shape:-K-,RSR,-R-
easy_ingredients.K:GOLDEN_SWORD
easy_ingredients.R:REDSTONE
easy_ingredients.S:SHIELD
hard_shape:-K-,RSR,-R-
hard_ingredients.K:ENCHANTED_BOOK≈KNOCKBACK
hard_ingredients.R:REDSTONE
hard_ingredients.S:SHIELD
result:GLOWSTONE_DUST
amount:1
*/

public class KnockbackCircuitRecipe {

    private final TARDIS plugin;

    public KnockbackCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Knockback Circuit");
        CustomModelDataComponent component = im.getCustomModelDataComponent();
        component.setFloats(CircuitVariant.KNOCKBACK.getFloats());
        im.setCustomModelDataComponent(component);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "knockback_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" K ", "RSR", " R ");
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
            EnchantmentStorageMeta pm = (EnchantmentStorageMeta) book.getItemMeta();
            Enchantment enchantment = Enchantment.KNOCKBACK;
            pm.addStoredEnchant(enchantment, 1, false);
            book.setItemMeta(pm);
            r.setIngredient('K', new RecipeChoice.ExactChoice(book));
        } else {
            r.shape(" K ", "RSR", " R ");
            r.setIngredient('K', Material.GOLDEN_SWORD);
        }
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('S', Material.SHIELD);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Knockback Circuit", r);
    }
}
