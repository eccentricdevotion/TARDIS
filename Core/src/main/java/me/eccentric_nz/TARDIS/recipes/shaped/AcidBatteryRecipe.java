package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-A-,ARA,-A-
easy_ingredients.A:WATER_BUCKET=Acid Bucket
easy_ingredients.R:REDSTONE
hard_shape:-A-,ARA,-A-
hard_ingredients.A:WATER_BUCKET=Acid Bucket
hard_ingredients.R:REDSTONE_BLOCK
result:NETHER_BRICK
amount:1
*/

public class AcidBatteryRecipe {

    private final TARDIS plugin;

    public AcidBatteryRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.NETHER_BRICK, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Acid Battery");
        im.setCustomModelData(10000001);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "acid_battery");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = new ItemStack(Material.WATER_BUCKET, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName("Acid Bucket");
        em.setCustomModelData(RecipeItem.RUST_BUCKET.getCustomModelData());
        exact.setItemMeta(em);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" A ", "ARA", " A ");
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.shape(" A ", "ARA", " A ");
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('A', new RecipeChoice.ExactChoice(exact));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Acid Battery", r);
    }
}
