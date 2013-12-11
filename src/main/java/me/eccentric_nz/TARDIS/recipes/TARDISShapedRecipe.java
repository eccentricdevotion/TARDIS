/*
 *  Copyright 2013 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.recipes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISShapedRecipe {

    private final TARDIS plugin;
    private final HashMap<String, ShapedRecipe> shapedRecipes;

    public TARDISShapedRecipe(TARDIS plugin) {
        this.plugin = plugin;
        this.shapedRecipes = new HashMap<String, ShapedRecipe>();
    }

    public void addShapedRecipes() {
        Set<String> shaped = plugin.getRecipesConfig().getConfigurationSection("shaped").getKeys(false);
        for (String s : shaped) {
            plugin.getServer().addRecipe(makeRecipe(s));
        }
    }

    private ShapedRecipe makeRecipe(String s) {
        /*
         shape: A-A,BBB,CDC
         ingredients:
         A: 1
         B: 2
         C: '5:2'
         D: 57
         result: 276
         amount: 1
         lore: "The vorpal blade\ngoes snicker-snack!"
         enchantment: FIRE_ASPECT
         strength: 3
         */
        String[] result_iddata = plugin.getRecipesConfig().getString("shaped." + s + ".result").split(":");
        Material mat = Material.valueOf(result_iddata[0]);
        int amount = plugin.getRecipesConfig().getInt("shaped." + s + ".amount");
        ItemStack is;
        if (result_iddata.length == 2) {
            short result_data = Short.parseShort(result_iddata[1]);
            is = new ItemStack(mat, amount, result_data);
        } else {
            is = new ItemStack(mat, amount);
        }
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(s);
        if (!plugin.getRecipesConfig().getString("shaped." + s + ".lore").equals("")) {
            im.setLore(Arrays.asList(plugin.getRecipesConfig().getString("shaped." + s + ".lore").split("\n")));
        }
        if (!plugin.getRecipesConfig().getString("shaped." + s + ".enchantment").equals("NONE")) {
            Enchantment e = EnchantmentWrapper.getByName(plugin.getRecipesConfig().getString("shaped." + s + ".enchantment"));
            boolean did = im.addEnchant(e, plugin.getRecipesConfig().getInt("shaped." + s + ".strength"), plugin.getConfig().getBoolean("allow_unsafe_enchantments"));
            plugin.debug((did) ? " true" : "false");
        }
        is.setItemMeta(im);
        ShapedRecipe r = new ShapedRecipe(is);
        // get shape
        String difficulty = plugin.getConfig().getString("difficulty");
        String[] shape_tmp = plugin.getRecipesConfig().getString("shaped." + s + "." + difficulty + "_shape").split(",");
        String[] shape = new String[3];
        for (int i = 0; i < 3; i++) {
            shape[i] = shape_tmp[i].replaceAll("-", " ");
        }
        r.shape(shape[0], shape[1], shape[2]);
        Set<String> ingredients = plugin.getRecipesConfig().getConfigurationSection("shaped." + s + "." + difficulty + "_ingredients").getKeys(false);
        for (String g : ingredients) {
            char c = g.charAt(0);
            String[] recipe_iddata = plugin.getRecipesConfig().getString("shaped." + s + "." + difficulty + "_ingredients." + g).split(":");
            Material m = Material.valueOf(recipe_iddata[0]);
            if (recipe_iddata.length == 2) {
                int recipe_data = Integer.parseInt(recipe_iddata[1]);
                r.setIngredient(c, m, recipe_data);
            } else {
                r.setIngredient(c, m);
            }
        }
        shapedRecipes.put(s, r);
        return r;
    }

    public HashMap<String, ShapedRecipe> getShapedRecipes() {
        return shapedRecipes;
    }
}
