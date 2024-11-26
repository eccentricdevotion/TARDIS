package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.keys.GoldNugget;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
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
    private final HashMap<String, NamespacedKey> keyModelLookup = new HashMap<>();

    public TARDISKeyRecipe(TARDIS plugin) {
        this.plugin = plugin;
        keyModelLookup.put("first", GoldNugget.BRASS_YALE.getKey());
        keyModelLookup.put("second", GoldNugget.BRASS_PLAIN.getKey());
        keyModelLookup.put("third", GoldNugget.SPADE_SHAPED.getKey());
        keyModelLookup.put("fifth", GoldNugget.SILVER_YALE.getKey());
        keyModelLookup.put("seventh", GoldNugget.SEAL_OF_RASSILON.getKey());
        keyModelLookup.put("ninth", GoldNugget.SILVER_VARIANT.getKey());
        keyModelLookup.put("tenth", GoldNugget.SILVER_PLAIN.getKey());
        keyModelLookup.put("eleventh", GoldNugget.SILVER_NEW.getKey());
        keyModelLookup.put("rose", GoldNugget.SILVER_ERA.getKey());
        keyModelLookup.put("sally", GoldNugget.SILVER_STRING.getKey());
        keyModelLookup.put("perception", GoldNugget.FILTER.getKey());
        keyModelLookup.put("susan", GoldNugget.BRASS_STRING.getKey());
        keyModelLookup.put("gold", GoldNugget.BROMLEY_GOLD.getKey());
    }

    public void addRecipe() {
        NamespacedKey keyModel = keyModelLookup.getOrDefault(plugin.getConfig().getString("preferences.default_key").toLowerCase(Locale.ROOT), RecipeItem.TARDIS_KEY.getModel());
        ItemStack is = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Key");
        im.setItemModel(keyModel);
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
