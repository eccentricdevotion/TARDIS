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

/*
easy_shape:-N-,NFN,-N-
easy_ingredients.N:NETHERRACK
easy_ingredients.F:FLINT_AND_STEEL
hard_shape:LN-,NFN,-NL
hard_ingredients.N:NETHERRACK
hard_ingredients.F:FLINT_AND_STEEL
hard_ingredients.L:LAVA_BUCKET
result:GLOWSTONE_DUST
amount:1
*/

public class IgniteCircuitRecipe {

    private final TARDIS plugin;

    public IgniteCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Ignite Circuit");
        im.setItemModel(RecipeItem.IGNITE_CIRCUIT.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "ignite_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("LN ", "NFN", " NL");
            r.setIngredient('L', Material.LAVA_BUCKET);
        } else {
            r.shape(" N ", "NFN", " N ");
        }
        r.setIngredient('N', Material.NETHERRACK);
        r.setIngredient('F', Material.FLINT_AND_STEEL);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Ignite Circuit", r);
    }
}
