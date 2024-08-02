package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-I-,DGD,-I-
easy_ingredients.I:BLACK_DYE
easy_ingredients.D:PURPLE_DYE
easy_ingredients.G:GOLD_NUGGET
hard_shape:-I-,DGD,-I-
hard_ingredients.I:BLACK_DYE
hard_ingredients.D:PURPLE_DYE
hard_ingredients.G:GOLD_BLOCK
result:GLOWSTONE_DUST
amount:1
*/

public class PainterCircuitRecipe {

    private final TARDIS plugin;

    public PainterCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Painter Circuit");
        im.setCustomModelData(10001979);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "painter_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" I ", "DGD", " I ");
        r.setIngredient('I', Material.BLACK_DYE);
        r.setIngredient('D', Material.PURPLE_DYE);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('G', Material.GOLD_BLOCK);
        } else {
            r.setIngredient('G', Material.GOLD_NUGGET);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Painter Circuit", r);
    }
}
