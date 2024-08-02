package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:---,OFO,RRR
easy_ingredients.O:OBSIDIAN
easy_ingredients.F:FURNACE
easy_ingredients.R:REDSTONE
hard_shape:---,OFO,RRR
hard_ingredients.O:OBSIDIAN
hard_ingredients.F:FURNACE
hard_ingredients.R:REDSTONE
result:FURNACE
amount:1
*/

public class TARDISArtronFurnaceRecipe {

    private final TARDIS plugin;

    public TARDISArtronFurnaceRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.FURNACE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Artron Furnace");
        im.setCustomModelData(10000001);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_artron_furnace");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("OFO", "RRR");
        r.setIngredient('O', Material.OBSIDIAN);
        r.setIngredient('F', Material.FURNACE);
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Artron Furnace", r);
    }
}
