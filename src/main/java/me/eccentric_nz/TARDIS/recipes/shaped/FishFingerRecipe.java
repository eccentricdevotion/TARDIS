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
easy_shape:-B-,-F-,-B-
easy_ingredients.B:BREAD
easy_ingredients.F:COD
hard_shape:-B-,-F-,-B-
hard_ingredients.B:BREAD
hard_ingredients.F:COD
result:COOKED_COD
amount:3
lore:Best eaten with custard!
*/

public class FishFingerRecipe {

    private final TARDIS plugin;

    public FishFingerRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.COOKED_COD, 3);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Fish Finger");
        im.setCustomModelData(10000001);
        im.setLore(List.of("Best eaten with custard!"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "fish_finger");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r.shape(" B ", " F ", " B ");
            r.setIngredient('B', Material.BREAD);
            r.setIngredient('F', Material.COD);            
        } else {
            r.shape(" B ", " F ", " B ");
            r.setIngredient('B', Material.BREAD);
            r.setIngredient('F', Material.COD);            
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Fish Finger", r);
    }
}
