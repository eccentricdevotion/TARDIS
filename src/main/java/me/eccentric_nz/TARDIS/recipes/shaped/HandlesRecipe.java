package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:III,ISI,IRI
easy_ingredients.I:IRON_INGOT
easy_ingredients.S:SKELETON_SKULL
easy_ingredients.R:REDSTONE
hard_shape:IDI,ISI,IRI
hard_ingredients.I:IRON_INGOT
hard_ingredients.D:DIAMOND
hard_ingredients.S:SKELETON_SKULL
hard_ingredients.R:REDSTONE
result:BIRCH_BUTTON
amount:1
lore:Cyberhead from the~Maldovar Market
*/

public class HandlesRecipe {

    private final TARDIS plugin;

    public HandlesRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BIRCH_BUTTON, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Handles");
        im.setCustomModelData(10000001);
        im.setLore(List.of("Cyberhead from the", "Maldovar Market"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "handles");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r.shape("IDI", "ISI", "IRI");
            r.setIngredient('I', Material.IRON_INGOT);
            r.setIngredient('D', Material.DIAMOND);
            r.setIngredient('S', Material.SKELETON_SKULL);
            r.setIngredient('R', Material.REDSTONE);
        } else {
            r.shape("III", "ISI", "IRI");
            r.setIngredient('I', Material.IRON_INGOT);
            r.setIngredient('S', Material.SKELETON_SKULL);
            r.setIngredient('R', Material.REDSTONE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Handles", r);
    }
}
