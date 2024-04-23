package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.ColourType;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

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
    private final RecipeChoice.MaterialChoice choices;

    public ConsoleRecipe(TARDIS plugin) {
        this.plugin = plugin;
        this.choices = new RecipeChoice.MaterialChoice(Tag.CONCRETE_POWDER.getValues().toArray(new Material[0]));
    }

    public void addRecipes() {
        for (Map.Entry<Material, Integer> colour : ColourType.LOOKUP.entrySet()) {
            // get colour name
            String name = colour.getKey().toString().replace("_CONCRETE_POWDER", "");
            ItemStack is = new ItemStack(Material.LIGHT_GRAY_CONCRETE, 1);
            ItemMeta im = is.getItemMeta();
            String dn = TARDISStringUtils.capitalise(name) + " Console";
            plugin.debug("dn " + dn);
            im.setDisplayName(dn);
            im.setCustomModelData(1000 + colour.getValue());
            is.setItemMeta(im);
            NamespacedKey key = new NamespacedKey(plugin, name.toLowerCase() + "_console");
            plugin.debug("key " + key);
            ShapedRecipe r = new ShapedRecipe(key, is);
            if (plugin.getDifficulty() == Difficulty.HARD) {
                r.shape("CBC", "LRL", "CBC");
                r.setIngredient('C', colour.getKey());
                r.setIngredient('B', Material.BAMBOO_BUTTON);
                r.setIngredient('L', Material.LEVER);
                r.setIngredient('R', Material.COMPARATOR);
            } else {
                r.shape("CBC", "ORO", "CBC");
                r.setIngredient('C', colour.getKey());
                r.setIngredient('B', Material.BAMBOO_BUTTON);
                r.setIngredient('O', Material.COMPARATOR);
                r.setIngredient('R', Material.REDSTONE_BLOCK);
            }
            plugin.getServer().addRecipe(r);
            plugin.getFigura().getShapedRecipes().put(dn, r);
        }
    }
}
