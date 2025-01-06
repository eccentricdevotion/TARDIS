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
recipe:BLAZE_ROD,GLOWSTONE_DUST=Painter Circuit
result:BLAZE_ROD
amount:1
*/

public class PainterUpgradeRecipe {

    private final TARDIS plugin;

    public PainterUpgradeRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Painter Upgrade");
        im.setItemModel(RecipeItem.PAINTER_UPGRADE.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "painter_upgrade");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.BLAZE_ROD);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName(ChatColor.WHITE + "Painter Circuit");
        em.setItemModel(RecipeItem.PAINTER_CIRCUIT.getModel());
        exact.setItemMeta(em);
        r.addIngredient(new RecipeChoice.ExactChoice(exact));
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Painter Upgrade", r);
    }
}
