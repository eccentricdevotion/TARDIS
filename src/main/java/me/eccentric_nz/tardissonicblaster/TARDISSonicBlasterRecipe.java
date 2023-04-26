/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.tardissonicblaster;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSonicBlasterRecipe {

    private final TARDIS plugin;
    private final HashMap<String, Integer> modelData = new HashMap<>();

    public TARDISSonicBlasterRecipe(TARDIS plugin) {
        this.plugin = plugin;
        modelData.put("Sonic Blaster", 10000001);
        modelData.put("Blaster Battery", 10000002);
        modelData.put("Landing Pad", 10000001);
    }

    public void addShapedRecipes() {
        Set<String> shaped = plugin.getBlasterConfig().getConfigurationSection("recipes").getKeys(false);
        for (String s : shaped) {
            plugin.getServer().addRecipe(makeRecipe(s));
        }
    }

    public ShapedRecipe makeRecipe(String s) {
        Material mat = Material.valueOf(plugin.getBlasterConfig().getString("recipes." + s + ".result"));
        int amount = plugin.getBlasterConfig().getInt("recipes." + s + ".amount");
        ItemStack is = new ItemStack(mat, amount);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(s);
        if (!plugin.getBlasterConfig().getString("recipes." + s + ".lore").isEmpty()) {
            im.setLore(Arrays.asList(plugin.getBlasterConfig().getString("recipes." + s + ".lore").split("~")));
        }
        im.setCustomModelData(modelData.get(s));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(TARDIS.plugin, s.replace(" ", "_").toLowerCase(Locale.ENGLISH));
        ShapedRecipe r = new ShapedRecipe(key, is);
        // get shape
        try {
            String[] shape_tmp = plugin.getBlasterConfig().getString("recipes." + s + ".shape").split(",");
            String[] shape = new String[3];
            for (int i = 0; i < 3; i++) {
                shape[i] = shape_tmp[i].replaceAll("-", " ");
            }
            r.shape(shape[0], shape[1], shape[2]);
            Set<String> ingredients = plugin.getBlasterConfig().getConfigurationSection("recipes." + s + ".ingredients").getKeys(false);
            for (String g : ingredients) {
                char c = g.charAt(0);
                String ingredient = plugin.getBlasterConfig().getString("recipes." + s + ".ingredients." + g);
                if (ingredient.contains("=")) {
                    ItemStack exact;
                    String[] choice = ingredient.split("=");
                    Material m = Material.valueOf(choice[0]);
                    exact = new ItemStack(m, 1);
                    ItemMeta em = exact.getItemMeta();
                    em.setDisplayName(choice[1]);
                    em.setCustomModelData(RecipeItem.getByName(choice[1]).getCustomModelData());
                    exact.setItemMeta(em);
                    r.setIngredient(c, new RecipeChoice.ExactChoice(exact));
                } else {
                    Material m = Material.valueOf(ingredient);
                    r.setIngredient(c, m);
                }
            }
        } catch (IllegalArgumentException e) {
            plugin.getServer().getConsoleSender().sendMessage(TardisModule.BLASTER.getName() + ChatColor.RED + "Recipe failed! " + ChatColor.RESET + "Check the config file!");
        }
        // add the recipe to TARDIS' list
        plugin.getFigura().getShapedRecipes().put(s, r);
        // return the recipe
        return r;
    }
}
