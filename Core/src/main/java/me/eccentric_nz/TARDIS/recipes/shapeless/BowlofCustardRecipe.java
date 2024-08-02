package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
recipe:BOWL,MILK_BUCKET,EGG
result:MUSHROOM_STEW
amount:1
*/

public class BowlofCustardRecipe {

    private final TARDIS plugin;

    public BowlofCustardRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MUSHROOM_STEW, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Bowl of Custard");
        im.setCustomModelData(10000001);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "bowl_of_custard");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.BOWL);
        r.addIngredient(Material.MILK_BUCKET);
        r.addIngredient(Material.EGG);
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Bowl of Custard", r);
    }
}
