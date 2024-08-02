package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:QQQ,Q-Q,QQQ
easy_ingredients.Q:QUARTZ
hard_shape:QQQ,Q-Q,QQQ
hard_ingredients.Q:QUARTZ
result:MUSIC_DISC_STRAD
amount:1
*/

public class BlankStorageDiskRecipe {

    private final TARDIS plugin;

    public BlankStorageDiskRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MUSIC_DISC_STRAD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Blank Storage Disk");
        im.setCustomModelData(10000001);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "blank_storage_disk");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("QQQ", "Q Q", "QQQ");
        r.setIngredient('Q', Material.QUARTZ);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Blank Storage Disk", r);
    }
}
