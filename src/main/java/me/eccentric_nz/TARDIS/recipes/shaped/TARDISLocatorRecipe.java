package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:OIO,ICI,OIO
easy_ingredients.O:GRAVEL
easy_ingredients.I:IRON_INGOT
easy_ingredients.C:RED_WOOL
hard_shape:OIO,ICI,OIO
hard_ingredients.O:OBSIDIAN
hard_ingredients.I:IRON_INGOT
hard_ingredients.C:GLOWSTONE_DUST=TARDIS Locator Circuit
result:COMPASS
amount:1
*/

public class TARDISLocatorRecipe {

    private final TARDIS plugin;

    public TARDISLocatorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Locator");
        im.setCustomModelData(10000001);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_locator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getDifficulty() == Difficulty.HARD) {
            r.shape("OIO", "ICI", "OIO");
            r.setIngredient('O', Material.OBSIDIAN);
            r.setIngredient('I', Material.IRON_INGOT);
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName("TARDIS Locator Circuit");
            em.setCustomModelData(RecipeItem.TARDIS_LOCATOR_CIRCUIT.getCustomModelData());
            exact.setItemMeta(em);
            r.setIngredient('C', new RecipeChoice.ExactChoice(exact));
        } else {
            r.shape("OIO", "ICI", "OIO");
            r.setIngredient('O', Material.GRAVEL);
            r.setIngredient('I', Material.IRON_INGOT);
            r.setIngredient('C', Material.RED_WOOL);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Locator", r);
    }
}
