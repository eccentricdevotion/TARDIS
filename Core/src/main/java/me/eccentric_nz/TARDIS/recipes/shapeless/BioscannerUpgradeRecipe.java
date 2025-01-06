package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
recipe:BLAZE_ROD,GLOWSTONE_DUST=Bio-scanner Circuit
result:BLAZE_ROD
amount:1
*/

public class BioscannerUpgradeRecipe {

    private final TARDIS plugin;

    public BioscannerUpgradeRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Bio-scanner Upgrade");
        im.setItemModel(RecipeItem.BIO_SCANNER_UPGRADE.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "bio-scanner_upgrade");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.BLAZE_ROD);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName(ChatColor.WHITE + "Bio-scanner Circuit");
        em.setItemModel(RecipeItem.BIO_SCANNER_CIRCUIT.getModel());
        exact.setItemMeta(em);
        r.addIngredient(new RecipeChoice.ExactChoice(exact));
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Bio-scanner Upgrade", r);
    }
}
