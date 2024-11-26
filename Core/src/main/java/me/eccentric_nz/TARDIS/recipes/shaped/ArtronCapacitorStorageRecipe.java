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
import org.bukkit.persistence.PersistentDataType;

/*
easy_shape:RRR,EDE,CCC
easy_ingredients.R:REPEATER
easy_ingredients.E:BUCKET:Artron Capacitor
easy_ingredients.D:DIAMOND
easy_ingredients.C:COPPER_INGOT
hard_shape:RRR,EDE,CCC
hard_ingredients.R:COMPARATOR
hard_ingredients.E:BUCKET:Artron Capacitor
hard_ingredients.D:DIAMOND_BLOCK
hard_ingredients.C:COPPER_BLOCK
result:GRAY_SHULKER_BOX
amount:1
*/

public class ArtronCapacitorStorageRecipe {

    private final TARDIS plugin;

    public ArtronCapacitorStorageRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GRAY_SHULKER_BOX, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Artron Capacitor Storage");
        im.setItemModel(RecipeItem.ARTRON_CAPACITOR_STORAGE.getModel());
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1);
        is.setItemMeta(im);
        // exact choice
        ItemStack capac = new ItemStack(Material.BUCKET, 1);
        ItemMeta itor = capac.getItemMeta();
        itor.setDisplayName(ChatColor.WHITE + "Artron Capacitor");
        itor.setItemModel(RecipeItem.ARTRON_CAPACITOR.getModel());
        capac.setItemMeta(itor);
        NamespacedKey key = new NamespacedKey(plugin, "artron_capacitor_storage");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("RRR", "EDE", "CCC");
        r.setIngredient('E', new RecipeChoice.ExactChoice(capac));
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('R', Material.COMPARATOR);
            r.setIngredient('D', Material.DIAMOND_BLOCK);
            r.setIngredient('C', Material.COPPER_BLOCK);
        } else {
            r.setIngredient('R', Material.REPEATER);
            r.setIngredient('D', Material.DIAMOND);
            r.setIngredient('C', Material.COPPER_INGOT);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Artron Capacitor Storage", r);
    }
}
