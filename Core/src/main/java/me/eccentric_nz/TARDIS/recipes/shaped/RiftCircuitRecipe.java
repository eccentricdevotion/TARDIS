package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:-D-,DND,-D-
easy_ingredients.D:DIAMOND
easy_ingredients.N:NETHER_STAR
hard_shape:-D-,DND,-D-
hard_ingredients.D:DIAMOND
hard_ingredients.N:NETHER_STAR
result:GLOWSTONE_DUST
amount:1
*/

public class RiftCircuitRecipe {

    private final TARDIS plugin;

    public RiftCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Rift Circuit");
        im.setCustomModelData(10001983);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "rift_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" D ", "DND", " D ");
        r.setIngredient('D', Material.DIAMOND);
        r.setIngredient('N', Material.NETHER_STAR);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Rift Circuit", r);
    }
}
