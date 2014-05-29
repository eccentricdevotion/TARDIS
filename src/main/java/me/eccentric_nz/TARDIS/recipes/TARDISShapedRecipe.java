/*
 *  Copyright 2013 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.recipes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    private ChatColor keyDisplay;
    private ChatColor sonicDisplay;
    private final HashMap<String, ChatColor> sonic_colour_lookup = new HashMap<String, ChatColor>();
    private final HashMap<String, ChatColor> key_colour_lookup = new HashMap<String, ChatColor>();

    public TARDISShapedRecipe(TARDIS plugin) {
        this.plugin = plugin;
        this.shapedRecipes = new HashMap<String, ShapedRecipe>();
        this.sonic_colour_lookup.put("mark_1", ChatColor.DARK_GRAY);
        this.sonic_colour_lookup.put("mark_2", ChatColor.YELLOW);
        this.sonic_colour_lookup.put("mark_3", ChatColor.DARK_PURPLE);
        this.sonic_colour_lookup.put("mark_4", ChatColor.GRAY);
        this.sonic_colour_lookup.put("eighth", ChatColor.BLUE);
        this.sonic_colour_lookup.put("ninth", ChatColor.GREEN);
        this.sonic_colour_lookup.put("ninth_open", ChatColor.DARK_GREEN);
        this.sonic_colour_lookup.put("tenth", ChatColor.AQUA);
        this.sonic_colour_lookup.put("tenth_open", ChatColor.DARK_AQUA);
        this.sonic_colour_lookup.put("eleventh", null);
        this.sonic_colour_lookup.put("eleventh_open", ChatColor.LIGHT_PURPLE);
        this.sonic_colour_lookup.put("master", ChatColor.DARK_BLUE);
        this.sonic_colour_lookup.put("sarah_jane", ChatColor.RED);
        this.sonic_colour_lookup.put("river_song", ChatColor.GOLD);
        this.sonic_colour_lookup.put("war", ChatColor.DARK_RED);
        this.key_colour_lookup.put("first", ChatColor.AQUA);
        this.key_colour_lookup.put("second", ChatColor.DARK_BLUE);
        this.key_colour_lookup.put("third", ChatColor.LIGHT_PURPLE);
        this.key_colour_lookup.put("fifth", ChatColor.DARK_RED);
        this.key_colour_lookup.put("seventh", ChatColor.GRAY);
        this.key_colour_lookup.put("ninth", ChatColor.DARK_PURPLE);
        this.key_colour_lookup.put("tenth", ChatColor.GREEN);
        this.key_colour_lookup.put("eleventh", null);
        this.key_colour_lookup.put("susan", ChatColor.YELLOW);
        this.key_colour_lookup.put("rose", ChatColor.RED);
        this.key_colour_lookup.put("sally", ChatColor.DARK_AQUA);
        this.key_colour_lookup.put("perception", ChatColor.BLUE);
        this.key_colour_lookup.put("gold", ChatColor.GOLD);
    }

    public void addShapedRecipes() {
        keyDisplay = key_colour_lookup.get(plugin.getConfig().getString("preferences.default_key").toLowerCase());
        sonicDisplay = sonic_colour_lookup.get(plugin.getConfig().getString("preferences.default_sonic").toLowerCase());
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
            short result_data = plugin.getUtils().parseShort(result_iddata[1]);
            is = new ItemStack(mat, amount, result_data);
        } else {
            is = new ItemStack(mat, amount);
        }
        ItemMeta im = is.getItemMeta();
        if (s.equals("TARDIS Key") && keyDisplay != null) {
            im.setDisplayName(keyDisplay + s);
        } else if (s.equals("Sonic Screwdriver") && sonicDisplay != null) {
            im.setDisplayName(sonicDisplay + s);
        } else {
            im.setDisplayName(s);
        }
        if (!plugin.getRecipesConfig().getString("shaped." + s + ".lore").equals("")) {
            im.setLore(Arrays.asList(plugin.getRecipesConfig().getString("shaped." + s + ".lore").split("\n")));
        }
        is.setItemMeta(im);
        ShapedRecipe r = new ShapedRecipe(is);
        // get shape
        String difficulty = plugin.getConfig().getString("preferences.difficulty");
        try {
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
                    int recipe_data = plugin.getUtils().parseInt(recipe_iddata[1]);
                    r.setIngredient(c, m, recipe_data);
                } else {
                    r.setIngredient(c, m);
                }
            }
        } catch (IllegalArgumentException e) {
            plugin.getConsole().sendMessage(plugin.getPluginName() + ChatColor.RED + s + " recipe failed! " + ChatColor.RESET + "Check the recipe config file!");
        }
        shapedRecipes.put(s, r);
        return r;
    }

    public HashMap<String, ShapedRecipe> getShapedRecipes() {
        return shapedRecipes;
    }
}
