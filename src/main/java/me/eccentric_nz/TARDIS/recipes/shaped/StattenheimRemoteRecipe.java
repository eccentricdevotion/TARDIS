package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:OBO,-L-,RRR
easy_ingredients.O:OBSIDIAN
easy_ingredients.B:STONE_BUTTON
easy_ingredients.L:BLUE_DYE
easy_ingredients.R:REDSTONE
hard_shape:OBO,OLO,RRR
hard_ingredients.O:OBSIDIAN
hard_ingredients.B:STONE_BUTTON
hard_ingredients.L:GLOWSTONE_DUST=TARDIS Stattenheim Circuit
hard_ingredients.R:REDSTONE
result:FLINT
amount:1
lore:Right-click block~to call TARDIS
*/

public class StattenheimRemoteRecipe {

    private final TARDIS plugin;

    public StattenheimRemoteRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.FLINT, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Stattenheim Remote");
        im.setCustomModelData(10000001);
        im.setLore(List.of("Right-click block", "to call TARDIS"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "stattenheim_remote");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("OBO", "OLO", "RRR");
            r.setIngredient('O', Material.OBSIDIAN);
            r.setIngredient('B', Material.STONE_BUTTON);
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName("TARDIS Stattenheim Circuit");
            em.setCustomModelData(RecipeItem.TARDIS_STATTENHEIM_CIRCUIT.getCustomModelData());
            exact.setItemMeta(em);
            r.setIngredient('L', new RecipeChoice.ExactChoice(exact));
            r.setIngredient('R', Material.REDSTONE);
        } else {
            r.shape("OBO", " L ", "RRR");
            r.setIngredient('O', Material.OBSIDIAN);
            r.setIngredient('B', Material.STONE_BUTTON);
            r.setIngredient('L', Material.BLUE_DYE);
            r.setIngredient('R', Material.REDSTONE);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Stattenheim Remote", r);
    }
}
