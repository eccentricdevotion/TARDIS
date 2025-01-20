package me.eccentric_nz.TARDIS.recipes.shapeless;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
recipe:BLAZE_ROD,GLOWSTONE_DUST=Redstone Activator Circuit
result:BLAZE_ROD
amount:1
*/

public class RedstoneUpgradeRecipe {

    private final TARDIS plugin;

    public RedstoneUpgradeRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(NamedTextColor.WHITE + "Redstone Upgrade");
        im.setItemModel(RecipeItem.REDSTONE_UPGRADE.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "redstone_upgrade");
        ShapelessRecipe r = new ShapelessRecipe(key, is);
        r.addIngredient(Material.BLAZE_ROD);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName(NamedTextColor.WHITE + "Redstone Activator Circuit");
        em.setItemModel(RecipeItem.REDSTONE_ACTIVATOR_CIRCUIT.getModel());
        exact.setItemMeta(em);
        r.addIngredient(new RecipeChoice.ExactChoice(exact));
        plugin.getServer().addRecipe(r);
        plugin.getIncomposita().getShapelessRecipes().put("Redstone Upgrade", r);
    }
}
