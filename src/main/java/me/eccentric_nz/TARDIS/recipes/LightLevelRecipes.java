package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class LightLevelRecipes {

    private final TARDIS plugin;

    public LightLevelRecipes(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipes() {
        // add interior recipe
        ItemStack is = new ItemStack(Material.LEVER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Interior Light Level Switch");
        im.setCustomModelData(3000);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "interior_light_level_switch");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("   ", "WLA", "CCC");
        r.setIngredient('W', Material.WARPED_BUTTON);
        r.setIngredient('L', Material.LEVER);
        r.setIngredient('A', Material.ACACIA_BUTTON);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r.setIngredient('C', Material.COPPER_BLOCK);
        } else {
            r.setIngredient('C', Material.COPPER_INGOT);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Interior Light Level Switch", r);
        // add exterior recipe
        ItemStack is_ex = new ItemStack(Material.LEVER, 1);
        ItemMeta im_ex = is_ex.getItemMeta();
        im_ex.setDisplayName("Exterior Light Level Switch");
        im_ex.setCustomModelData(1000);
        is_ex.setItemMeta(im_ex);
        NamespacedKey key_ex = new NamespacedKey(plugin, "exterior_light_level_switch");
        ShapedRecipe r_ex = new ShapedRecipe(key_ex, is_ex);
        r_ex.shape("   ", "LBM", "CCC");
        r_ex.setIngredient('L', Material.LEVER);
        r_ex.setIngredient('B', Material.BAMBOO_BUTTON);
        r_ex.setIngredient('M', Material.MANGROVE_BUTTON);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r_ex.setIngredient('C', Material.COPPER_BLOCK);
        } else {
            r_ex.setIngredient('C', Material.COPPER_INGOT);
        }
        plugin.getServer().addRecipe(r_ex);
        plugin.getFigura().getShapedRecipes().put("Exterior Light Level Switch", r_ex);
    }
}
