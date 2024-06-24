package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-S-,-F-,GRG
easy_ingredients.G:GOLD_NUGGET
easy_ingredients.R:REDSTONE
easy_ingredients.F:FLOWER_POT
easy_ingredients.S:BLAZE_ROD
hard_shape:-S-,-F-,GRG
hard_ingredients.G:GOLD_INGOT
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.F:FLOWER_POT
hard_ingredients.S:BLAZE_ROD
result:FLOWER_POT
amount:1
*/

public class SonicGeneratorRecipe {

    private final TARDIS plugin;

    public SonicGeneratorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.FLOWER_POT, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Sonic Generator");
        im.setCustomModelData(10000001);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "sonic_generator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" S ", " F ", "GRG");
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('G', Material.GOLD_INGOT);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.setIngredient('G', Material.GOLD_NUGGET);
            r.setIngredient('R', Material.REDSTONE);
        }
        r.setIngredient('F', Material.FLOWER_POT);
        r.setIngredient('S', Material.BLAZE_ROD);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Sonic Generator", r);
    }
}
