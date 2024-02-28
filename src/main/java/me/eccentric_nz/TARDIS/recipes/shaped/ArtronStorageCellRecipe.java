package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/*
easy_shape:-B-,-R-,-L-
easy_ingredients.B:BUCKET
easy_ingredients.R:REDSTONE
easy_ingredients.L:LIME_STAINED_GLASS
hard_shape:-B-,LRL,-L-
hard_ingredients.B:BUCKET
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.L:LIME_STAINED_GLASS
result:BUCKET
amount:1
lore:Charge Level~0
*/

public class ArtronStorageCellRecipe {

    private final TARDIS plugin;

    public ArtronStorageCellRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BUCKET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Artron Storage Cell");
        im.setCustomModelData(10000001);
        String lore = plugin.getRecipesConfig().getString("shaped.Artron Storage Cell.lore");
        if (!lore.isEmpty()) {
            im.setLore(Arrays.asList(lore.split("~")));
        }
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "artron_storage_cell");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r.shape(" B ", "LRL", " L ");
            r.setIngredient('B', Material.BUCKET);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
            r.setIngredient('L', Material.LIME_STAINED_GLASS);            
        } else {
            r.shape(" B ", " R ", " L ");
            r.setIngredient('B', Material.BUCKET);
            r.setIngredient('R', Material.REDSTONE);
            r.setIngredient('L', Material.LIME_STAINED_GLASS);            
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Artron Storage Cell", r);
    }
}
