package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/*
easy_shape:---,SWS,---
easy_ingredients.S:STRING
easy_ingredients.W:BLACK_WOOL
hard_shape:STS,L-L,WWW
hard_ingredients.S:STRING
hard_ingredients.T:GLOWSTONE_DUST
hard_ingredients.L:LEATHER
hard_ingredients.W:BLACK_WOOL
result:LEATHER_HELMET
amount:1
lore:Bow ties are cool!
*/

public class BlackBowTieRecipe {

    private final TARDIS plugin;

    public BlackBowTieRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Black Bow Tie");
        im.setCustomModelData(10000038);
        String lore = plugin.getRecipesConfig().getString("shaped.Black Bow Tie.lore");
        if (!lore.isEmpty()) {
            im.setLore(Arrays.asList(lore.split("~")));
        }
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "black_bow_tie");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r.shape("STS", "L L", "WWW");
            r.setIngredient('S', Material.STRING);
            r.setIngredient('T', Material.GLOWSTONE_DUST);
            r.setIngredient('L', Material.LEATHER);
            r.setIngredient('W', Material.BLACK_WOOL);            
        } else {
            r.shape("   ", "SWS", "   ");
            r.setIngredient('S', Material.STRING);
            r.setIngredient('W', Material.BLACK_WOOL);            
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Black Bow Tie", r);
    }
}
