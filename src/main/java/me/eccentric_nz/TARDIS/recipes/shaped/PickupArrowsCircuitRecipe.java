package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-A-,ARA,-A-
easy_ingredients.A:ARROW
easy_ingredients.R:REDSTONE
hard_shape:-S-,SRS,-S-
hard_ingredients.S:SPECTRAL_ARROW
hard_ingredients.R:REDSTONE_BLOCK
result:GLOWSTONE_DUST
amount:1
*/

public class PickupArrowsCircuitRecipe {

    private final TARDIS plugin;

    public PickupArrowsCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Pickup Arrows Circuit");
        im.setCustomModelData(10001984);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "pickup_arrows_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" S ", "SRS", " S ");
            r.setIngredient('S', Material.SPECTRAL_ARROW);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.shape(" A ", "ARA", " A ");
            r.setIngredient('A', Material.ARROW);
            r.setIngredient('R', Material.REDSTONE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Pickup Arrows Circuit", r);
    }
}
