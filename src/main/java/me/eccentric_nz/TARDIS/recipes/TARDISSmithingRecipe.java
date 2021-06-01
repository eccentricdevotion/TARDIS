/*
 * Copyright (C) 2021 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.recipes;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class TARDISSmithingRecipe {

	private final TARDISPlugin plugin;
	private final HashMap<String, SmithingRecipe> smithingRecipes;

	public TARDISSmithingRecipe(TARDISPlugin plugin) {
		this.plugin = plugin;
		smithingRecipes = new HashMap<>();
	}

	public void addSmithingRecipes() {
		Set<String> smithing = Objects.requireNonNull(plugin.getRecipesConfig().getConfigurationSection("smithing")).getKeys(false);
		smithing.forEach((s) -> plugin.getServer().addRecipe(makeRecipe(s)));
	}

	private SmithingRecipe makeRecipe(String s) {
        /*
         base: BLAZE_ROD
         addition: GLOWSTONE_DUST
         result: BLAZE_ROD
         */
		// result
		String result = plugin.getRecipesConfig().getString("smithing." + s + ".result");
		Material mat = Material.valueOf(result);
		ItemStack is = new ItemStack(mat, 1);
		NamespacedKey key = new NamespacedKey(plugin, s.replace(" ", "_").toLowerCase(Locale.ENGLISH));
		// base material to upgrade
		Material bm = Material.valueOf(plugin.getRecipesConfig().getString("smithing." + s + ".base"));
		RecipeChoice base = new RecipeChoice.MaterialChoice(bm);
		// addition material to use
		String[] split = Objects.requireNonNull(plugin.getRecipesConfig().getString(
				"smithing." + s + ".addition")).split("=");
		Material am = Material.valueOf(split[0]);
		ItemStack isa = new ItemStack(am, 1);
		ItemMeta im = isa.getItemMeta();
		assert im != null;
		im.setDisplayName(split[1]);
		im.setCustomModelData(RecipeItem.getByName(split[1]).getCustomModelData());
		isa.setItemMeta(im);
		RecipeChoice addition = new RecipeChoice.ExactChoice(isa);
		SmithingRecipe r = new SmithingRecipe(key, is, base, addition);
		smithingRecipes.put(s, r);
		return r;
	}

	public HashMap<String, SmithingRecipe> getSmithingRecipes() {
		return smithingRecipes;
	}
}
