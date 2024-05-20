package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:BBB,BGB,BRB
easy_ingredients.B:BLACKSTONE
easy_ingredients.G:GLASS_PANE
easy_ingredients.R:REDSTONE
hard_shape:BBB,BGB,BRB
hard_ingredients.B:BLACKSTONE
hard_ingredients.G:TINTED_GLASS
hard_ingredients.R:REDSTONE_BLOCK
result:GLASS
amount:1
lore:Place in an upwards~facing item frame
*/

public class MonitorFrameRecipe {

    private final TARDIS plugin;

    public MonitorFrameRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLASS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Monitor Frame");
        im.setCustomModelData(2);
        im.setLore(List.of("Place in an upwards", "facing item frame"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "monitor_frame");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("BBB", "BGB", "BRB");
            r.setIngredient('B', Material.BLACKSTONE);
            r.setIngredient('G', Material.TINTED_GLASS);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        } else {
            r.shape("BBB", "BGB", "BRB");
            r.setIngredient('B', Material.BLACKSTONE);
            r.setIngredient('G', Material.GLASS_PANE);
            r.setIngredient('R', Material.REDSTONE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Monitor Frame", r);
    }
}
