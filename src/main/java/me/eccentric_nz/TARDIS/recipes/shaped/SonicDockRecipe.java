package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:RGR,GSG,BGB
easy_ingredients.R:REDSTONE
easy_ingredients.G:GOLD_NUGGET
easy_ingredients.S:REPEATER
easy_ingredients.B:BLACKSTONE
hard_shape:RGR,GSG,BGB
hard_ingredients.R:REDSTONE
hard_ingredients.G:GOLD_INGOT
hard_ingredients.S:GLOWSTONE_DUST=Sonic Oscillator
hard_ingredients.B:BLACKSTONE
result:FLOWER_POT
amount:1
*/

public class SonicDockRecipe {

    private final TARDIS plugin;

    public SonicDockRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.FLOWER_POT, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Sonic Dock");
        im.setCustomModelData(1000);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "sonic_dock");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r.shape("RGR", "GSG", "BGB");
            r.setIngredient('R', Material.REDSTONE);
            r.setIngredient('G', Material.GOLD_INGOT);
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName("Sonic Oscillator");
            em.setCustomModelData(RecipeItem.SONIC_OSCILLATOR.getCustomModelData());
            exact.setItemMeta(em);
            r.setIngredient('S', new RecipeChoice.ExactChoice(exact));
            r.setIngredient('B', Material.BLACKSTONE);
        } else {
            r.shape("RGR", "GSG", "BGB");
            r.setIngredient('R', Material.REDSTONE);
            r.setIngredient('G', Material.GOLD_NUGGET);
            r.setIngredient('S', Material.REPEATER);
            r.setIngredient('B', Material.BLACKSTONE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Sonic Dock", r);
    }
}
