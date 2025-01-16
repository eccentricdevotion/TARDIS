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
easy_shape:---,PLP,-P-
easy_ingredients.P:PAPER
easy_ingredients.L:LAPIS_BLOCK
hard_shape:-LC,PSP,-P-
hard_ingredients.L:LAPIS_BLOCK
hard_ingredients.C:COMPARATOR
hard_ingredients.P:PAPER
hard_ingredients.S:SHULKER_SHELL
result:PAPER
amount:1
lore:Smaller on the outside
*/

public class PaperBagRecipe {

    private final TARDIS plugin;

    public PaperBagRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Paper Bag");
        im.setItemModel(RecipeItem.PAPER_BAG.getModel());
        im.setLore(List.of("Smaller on the outside"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "paper_bag");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" LC", "PSP", " P ");
            r.setIngredient('C', Material.COMPARATOR);
            r.setIngredient('S', Material.SHULKER_SHELL);
        } else {
            r.shape("PLP", " P ");
        }
        r.setIngredient('P', Material.PAPER);
        r.setIngredient('L', Material.LAPIS_BLOCK);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Paper Bag", r);
    }
}
