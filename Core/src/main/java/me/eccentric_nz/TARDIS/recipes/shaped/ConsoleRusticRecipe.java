package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/*
easy_shape:CBC,LWL,CBC
easy_ingredients.C:CONCRETE_POWDER
easy_ingredients.B:BAMBOO_BUTTON
easy_ingredients.L:LEVER
easy_ingredients.W:REDSTONE
hard_shape:CBC,ORO,CBC
hard_ingredients.C:CONCRETE_POWDER
hard_ingredients.B:BAMBOO_BUTTON
hard_ingredients.O:COMPARATOR
hard_ingredients.R:REDSTONE_BLOCK
result:GLOWSTONE_DUST
amount:1
*/

public class ConsoleRusticRecipe {

    private final TARDIS plugin;

    public ConsoleRusticRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.WAXED_OXIDIZED_COPPER, 1);
        ItemMeta im = is.getItemMeta();
        String dn = ChatColor.WHITE + "Rustic Console";
        im.setDisplayName(dn);
        im.setLore(List.of("Integration with interaction"));
        im.setCustomModelData(1001);
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 17);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "rustic_console");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("CBC", "LRL", "CBC");
            r.setIngredient('C', Material.COPPER_INGOT);
            r.setIngredient('L', Material.LEVER);
            r.setIngredient('R', Material.COMPARATOR);
        } else {
            r.shape("CBC", "ORO", "CBC");
            r.setIngredient('C', Material.COPPER_BLOCK);
            r.setIngredient('O', Material.COMPARATOR);
            r.setIngredient('R', Material.REDSTONE_BLOCK);
        }
        r.setIngredient('B', Material.BAMBOO_BUTTON);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put(dn, r);
    }
}
