package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-K-,RSR,-R-
easy_ingredients.K:FEATHER
easy_ingredients.R:REDSTONE
easy_ingredients.S:COPPER_INGOT
hard_shape:-K-,RSR,-R-
hard_ingredients.K:BRUSH
hard_ingredients.R:REDSTONE
hard_ingredients.S:COPPER_BLOCK
result:GLOWSTONE_DUST
amount:1
*/

public class BrushCircuitRecipe {

    private final TARDIS plugin;

    public BrushCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Brush Circuit");
        im.setCustomModelData(10001987);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "brush_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" K ", "RSR", " R ");
            r.setIngredient('K', Material.BRUSH);
            r.setIngredient('S', Material.COPPER_BLOCK);
        } else {
            r.shape(" K ", "RSR", " R ");
            r.setIngredient('K', Material.FEATHER);
            r.setIngredient('S', Material.COPPER_INGOT);
        }
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Brush Circuit", r);
    }
}
