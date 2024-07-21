package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:IGI,CEC,DTD
easy_ingredients.I:IRON_INGOT
easy_ingredients.G:GOLDEN_CARROT
easy_ingredients.C:COMPARATOR
easy_ingredients.E:FERMENTED_SPIDER_EYE
easy_ingredients.D:REPEATER
easy_ingredients.T:REDSTONE_TORCH
hard_shape:IGI,CEC,DTD
hard_ingredients.I:IRON_INGOT
hard_ingredients.G:GOLDEN_CARROT
hard_ingredients.C:COMPARATOR
hard_ingredients.E:FERMENTED_SPIDER_EYE
hard_ingredients.D:REPEATER
hard_ingredients.T:REDSTONE_TORCH
result:GLOWSTONE_DUST
amount:1
*/

public class PerceptionCircuitRecipe {

    private final TARDIS plugin;

    public PerceptionCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Perception Circuit");
        im.setCustomModelData(10001978);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "perception_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("IGI", "CEC", "DTD");
        r.setIngredient('I', Material.IRON_INGOT);
        r.setIngredient('G', Material.GOLDEN_CARROT);
        r.setIngredient('C', Material.COMPARATOR);
        r.setIngredient('E', Material.FERMENTED_SPIDER_EYE);
        r.setIngredient('D', Material.REPEATER);
        r.setIngredient('T', Material.REDSTONE_TORCH);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Perception Circuit", r);
    }
}
