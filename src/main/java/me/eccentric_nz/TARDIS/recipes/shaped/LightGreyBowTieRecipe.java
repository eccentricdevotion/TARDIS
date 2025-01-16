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

import java.util.List;

/*
easy_shape:---,SWS,---
easy_ingredients.S:STRING
easy_ingredients.W:LIGHT_GRAY_WOOL
hard_shape:STS,L-L,WWW
hard_ingredients.S:STRING
hard_ingredients.T:GLOWSTONE_DUST
hard_ingredients.L:LEATHER
hard_ingredients.W:LIGHT_GRAY_WOOL
result:LEATHER_HELMET
amount:1
lore:Bow ties are cool!
*/

public class LightGreyBowTieRecipe {

    private final TARDIS plugin;

    public LightGreyBowTieRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Light Grey Bow Tie");
        im.setItemModel(RecipeItem.LIGHT_GREY_BOW_TIE.getModel());
        im.setLore(List.of("Bow ties are cool!"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "light_grey_bow_tie");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("STS", "L L", "WWW");
            r.setIngredient('T', Material.GLOWSTONE_DUST);
            r.setIngredient('L', Material.LEATHER);
        } else {
            r.shape("SWS");
        }
        r.setIngredient('S', Material.STRING);
        r.setIngredient('W', Material.LIGHT_GRAY_WOOL);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Light Grey Bow Tie", r);
    }
}
