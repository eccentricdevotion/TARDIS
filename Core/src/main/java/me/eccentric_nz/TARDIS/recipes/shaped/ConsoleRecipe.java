package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.ColourType;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Locale;
import java.util.Map;

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

public class ConsoleRecipe {

    private final TARDIS plugin;

    public ConsoleRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipes() {
        for (Map.Entry<Material, Integer> colour : ColourType.LOOKUP.entrySet()) {
            // get colour name
            String name = colour.getKey().toString().replace("_CONCRETE_POWDER", "");
            Material material = Material.valueOf(name + "_CONCRETE");
            ItemStack is = new ItemStack(material, 1);
            ItemMeta im = is.getItemMeta();
            String dn = TARDISStringUtils.capitalise(name) + " Console";
            im.setDisplayName(ChatColor.WHITE + dn);
            im.setLore(List.of("Integration with interaction"));
            im.setCustomModelData(1001);
            im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, colour.getValue()
            );
            is.setItemMeta(im);
            NamespacedKey key = new NamespacedKey(plugin, name.toLowerCase(Locale.ROOT) + "_console");
            ShapedRecipe r = new ShapedRecipe(key, is);
            if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
                r.shape("CBC", "LRL", "CBC");
                r.setIngredient('L', Material.LEVER);
                r.setIngredient('R', Material.COMPARATOR);
            } else {
                r.shape("CBC", "ORO", "CBC");
                r.setIngredient('O', Material.COMPARATOR);
                r.setIngredient('R', Material.REDSTONE_BLOCK);
            }
            r.setIngredient('C', colour.getKey());
            r.setIngredient('B', Material.BAMBOO_BUTTON);
            plugin.getServer().addRecipe(r);
            plugin.getFigura().getShapedRecipes().put(dn, r);
        }
    }
}
