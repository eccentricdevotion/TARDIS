package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:N--,IIH,--I
easy_ingredients.N:NOTE_BLOCK
easy_ingredients.I:IRON_INGOT
easy_ingredients.H:HOPPER
hard_shape:N--,IIH,--D
hard_ingredients.N:NOTE_BLOCK
hard_ingredients.I:IRON_INGOT
hard_ingredients.H:HOPPER
hard_ingredients.D:DIAMOND
result:LEATHER_HELMET
amount:1
*/

public class TARDISCommunicatorRecipe {

    private final TARDIS plugin;

    public TARDISCommunicatorRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.LEATHER_HELMET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Communicator");
        im.setCustomModelData(10000040);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_communicator");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("N  ", "IIH", "  D");
            r.setIngredient('N', Material.NOTE_BLOCK);
            r.setIngredient('I', Material.IRON_INGOT);
            r.setIngredient('H', Material.HOPPER);
            r.setIngredient('D', Material.DIAMOND);
        } else {
            r.shape("N  ", "IIH", "  I");
            r.setIngredient('N', Material.NOTE_BLOCK);
            r.setIngredient('I', Material.IRON_INGOT);
            r.setIngredient('H', Material.HOPPER);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Communicator", r);
    }
}
