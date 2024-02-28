package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:RCR,-I-,RCR
easy_ingredients.R:REDSTONE
easy_ingredients.C:COMPARATOR
easy_ingredients.I:IRON_INGOT
hard_shape:RCR,-I-,RCR
hard_ingredients.R:REPEATER
hard_ingredients.C:COMPARATOR
hard_ingredients.I:IRON_INGOT
result:GLOWSTONE_DUST
amount:1
*/

public class RedstoneActivatorCircuitRecipe {

    private final TARDIS plugin;

    public RedstoneActivatorCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Redstone Activator Circuit");
        im.setCustomModelData(10001970);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "redstone_activator_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r.shape("RCR", " I ", "RCR");
            r.setIngredient('R', Material.REPEATER);
            r.setIngredient('C', Material.COMPARATOR);
            r.setIngredient('I', Material.IRON_INGOT);            
        } else {
            r.shape("RCR", " I ", "RCR");
            r.setIngredient('R', Material.REDSTONE);
            r.setIngredient('C', Material.COMPARATOR);
            r.setIngredient('I', Material.IRON_INGOT);            
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Redstone Activator Circuit", r);
    }
}
