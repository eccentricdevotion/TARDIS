package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-D-,LEL,-D-
easy_ingredients.E:EMERALD
easy_ingredients.D:DIRT
easy_ingredients.L:OAK_LEAVES
hard_shape:-S-,LEL,-S-
hard_ingredients.E:EMERALD
hard_ingredients.L:OAK_LEAVES
hard_ingredients.S:STONE
result:GLOWSTONE_DUST
amount:1
*/

public class EmeraldEnvironmentCircuitRecipe {

    private final TARDIS plugin;

    public EmeraldEnvironmentCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Emerald Environment Circuit");
        im.setCustomModelData(10001972);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "emerald_environment_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" S ", "LEL", " S ");
            r.setIngredient('S', Material.STONE);
        } else {
            r.shape(" D ", "LEL", " D ");
            r.setIngredient('D', Material.DIRT);
        }
        r.setIngredient('E', Material.EMERALD);
        r.setIngredient('L', Material.OAK_LEAVES);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Emerald Environment Circuit", r);
    }
}
