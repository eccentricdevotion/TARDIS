package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class InteriorLightLevelSwitchRecipe {

    private final TARDIS plugin;

    public InteriorLightLevelSwitchRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
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
    }
}
