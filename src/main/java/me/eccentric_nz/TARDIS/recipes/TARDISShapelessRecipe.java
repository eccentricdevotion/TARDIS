/*
 *  Copyright 2013 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.recipes;

import java.util.Arrays;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISShapelessRecipe {

    private final TARDIS plugin;

    public TARDISShapelessRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addShapelessRecipes() {
        Set<String> shapeless = plugin.getRecipesConfig().getConfigurationSection("shapeless").getKeys(false);
        for (String s : shapeless) {
            plugin.getServer().addRecipe(makeRecipe(s));
        }
    }

    private ShapelessRecipe makeRecipe(String s) {
        /*
         recipe: 106,106,106
         result: 341
         amount: 1
         displayname: false
         lore: ""
         */
        String[] ingredients = plugin.getRecipesConfig().getString("shapeless." + s + ".recipe").split(",");
        String[] result_iddata = plugin.getRecipesConfig().getString("shapeless." + s + ".result").split(":");
        Material mat = Material.valueOf(result_iddata[0]);
        int amount = plugin.getRecipesConfig().getInt("shapeless." + s + ".amount");
        ItemStack is;
        if (result_iddata.length == 2) {
            short result_data = Short.parseShort(result_iddata[1]);
            is = new ItemStack(mat, amount, result_data);
        } else {
            is = new ItemStack(mat, amount);
        }
        ItemMeta im = is.getItemMeta();
        boolean set_meta = false;
        if (plugin.getRecipesConfig().getBoolean("shapeless." + s + ".displayname")) {
            im.setDisplayName(s);
            if (!plugin.getRecipesConfig().getString("shapeless." + s + ".lore").equals("")) {
                im.setLore(Arrays.asList(plugin.getRecipesConfig().getString("shapeless." + s + ".lore").split("\n")));
            }
            set_meta = true;
        }
        if (!plugin.getRecipesConfig().getString("shapeless." + s + ".enchantment").equals("NONE")) {
            Enchantment e = EnchantmentWrapper.getByName(plugin.getRecipesConfig().getString("shapeless." + s + ".enchantment"));
            im.addEnchant(e, plugin.getRecipesConfig().getInt("shapeless." + s + ".strength"), plugin.getConfig().getBoolean("allow_unsafe_enchantments"));
            set_meta = true;
        }
        if (set_meta) {
            is.setItemMeta(im);
        }
        ShapelessRecipe r = new ShapelessRecipe(is);
        for (String i : ingredients) {
            String[] recipe_iddata = i.split(":");
            int recipe_id = Integer.parseInt(recipe_iddata[0]);
            Material m = Material.getMaterial(recipe_id);
            if (recipe_iddata.length == 2) {
                int recipe_data = Integer.parseInt(recipe_iddata[1]);
                r.addIngredient(m, recipe_data);
            } else {
                r.addIngredient(m);
            }
        }
        return r;
    }
}
