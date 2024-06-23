package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:CRC,GWG,GRG
easy_ingredients.C:GREEN_DYE
easy_ingredients.R:REDSTONE
easy_ingredients.W:CLOCK
easy_ingredients.G:GLASS_PANE
hard_shape:CRC,GWG,GRG
hard_ingredients.C:GREEN_DYE
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.W:CLOCK
hard_ingredients.G:GLASS_PANE
result:LIGHT_GRAY_DYE
amount:1
*/

public class TimeRotorRusticRecipe {

    private final TARDIS plugin;

    public TimeRotorRusticRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LIGHT_GRAY_DYE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Time Rotor Rustic");
        im.setCustomModelData(10000101);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "time_rotor_rustic");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("CRC", "GWG", "GRG");
            r.setIngredient('C', Material.GREEN_DYE);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
            r.setIngredient('W', Material.CLOCK);
            r.setIngredient('G', Material.GLASS_PANE);
        } else {
            r.shape("CRC", "GWG", "GRG");
            r.setIngredient('C', Material.GREEN_DYE);
            r.setIngredient('R', Material.REDSTONE);
            r.setIngredient('W', Material.CLOCK);
            r.setIngredient('G', Material.GLASS_PANE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Time Rotor Rustic", r);
    }
}
