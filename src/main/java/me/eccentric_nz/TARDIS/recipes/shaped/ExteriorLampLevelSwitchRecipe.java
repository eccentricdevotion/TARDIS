package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape: ---,LBM,CCC
easy_ingredients.L: LEVER
easy_ingredients.B: BAMBOO_BUTTON
easy_ingredients.M: MANGROVE_BUTTON
easy_ingredients.C: COPPER_INGOT
hard_shape: ---,LBM,CCC
hard_ingredients.L: LEVER
hard_ingredients.B: BAMBOO_BUTTON
hard_ingredients.M: MANGROVE_BUTTON
hard_ingredients.C: COPPER_BLOCK
result: LEVER
amount: 1
 */
public class ExteriorLampLevelSwitchRecipe {

    private final TARDIS plugin;

    public ExteriorLampLevelSwitchRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        // add exterior recipe
        ItemStack is = new ItemStack(Material.LEVER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Exterior Lamp Level Switch");
        im.setCustomModelData(RecipeItem.EXTERIOR_LAMP_LEVEL_SWITCH.getCustomModelData());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "exterior_lamp_level_switch");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("   ", "LBM", "CCC");
        r.setIngredient('L', Material.LEVER);
        r.setIngredient('B', Material.BAMBOO_BUTTON);
        r.setIngredient('M', Material.MANGROVE_BUTTON);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('C', Material.COPPER_BLOCK);
        } else {
            r.setIngredient('C', Material.COPPER_INGOT);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Exterior Lamp Level Switch", r);
    }
}
