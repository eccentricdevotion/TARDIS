package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
easy_shape:QRQ,RLR,QRQ
easy_ingredients.Q:QUARTZ
easy_ingredients.R:REDSTONE
easy_ingredients.L:GLASS_PANE
hard_shape:QRQ,RGR,QRQ
hard_ingredients.Q:QUARTZ
hard_ingredients.R:REDSTONE
hard_ingredients.G:GOLDEN_HELMET
result:MUSIC_DISC_FAR
amount:1
*/

public class AuthorisedControlDiskRecipe {

    private final TARDIS plugin;

    public AuthorisedControlDiskRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.MUSIC_DISC_FAR, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Authorised Control Disk");
        im.setCustomModelData(10000001);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "authorised_control_disk");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("QRQ", "RGR", "QRQ");
            r.setIngredient('G', Material.GOLDEN_HELMET);
        } else {
            r.shape("QRQ", "RLR", "QRQ");
            r.setIngredient('L', Material.GLASS_PANE);
        }
        r.setIngredient('Q', Material.QUARTZ);
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Authorised Control Disk", r);
    }
}
