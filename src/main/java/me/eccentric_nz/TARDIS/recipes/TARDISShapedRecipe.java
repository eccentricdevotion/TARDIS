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
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISShapedRecipe {

    private final TARDIS plugin;

    public TARDISShapedRecipe(TARDIS plugin) {
        this.plugin = plugin;
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
         displayname: true
         lore: "The vorpal blade\ngoes snicker-snack!"
         enchantment: FIRE_ASPECT
         strength: 3
         */
        String[] result_iddata = plugin.getRecipesConfig().getString("shaped." + s + ".result").split(":");
        int result_id = Integer.parseInt(result_iddata[0]);
        Material mat = Material.getMaterial(result_id);
        int amount = plugin.getRecipesConfig().getInt("shaped." + s + ".amount");
        ItemStack is;
        if (result_iddata.length == 2) {
            byte result_data = Byte.parseByte(result_iddata[1]);
            is = new ItemStack(mat, amount, result_data);
        } else {
            is = new ItemStack(mat, amount);
        }
        ItemMeta im = is.getItemMeta();
        boolean set_meta = false;
        if (plugin.getRecipesConfig().getBoolean("shaped." + s + ".displayname")) {
            im.setDisplayName(s);
            if (!plugin.getRecipesConfig().getString("shaped." + s + ".lore").equals("")) {
                im.setLore(Arrays.asList(plugin.getRecipesConfig().getString("shaped." + s + ".lore").split("\n")));
            }
            set_meta = true;
        }
        if (!plugin.getRecipesConfig().getString("shaped." + s + ".enchantment").equals("NONE")) {
            Enchantment e = EnchantmentWrapper.getByName(plugin.getRecipesConfig().getString("shaped." + s + ".enchantment"));
            boolean did = im.addEnchant(e, plugin.getRecipesConfig().getInt("shaped." + s + ".strength"), plugin.getConfig().getBoolean("allow_unsafe_enchantments"));
            System.out.println((did) ? "true" : "false");
            set_meta = true;
        }
        if (set_meta) {
            is.setItemMeta(im);
        }
        ShapedRecipe r = new ShapedRecipe(is);
        // get shape
        String[] shape_tmp = plugin.getRecipesConfig().getString("shaped." + s + ".shape").split(",");
        String[] shape = new String[3];
        for (int i = 0; i < 3; i++) {
            shape[i] = shape_tmp[i].replaceAll("-", " ");
        }
        r.shape(shape[0], shape[1], shape[2]);
        Set<String> ingredients = plugin.getRecipesConfig().getConfigurationSection("shaped." + s + ".ingredients").getKeys(false);
        for (String g : ingredients) {
            char c = g.charAt(0);
            String[] recipe_iddata = plugin.getRecipesConfig().getString("shaped." + s + ".ingredients." + g).split(":");
            int recipe_id = Integer.parseInt(recipe_iddata[0]);
            Material m = Material.getMaterial(recipe_id);
            if (recipe_iddata.length == 2) {
                int recipe_data = Integer.parseInt(recipe_iddata[1]);
                r.setIngredient(c, m, recipe_data);
            } else {
                r.setIngredient(c, m);
            }
        }
        return r;
    }
}
