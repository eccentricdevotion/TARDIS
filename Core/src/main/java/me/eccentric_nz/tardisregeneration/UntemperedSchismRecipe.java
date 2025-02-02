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
package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/*
easy_shape:NNN,NTN,NNN
easy_ingredients.N:NETHERITE_INGOT
easy_ingredients.T:TOTEM_OF_UNDYING
hard_shape:NHN,STS,NGN
hard_ingredients.N:NETHERITE_BLOCK
hard_ingredients.H:HEART_OF_THE_SEA
hard_ingredients.S:ECHO_SHARD
hard_ingredients.T:TOTEM_OF_UNDYING
hard_ingredients.G:GOLDEN_APPLE
result:ANCIENT_DEBRIS
amount:1
*/

public class UntemperedSchismRecipe {

    private final TARDIS plugin;

    public UntemperedSchismRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = UntemperedSchism.create();
        NamespacedKey key = new NamespacedKey(plugin, "untempered_schism");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("NHN", "ETE", "NGN");
            r.setIngredient('N', Material.NETHERITE_BLOCK);
            r.setIngredient('H', Material.HEART_OF_THE_SEA);
            r.setIngredient('E', Material.ECHO_SHARD);
            r.setIngredient('G', Material.GOLDEN_APPLE);
        } else {
            r.shape("NNN", "NTN", "NNN");
            r.setIngredient('N', Material.NETHERITE_INGOT);
        }
        r.setIngredient('T', Material.TOTEM_OF_UNDYING);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Untempered Schism", r);
    }
}

