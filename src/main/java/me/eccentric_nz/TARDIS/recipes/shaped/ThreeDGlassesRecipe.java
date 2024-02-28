package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:---,P-P,CPM
easy_ingredients.P:PAPER
easy_ingredients.C:CYAN_STAINED_GLASS_PANE
easy_ingredients.M:MAGENTA_STAINED_GLASS_PANE
hard_shape:R-T,P-P,CPM
hard_ingredients.R:COMPARATOR
hard_ingredients.T:REDSTONE_TORCH
hard_ingredients.P:PAPER
hard_ingredients.C:CYAN_STAINED_GLASS_PANE
hard_ingredients.M:MAGENTA_STAINED_GLASS_PANE
result:LEATHER_HELMET
amount:1
*/

public class ThreeDGlassesRecipe {

    private final TARDIS plugin;

    public ThreeDGlassesRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("3-D Glasses");
        im.setCustomModelData(10000039);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "3-d_glasses");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r.shape("R T", "P P", "CPM");
            r.setIngredient('R', Material.COMPARATOR);
            r.setIngredient('T', Material.REDSTONE_TORCH);
            r.setIngredient('P', Material.PAPER);
            r.setIngredient('C', Material.CYAN_STAINED_GLASS_PANE);
            r.setIngredient('M', Material.MAGENTA_STAINED_GLASS_PANE);            
        } else {
            r.shape("   ", "P P", "CPM");
            r.setIngredient('P', Material.PAPER);
            r.setIngredient('C', Material.CYAN_STAINED_GLASS_PANE);
            r.setIngredient('M', Material.MAGENTA_STAINED_GLASS_PANE);            
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("3-D Glasses", r);
    }
}
