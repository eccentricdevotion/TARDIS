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
easy_shape:RIR,RIR,-S-
easy_ingredients.R:LAVA_BUCKET=Rust Bucket
easy_ingredients.I:IRON_INGOT
easy_ingredients.S:STICK
hard_shape:RIR,RIR,DSD
hard_ingredients.R:LAVA_BUCKET=Rust Bucket
hard_ingredients.I:IRON_INGOT
hard_ingredients.D:DIAMOND
hard_ingredients.S:STICK
result:IRON_SWORD
amount:1
lore:Dalek Virus Dispenser
*/

public class RustPlagueSwordRecipe {

    private final TARDIS plugin;

    public RustPlagueSwordRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Rust Plague Sword");
        im.setLore(List.of("Dalek Virus Dispenser"));
        is.setItemMeta(im);
        // set weapon component
        TARDIS.plugin.getComponentSetter().setWeapon(is);
        NamespacedKey key = new NamespacedKey(plugin, "rust_plague_sword");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = new ItemStack(Material.LAVA_BUCKET, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName(ChatColor.WHITE + "Rust Bucket");
        exact.setItemMeta(em);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("RIR", "RIR", "DSD");
            r.setIngredient('D', Material.DIAMOND);
        } else {
            r.shape("RIR", "RIR", " S ");
        }
        r.setIngredient('R', new RecipeChoice.ExactChoice(exact));
        r.setIngredient('I', Material.IRON_INGOT);
        r.setIngredient('S', Material.STICK);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Rust Plague Sword", r);
    }
}
