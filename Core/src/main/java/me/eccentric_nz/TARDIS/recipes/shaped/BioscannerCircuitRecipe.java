package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-E-,BRB,-E-
easy_ingredients.R:REDSTONE
easy_ingredients.E:SPIDER_EYE
easy_ingredients.B:BONE
hard_shape:-E-,BRB,-E-
hard_ingredients.R:REPEATER
hard_ingredients.E:SPIDER_EYE
hard_ingredients.B:BONE
result:GLOWSTONE_DUST
amount:1
*/

public class BioscannerCircuitRecipe {

    private final TARDIS plugin;

    public BioscannerCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Bio-scanner Circuit");
        im.setCustomModelData(10001969);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "bio-scanner_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" E ", "BRB", " E ");
            r.setIngredient('R', Material.REPEATER);
        } else {
            r.shape(" E ", "BRB", " E ");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('E', Material.SPIDER_EYE);
        r.setIngredient('B', Material.BONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Bio-scanner Circuit", r);
    }
}
