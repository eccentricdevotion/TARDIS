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
package me.eccentric_nz.tardissonicblaster;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSonicBlasterRecipe {

    private final TARDIS plugin;
    private final HashMap<String, NamespacedKey> modelData = new HashMap<>();

    public TARDISSonicBlasterRecipe(TARDIS plugin) {
        this.plugin = plugin;
        modelData.put("Sonic Blaster", RecipeItem.SONIC_BLASTER.getModel());
        modelData.put("Blaster Battery", RecipeItem.BLASTER_BATTERY.getModel());
        modelData.put("Landing Pad", RecipeItem.LANDING_PAD.getModel());
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
            im.setLore(List.of(plugin.getBlasterConfig().getString("recipes." + s + ".lore").split("~")));
        }
        im.setItemModel(modelData.get(s));
        im.addItemFlags(ItemFlag.values());
        im.setAttributeModifiers(Multimaps.forMap(Map.of()));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(TARDIS.plugin, s.replace(" ", "_").toLowerCase(Locale.ROOT));
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
                    em.setItemModel(RecipeItem.getByName(choice[1]).getModel());
                    exact.setItemMeta(em);
                    r.setIngredient(c, new RecipeChoice.ExactChoice(exact));
                } else {
                    Material m = Material.valueOf(ingredient);
                    r.setIngredient(c, m);
                }
                plugin.getFigura().getShapedRecipes().put(s, r);
            }
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.BLASTER, "Recipe failed! Check the config file!");
        }
        // add the recipe to TARDIS' list
        plugin.getFigura().getShapedRecipes().put(s, r);
        // return the recipe
        return r;
    }
}
