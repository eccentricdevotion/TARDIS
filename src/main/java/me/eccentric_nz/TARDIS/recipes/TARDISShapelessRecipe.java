/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
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
         recipe: VINE,VINE,VINE
         result: SLIME_BALL
         amount: 1
         displayname: false
         lore: ""
         */
        String[] ingredients = plugin.getRecipesConfig().getString("shapeless." + s + ".recipe").split(",");
        String[] result_iddata = plugin.getRecipesConfig().getString("shapeless." + s + ".result").split(":");
        Material mat = Material.valueOf(result_iddata[0]);
        int amount = plugin.getRecipesConfig().getInt("shapeless." + s + ".amount");
        ItemStack is;
        if (result_iddata.length == 2 && mat.equals(Material.FILLED_MAP)) {
            int map = TARDISNumberParsers.parseInt(result_iddata[1]);
            is = plugin.getTardisHelper().setMapNBT(new ItemStack(mat, amount), map);
        } else {
            is = new ItemStack(mat, amount);
        }
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(s);
        if (!plugin.getRecipesConfig().getString("shapeless." + s + ".lore").equals("")) {
            im.setLore(Arrays.asList(plugin.getRecipesConfig().getString("shapeless." + s + ".lore").split("\n")));
        }
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, s.replace(" ", "_").toLowerCase(Locale.ENGLISH));
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        for (String i : ingredients) {
            String[] recipe_idata = i.split(":");
            Material m = Material.valueOf(recipe_idata[0]);
            r.addIngredient(m);
        }
        shapelessRecipes.put(s, r);
        return r;
    }

    public HashMap<String, ShapelessRecipe> getShapelessRecipes() {
        return shapelessRecipes;
    }
}
