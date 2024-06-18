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
easy_ingredients.W:GRAY_WOOL
hard_shape:STS,L-L,WWW
hard_ingredients.S:STRING
hard_ingredients.T:GLOWSTONE_DUST
hard_ingredients.L:LEATHER
hard_ingredients.W:GRAY_WOOL
result:LEATHER_HELMET
amount:1
lore:Bow ties are cool!
*/

public class GreyBowTieRecipe {

    private final TARDIS plugin;

    public GreyBowTieRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Grey Bow Tie");
        im.setCustomModelData(10000030);
        im.setLore(List.of("Bow ties are cool!"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "grey_bow_tie");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("STS", "L L", "WWW");
            r.setIngredient('S', Material.STRING);
            r.setIngredient('T', Material.GLOWSTONE_DUST);
            r.setIngredient('L', Material.LEATHER);
            r.setIngredient('W', Material.GRAY_WOOL);
        } else {
            r.shape("   ", "SWS", "   ");
            r.setIngredient('S', Material.STRING);
            r.setIngredient('W', Material.GRAY_WOOL);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Grey Bow Tie", r);
    }
}
