package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/*
easy_shape:-H-,YYY,BGB
easy_ingredients.H:LEATHER_HELMET
easy_ingredients.Y:YELLOW_DYE
easy_ingredients.B:BLACK_DYE
easy_ingredients.G:GLASS_PANE
hard_ingredients.H:LEATHER_HELMET:Dyed Black
hard_ingredients.Y:YELLOW_DYE
hard_ingredients.B:BLACK_DYE
hard_ingredients.G:GLASS_PANE
result:NETHER_BRICK
amount:1
*/

public class SpaceHelmetRecipe {

    private final TARDIS plugin;

    public SpaceHelmetRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLASS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Space Helmet");
        im.setCustomModelData(5);
        im.setMaxStackSize(1);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "space_helmet");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" H ", "YYY", "BGB");
        r.setIngredient('Y', Material.YELLOW_DYE);
        r.setIngredient('B', Material.BLACK_DYE);
        r.setIngredient('G', Material.GLASS_PANE);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            ItemStack exact = new ItemStack(Material.LEATHER_HELMET, 1);
            LeatherArmorMeta am = (LeatherArmorMeta) exact.getItemMeta();
            Color black = Color.fromARGB(-14869215); // [argb0xFF1D1D21] not BLACK!
            am.setColor(black);
            exact.setItemMeta(am);
            r.setIngredient('H', new RecipeChoice.ExactChoice(exact));
        } else {
            r.setIngredient('H', Material.LEATHER_HELMET);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Space Helmet", r);
    }
}
