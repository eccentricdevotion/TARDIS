package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:MRM,RGR,MRM
easy_ingredients.M:MAP
easy_ingredients.G:GLASS_PANE
easy_ingredients.R:REDSTONE
hard_shape:MRM,RGR,MRM
hard_ingredients.M:MAP
hard_ingredients.G:TINTED_GLASS
hard_ingredients.R:REDSTONE_BLOCK
result:MAP
amount:1
*/

public class TARDISMonitorRecipe {

    private final TARDIS plugin;

    public TARDISMonitorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MAP, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Monitor");
        im.setCustomModelData(5);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_monitor");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("MRM", "RGR", "MRM");
            r.setIngredient('M', Material.MAP);
            r.setIngredient('G', Material.TINTED_GLASS);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.shape("MRM", "RGR", "MRM");
            r.setIngredient('M', Material.MAP);
            r.setIngredient('G', Material.GLASS_PANE);
            r.setIngredient('R', Material.REDSTONE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Monitor", r);
    }
}
