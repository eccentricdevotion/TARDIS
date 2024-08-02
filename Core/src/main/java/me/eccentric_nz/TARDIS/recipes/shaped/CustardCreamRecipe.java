package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:---,WYW,---
easy_ingredients.W:WHEAT
easy_ingredients.Y:YELLOW_DYE
hard_shape:---,WYW,---
hard_ingredients.W:WHEAT
hard_ingredients.Y:YELLOW_DYE
result:COOKIE
amount:8
*/

public class CustardCreamRecipe {

    private final TARDIS plugin;

    public CustardCreamRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.COOKIE, 8);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Custard Cream");
        im.setCustomModelData(10000002);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "custard_cream");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("WYW");
        r.setIngredient('W', Material.WHEAT);
        r.setIngredient('Y', Material.YELLOW_DYE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Custard Cream", r);
    }
}
