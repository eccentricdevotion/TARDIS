package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-R-,DSD,-R-
easy_ingredients.R:REDSTONE
easy_ingredients.D:DIAMOND
easy_ingredients.S:SHEARS
hard_shape:-O-,DSD,-O-
hard_ingredients.O:OBSIDIAN
hard_ingredients.D:DIAMOND
hard_ingredients.S:SHEARS
result:GLOWSTONE_DUST
amount:1
*/

public class DiamondDisruptorCircuitRecipe {

    private final TARDIS plugin;

    public DiamondDisruptorCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Diamond Disruptor Circuit");
        im.setCustomModelData(10001971);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "diamond_disruptor_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" O ", "DSD", " O ");
            r.setIngredient('O', Material.OBSIDIAN);
        } else {
            r.shape(" R ", "DSD", " R ");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('D', Material.DIAMOND);
        r.setIngredient('S', Material.SHEARS);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Diamond Disruptor Circuit", r);
    }
}
