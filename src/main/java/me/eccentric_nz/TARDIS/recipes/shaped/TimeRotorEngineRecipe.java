package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:BRB,GWG,GRG
easy_ingredients.B:BLUE_DYE
easy_ingredients.R:REDSTONE
easy_ingredients.W:CLOCK
easy_ingredients.G:GLASS_PANE
hard_shape:BRB,GWG,GRG
hard_ingredients.B:BLUE_DYE
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.W:CLOCK
hard_ingredients.G:GLASS_PANE
result:LIGHT_GRAY_DYE
amount:1
*/

public class TimeRotorEngineRecipe {

    private final TARDIS plugin;

    public TimeRotorEngineRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LIGHT_GRAY_DYE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Time Rotor Engine");
        im.setCustomModelData(10000008);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "time_rotor_engine");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("BRB", "GWG", "GRG");
            r.setIngredient('B', Material.BLUE_DYE);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
            r.setIngredient('W', Material.CLOCK);
            r.setIngredient('G', Material.GLASS_PANE);
        } else {
            r.shape("BRB", "GWG", "GRG");
            r.setIngredient('B', Material.BLUE_DYE);
            r.setIngredient('R', Material.REDSTONE);
            r.setIngredient('W', Material.CLOCK);
            r.setIngredient('G', Material.GLASS_PANE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Time Rotor Engine", r);
    }
}
