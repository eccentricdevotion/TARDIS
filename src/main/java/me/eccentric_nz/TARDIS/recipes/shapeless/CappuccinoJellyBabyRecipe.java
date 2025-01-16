package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
recipe:SUGAR,SLIME_BALL,BROWN_DYE
result:MELON_SLICE
amount:4
*/

public class CappuccinoJellyBabyRecipe {

    private final TARDIS plugin;

    public CappuccinoJellyBabyRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MELON_SLICE, 4);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Cappuccino Jelly Baby");
        im.setItemModel(RecipeItem.CAPPUCCINO_JELLY_BABY.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "cappuccino_jelly_baby");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.SUGAR);
        r.addIngredient(Material.SLIME_BALL);
        r.addIngredient(Material.BROWN_DYE);
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Cappuccino Jelly Baby", r);
    }
}
