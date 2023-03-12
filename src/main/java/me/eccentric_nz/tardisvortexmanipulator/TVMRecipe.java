/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TVMRecipe {

    private final TARDISVortexManipulator plugin;

    public TVMRecipe(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    public ShapedRecipe makeRecipe() {
        String[] result_iddata = plugin.getConfig().getString("recipe.result").split(":");
        Material mat = Material.valueOf(result_iddata[0]);
        int amount = plugin.getConfig().getInt("recipe.amount");
        ItemStack is;
        if (result_iddata.length == 2) {
            short result_data = Short.parseShort(result_iddata[1]);
            is = new ItemStack(mat, amount, result_data);
        } else {
            is = new ItemStack(mat, amount);
        }
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Vortex Manipulator");
        if (!plugin.getConfig().getString("recipe.lore").equals("")) {
            im.setLore(Arrays.asList(plugin.getConfig().getString("recipe.lore").split("~")));
        }
        im.setCustomModelData(10000002);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "Vortex_Manipulator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        // get shape
        try {
            String[] shape_tmp = plugin.getConfig().getString("recipe.shape").split(",");
            String[] shape = new String[3];
            for (int i = 0; i < 3; i++) {
                shape[i] = shape_tmp[i].replaceAll("-", " ");
            }
            r.shape(shape[0], shape[1], shape[2]);
            Set<String> ingredients = plugin.getConfig().getConfigurationSection("recipe.ingredients").getKeys(false);
            ingredients.forEach((g) -> {
                char c = g.charAt(0);
                Material m = Material.valueOf(plugin.getConfig().getString("recipe.ingredients." + g));
                r.setIngredient(c, m);
            });
        } catch (IllegalArgumentException e) {
            plugin.getServer().getConsoleSender().sendMessage(plugin.getPluginName() + ChatColor.RED + "Recipe failed! " + ChatColor.RESET + "Check the config file!");
        }
        // add the recipe to TARDIS' list
        plugin.getTardisAPI().getShapedRecipes().put("Vortex Manipulator", r);
        // return the recipe
        return r;
    }
}
