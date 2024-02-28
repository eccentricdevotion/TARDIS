package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:RDR,LWL,RCR
easy_ingredients.R:REDSTONE
easy_ingredients.D:DISPENSER
easy_ingredients.L:LEATHER
easy_ingredients.W:WATER_BUCKET
easy_ingredients.C:COMPARATOR
hard_shape:RDR,PWP,RCR
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.D:DISPENSER
hard_ingredients.W:WATER_BUCKET
hard_ingredients.P:PHANTOM_MEMBRANE
hard_ingredients.C:COMPARATOR
result:GLOWSTONE_DUST
amount:1
*/

public class ConversionCircuitRecipe {

    private final TARDIS plugin;

    public ConversionCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Conversion Circuit");
        im.setCustomModelData(10001988);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "conversion_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r.shape("RDR", "PWP", "RCR");
            r.setIngredient('R', Material.REDSTONE_BLOCK);
            r.setIngredient('D', Material.DISPENSER);
            r.setIngredient('W', Material.WATER_BUCKET);
            r.setIngredient('P', Material.PHANTOM_MEMBRANE);
            r.setIngredient('C', Material.COMPARATOR);            
        } else {
            r.shape("RDR", "LWL", "RCR");
            r.setIngredient('R', Material.REDSTONE);
            r.setIngredient('D', Material.DISPENSER);
            r.setIngredient('L', Material.LEATHER);
            r.setIngredient('W', Material.WATER_BUCKET);
            r.setIngredient('C', Material.COMPARATOR);            
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Conversion Circuit", r);
    }
}
