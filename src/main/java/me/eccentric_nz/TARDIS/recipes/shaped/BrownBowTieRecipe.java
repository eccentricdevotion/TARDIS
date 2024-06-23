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
easy_shape:---,SWS,---
easy_ingredients.S:STRING
easy_ingredients.W:BROWN_WOOL
hard_shape:STS,L-L,WWW
hard_ingredients.S:STRING
hard_ingredients.T:GLOWSTONE_DUST
hard_ingredients.L:LEATHER
hard_ingredients.W:BROWN_WOOL
result:LEATHER_HELMET
amount:1
lore:Bow ties are cool!
*/

public class BrownBowTieRecipe {

    private final TARDIS plugin;

    public BrownBowTieRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Brown Bow Tie");
        im.setCustomModelData(10000035);
        im.setLore(List.of("Bow ties are cool!"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "brown_bow_tie");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("STS", "L L", "WWW");
            r.setIngredient('S', Material.STRING);
            r.setIngredient('T', Material.GLOWSTONE_DUST);
            r.setIngredient('L', Material.LEATHER);
            r.setIngredient('W', Material.BROWN_WOOL);
        } else {
            r.shape("SWS");
            r.setIngredient('S', Material.STRING);
            r.setIngredient('W', Material.BROWN_WOOL);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Brown Bow Tie", r);
    }
}
