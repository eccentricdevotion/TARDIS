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
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Set;

/**
 * @author eccentric_nz
 */
class TARDISFurnaceRecipe {

    private final TARDIS plugin;

    public TARDISFurnaceRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addFurnaceRecipes() {
        Set<String> furnace = plugin.getRecipesConfig().getConfigurationSection("furnace").getKeys(false);
        furnace.forEach((s) -> plugin.getServer().addRecipe(makeRecipe(s)));
    }

    private FurnaceRecipe makeRecipe(String s) {
        FurnaceRecipe f;
        /*
         recipe: '35:0'
         result: '35:15'
         experience: 0.0
         cooktime: 200
         displayname: true
         lore: "Done to a crisp"
         */
        ItemStack is;
        String result = plugin.getRecipesConfig().getString("furnace." + s + ".result");
        Material result_m = Material.valueOf(result);
        is = new ItemStack(result_m, 1);
        ItemMeta im = is.getItemMeta();
        boolean set_meta = false;
        if (plugin.getRecipesConfig().getBoolean("furnace." + s + ".displayname")) {
            im.setDisplayName(s);
            if (!plugin.getRecipesConfig().getString("furnace." + s + ".lore").equals("")) {
                im.setLore(Arrays.asList(plugin.getRecipesConfig().getString("furnace." + s + ".lore").split("\n")));
            }
            set_meta = true;
        }
        if (!plugin.getRecipesConfig().getString("furnace." + s + ".enchantment").equals("NONE")) {
            Enchantment e = EnchantmentWrapper.getByName(plugin.getRecipesConfig().getString("furnace." + s + ".enchantment"));
            im.addEnchant(e, plugin.getRecipesConfig().getInt("furnace." + s + ".strength"), plugin.getConfig().getBoolean("allow_unsafe_enchantments"));
            set_meta = true;
        }
        if (set_meta) {
            is.setItemMeta(im);
        }
        String ingredient = plugin.getRecipesConfig().getString("furnace." + s + ".recipe");
        Material recipe_m = Material.valueOf(ingredient);
        float experience = (float) plugin.getRecipesConfig().getDouble("furnace." + s + ".experience");
        int cooktime = plugin.getRecipesConfig().getInt("furnace." + s + ".cooktime");
        NamespacedKey key = new NamespacedKey(plugin, s.replace(" ", "_"));
        f = new FurnaceRecipe(key, is, recipe_m, experience, cooktime);

        return f;
    }
}
