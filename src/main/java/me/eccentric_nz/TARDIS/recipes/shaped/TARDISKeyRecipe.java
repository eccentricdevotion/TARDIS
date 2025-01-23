package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.KeyVariant;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.format.NamedTextColor;
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
        keyModelLookup.put("first", KeyVariant.BRASS_YALE.getKey());
        keyModelLookup.put("second", KeyVariant.BRASS_PLAIN.getKey());
        keyModelLookup.put("third", KeyVariant.SPADE_SHAPED.getKey());
        keyModelLookup.put("fifth", KeyVariant.SILVER_YALE.getKey());
        keyModelLookup.put("seventh", KeyVariant.SEAL_OF_RASSILON.getKey());
        keyModelLookup.put("ninth", KeyVariant.SILVER_VARIANT.getKey());
        keyModelLookup.put("tenth", KeyVariant.SILVER_PLAIN.getKey());
        keyModelLookup.put("eleventh", KeyVariant.SILVER_NEW.getKey());
        keyModelLookup.put("rose", KeyVariant.SILVER_ERA.getKey());
        keyModelLookup.put("sally", KeyVariant.SILVER_STRING.getKey());
        keyModelLookup.put("perception", KeyVariant.FILTER.getKey());
        keyModelLookup.put("susan", KeyVariant.BRASS_STRING.getKey());
        keyModelLookup.put("gold", KeyVariant.BROMLEY_GOLD.getKey());
    }

    public void addRecipe() {
        NamespacedKey keyModel = keyModelLookup.getOrDefault(plugin.getConfig().getString("preferences.default_key").toLowerCase(Locale.ROOT), RecipeItem.TARDIS_KEY.getModel());
        ItemStack is = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(NamedTextColor.WHITE + "TARDIS Key");
        im.setItemModel(keyModel);
        im.lore(List.of("Enter and exit your TARDIS"));
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
