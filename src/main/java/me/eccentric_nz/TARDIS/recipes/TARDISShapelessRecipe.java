/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RECIPE_ITEM;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISShapelessRecipe {

    private final TARDIS plugin;
    private final HashMap<String, ShapelessRecipe> shapelessRecipes;

    public TARDISShapelessRecipe(TARDIS plugin) {
        this.plugin = plugin;
        shapelessRecipes = new HashMap<>();
    }

    public void addShapelessRecipes() {
        Set<String> shapeless = plugin.getRecipesConfig().getConfigurationSection("shapeless").getKeys(false);
        shapeless.forEach((s) -> plugin.getServer().addRecipe(makeRecipe(s)));
    }

    private ShapelessRecipe makeRecipe(String s) {
        /*
         recipe: VINE,VINE,VINE=Special Vine
         result: SLIME_BALL
         amount: 1
         lore: ""
         */
        String[] ingredients = plugin.getRecipesConfig().getString("shapeless." + s + ".recipe").split(",");
        String result_iddata = plugin.getRecipesConfig().getString("shapeless." + s + ".result");
        Material mat = Material.valueOf(result_iddata);
        int amount = plugin.getRecipesConfig().getInt("shapeless." + s + ".amount");
        ItemStack is = new ItemStack(mat, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(s);
        if (!plugin.getRecipesConfig().getString("shapeless." + s + ".lore").equals("")) {
            im.setLore(Arrays.asList(plugin.getRecipesConfig().getString("shapeless." + s + ".lore").split("~")));
        }
        im.setCustomModelData(RECIPE_ITEM.getByName(s).getCustomModelData());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, s.replace(" ", "_").toLowerCase(Locale.ENGLISH));
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        for (String i : ingredients) {
            if (i.contains("=")) {
                ItemStack exact;
                String[] choice = i.split("=");
                Material m = Material.valueOf(choice[0]);
                exact = new ItemStack(m, 1);
                ItemMeta em = exact.getItemMeta();
                em.setDisplayName(choice[1]);
                exact.setItemMeta(em);
                r.addIngredient(new RecipeChoice.ExactChoice(exact));
            } else {
                Material m = Material.valueOf(i);
                r.addIngredient(m);
            }
        }
        if (s.contains("Jelly Baby")) {
            r.setGroup("Jelly Babies");
        }
        shapelessRecipes.put(s, r);
        return r;
    }

    public HashMap<String, ShapelessRecipe> getShapelessRecipes() {
        return shapelessRecipes;
    }
}
