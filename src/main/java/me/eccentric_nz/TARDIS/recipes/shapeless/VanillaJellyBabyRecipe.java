package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
recipe:SUGAR,SLIME_BALL,WHITE_DYE
result:MELON_SLICE
amount:4
*/

public class VanillaJellyBabyRecipe {

    private final TARDIS plugin;

    public VanillaJellyBabyRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MELON_SLICE, 4);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Vanilla Jelly Baby");
        im.setCustomModelData(10000001);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "vanilla_jelly_baby");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.SUGAR);
        r.addIngredient(Material.SLIME_BALL);
        r.addIngredient(Material.WHITE_DYE);
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Vanilla Jelly Baby", r);
    }
}
