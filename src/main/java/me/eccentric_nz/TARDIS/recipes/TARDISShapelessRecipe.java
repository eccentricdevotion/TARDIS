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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.recipes;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISShapelessRecipe {

    private final TARDISPlugin plugin;
    private final HashMap<String, ShapelessRecipe> shapelessRecipes;

    public TARDISShapelessRecipe(TARDISPlugin plugin) {
        this.plugin = plugin;
        shapelessRecipes = new HashMap<>();
    }

    public void addShapelessRecipes() {
        Set<String> shapeless = Objects.requireNonNull(plugin.getRecipesConfig().getConfigurationSection("shapeless")).getKeys(false);
        shapeless.forEach((s) -> plugin.getServer().addRecipe(makeRecipe(s)));
    }

    private ShapelessRecipe makeRecipe(String s) {
        /*
         recipe: VINE,VINE,VINE=Special Vine
         result: SLIME_BALL
         amount: 1
         lore: ""
         */
        String[] ingredients = Objects.requireNonNull(plugin.getRecipesConfig().getString("shapeless." + s + ".recipe")).split(",");
        String result = plugin.getRecipesConfig().getString("shapeless." + s + ".result");
        Material mat = Material.valueOf(result);
        int amount = plugin.getRecipesConfig().getInt("shapeless." + s + ".amount");
        ItemStack is = new ItemStack(mat, amount);
        ItemMeta im = is.getItemMeta();
        assert im != null;
        im.setDisplayName(s);
        if (!Objects.equals(plugin.getRecipesConfig().getString("shapeless." + s + ".lore"), "")) {
            im.setLore(Arrays.asList(Objects.requireNonNull(plugin.getRecipesConfig().getString("shapeless." + s + ".lore")).split("~")));
        }
        im.setCustomModelData(RecipeItem.getByName(s).getCustomModelData());
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
                assert em != null;
                em.setDisplayName(choice[1]);
                em.setCustomModelData(RecipeItem.getByName(choice[1]).getCustomModelData());
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
