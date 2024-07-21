package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:RQR,R-R,RQR
easy_ingredients.Q:QUARTZ
easy_ingredients.R:REDSTONE
hard_shape:RQR,RBR,RQR
hard_ingredients.Q:QUARTZ
hard_ingredients.R:REDSTONE
hard_ingredients.B:STONE_BUTTON
result:GLOWSTONE_DUST
amount:1
*/

public class SonicOscillatorRecipe {

    private final TARDIS plugin;

    public SonicOscillatorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Sonic Oscillator");
        im.setCustomModelData(10001967);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "sonic_oscillator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("RQR", "RBR", "RQR");
            r.setIngredient('B', Material.STONE_BUTTON);
        } else {
            r.shape("RQR", "R R", "RQR");
        }
        r.setIngredient('Q', Material.QUARTZ);
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Sonic Oscillator", r);
    }
}
