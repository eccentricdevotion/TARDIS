package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:---,WRW,---
easy_ingredients.W:WHEAT
easy_ingredients.R:SWEET_BERRIES
hard_shape:---,WRW,---
hard_ingredients.W:WHEAT
hard_ingredients.R:SWEET_BERRIES
result:COOKIE
amount:8
*/

public class JammyDodgerRecipe {

    private final TARDIS plugin;

    public JammyDodgerRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.COOKIE, 8);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Jammy Dodger");
        im.setCustomModelData(10000001);
        im.setLore(List.of("Best eaten with custard!"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "jammy_dodger");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("   ", "WRW", "   ");
            r.setIngredient('W', Material.WHEAT);
            r.setIngredient('R', Material.SWEET_BERRIES);
        } else {
            r.shape("   ", "WRW", "   ");
            r.setIngredient('W', Material.WHEAT);
            r.setIngredient('R', Material.SWEET_BERRIES);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Jammy Dodger", r);
    }
}
