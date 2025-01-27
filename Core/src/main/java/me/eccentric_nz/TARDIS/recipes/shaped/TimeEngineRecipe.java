package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:LRL,GAG,GRG
easy_ingredients.L:LIGHT_BLUE_DYE
easy_ingredients.R:REDSTONE
easy_ingredients.A:ANVIL
easy_ingredients.G:GLASS_PANE
hard_shape:LRL,GAG,GRG
hard_ingredients.L:LIGHT_BLUE_DYE
hard_ingredients.R:REDSTONE_BLOCK
hard_ingredients.A:ANVIL
hard_ingredients.G:GLASS_PANE
result:LIGHT_GRAY_DYE
amount:1
*/

public class TimeEngineRecipe {

    private final TARDIS plugin;

    public TimeEngineRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LIGHT_GRAY_DYE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Time Engine");
        im.setItemModel(RecipeItem.TIME_ENGINE.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "time_engine");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("LRL", "GAG", "GRG");
        r.setIngredient('L', Material.LIGHT_BLUE_DYE);
        r.setIngredient('A', Material.ANVIL);
        r.setIngredient('G', Material.GLASS_PANE);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.setIngredient('R', Material.REDSTONE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Time Engine", r);
    }
}
