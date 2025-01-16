package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:S-S,-S-,RGC
easy_ingredients.S:STRING
easy_ingredients.R:REDSTONE
easy_ingredients.G:GOLD_NUGGET
easy_ingredients.C:COMPARATOR
hard_shape:S-S,-S-,RGC
hard_ingredients.S:STRING
hard_ingredients.R:REDSTONE
hard_ingredients.G:GOLD_NUGGET
hard_ingredients.C:GLOWSTONE_DUST=Perception Circuit
result:GOLD_NUGGET
amount:1
*/

public class PerceptionFilterRecipe {

    private final TARDIS plugin;

    public PerceptionFilterRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Perception Filter");
        im.setItemModel(RecipeItem.PERCEPTION_FILTER.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "perception_filter");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("S S", " S ", "RGC");
        r.setIngredient('S', Material.STRING);
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('G', Material.GOLD_NUGGET);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName(ChatColor.WHITE + "Perception Circuit");
            em.setItemModel(RecipeItem.PERCEPTION_CIRCUIT.getModel());
            exact.setItemMeta(em);
            r.setIngredient('C', new RecipeChoice.ExactChoice(exact));
        } else {
            r.setIngredient('C', Material.COMPARATOR);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Perception Filter", r);
    }
}
