package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/*
easy_shape:R,G
easy_ingredients.R:REDSTONE
easy_ingredients.G:GOLD_NUGGET
hard_shape:C,G
hard_ingredients.C:COMPARATOR
hard_ingredients.G:GOLD_NUGGET
result:GOLD_NUGGET
amount:1
lore:Enter and exit your TARDIS
*/

public class TARDISKeyRecipe {

    private final TARDIS plugin;
    private final HashMap<String, Integer> keyModelLookup = new HashMap<>();

    public TARDISKeyRecipe(TARDIS plugin) {
        this.plugin = plugin;
        keyModelLookup.put("first", 1);
        keyModelLookup.put("second", 2);
        keyModelLookup.put("third", 3);
        keyModelLookup.put("fifth", 4);
        keyModelLookup.put("seventh", 5);
        keyModelLookup.put("ninth", 6);
        keyModelLookup.put("tenth", 7);
        keyModelLookup.put("eleventh", 8);
        keyModelLookup.put("rose", 9);
        keyModelLookup.put("sally", 10);
        keyModelLookup.put("perception", 11);
        keyModelLookup.put("susan", 12);
        keyModelLookup.put("gold", 13);
    }

    public void addRecipe() {
        int keyModel = keyModelLookup.getOrDefault(plugin.getConfig().getString("preferences.default_key").toLowerCase(Locale.ENGLISH), 1);
        ItemStack is = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Key");
        im.setCustomModelData(keyModel);
        im.setLore(List.of("Enter and exit your TARDIS"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_key");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("C", "G");
            r.setIngredient('C', Material.COMPARATOR);
        } else {
            r.shape("R", "G");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('G', Material.GOLD_NUGGET);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Key", r);
    }
}
