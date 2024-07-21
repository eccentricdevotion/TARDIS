package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:CRC,GWG,GRG
easy_ingredients.C:<configured material>
easy_ingredients.R:REDSTONE
easy_ingredients.W:CLOCK
easy_ingredients.G:GLASS_PANE
hard_shape:CRC,GWG,GRG
hard_ingredients.C:<configured material>
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.W:CLOCK
hard_ingredients.G:GLASS_PANE
result:LIGHT_GRAY_DYE
amount:1
*/

public class TimeRotorCustomRecipe {

    private final TARDIS plugin;

    public TimeRotorCustomRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipes() {
        for (String r : plugin.getCustomRotorsConfig().getKeys(false)) {
            try {
                Material material = Material.valueOf(plugin.getCustomRotorsConfig().getString(r + ".animated_material"));
                ItemStack is = new ItemStack(Material.LIGHT_GRAY_DYE, 1);
                ItemMeta im = is.getItemMeta();
                String dn = TARDISStringUtils.capitalise(r);
                im.setDisplayName("Time Rotor " + dn);
                im.setCustomModelData(plugin.getCustomRotorsConfig().getInt(r + ".off_custom_model_data"));
                is.setItemMeta(im);
                NamespacedKey key = new NamespacedKey(plugin, "time_rotor_" + r);
                ShapedRecipe recipe = new ShapedRecipe(key, is);
                recipe.shape("CRC", "GWG", "GRG");
                recipe.setIngredient('C', material);
                recipe.setIngredient('W', Material.CLOCK);
                recipe.setIngredient('G', Material.GLASS_PANE);
                if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
                    recipe.setIngredient('R', Material.REDSTONE_BLOCK);
                } else {
                    recipe.setIngredient('R', Material.REDSTONE);
                }
                plugin.getServer().addRecipe(recipe);
                plugin.getFigura().getShapedRecipes().put("Time Rotor " + dn, recipe);
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid custom rotor item material for " + r + "!");
            }
        }
    }
}
