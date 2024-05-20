package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.List;

/*
easy_shape:-D-,P-E,-W-
easy_ingredients.D:DIAMOND
easy_ingredients.P:GLOWSTONE_DUST=Perception Circuit
easy_ingredients.E:EMERALD
easy_ingredients.W:POTION>INVISIBILITY
hard_shape:-D-,P-E,-W-
hard_ingredients.D:DIAMOND
hard_ingredients.P:GLOWSTONE_DUST=Perception Circuit
hard_ingredients.E:EMERALD
hard_ingredients.W:POTION>INVISIBILITY
result:GLOWSTONE_DUST
amount:1
lore:Uses left~5
*/

public class TARDISInvisibilityCircuitRecipe {

    private final TARDIS plugin;

    public TARDISInvisibilityCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Invisibility Circuit");
        im.setCustomModelData(10001981);
        im.setLore(List.of("Uses left", "5"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_invisibility_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName("Perception Circuit");
        em.setCustomModelData(RecipeItem.PERCEPTION_CIRCUIT.getCustomModelData());
        exact.setItemMeta(em);
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta pm = (PotionMeta) potion.getItemMeta();
        pm.setBasePotionType(PotionType.INVISIBILITY);
        potion.setItemMeta(pm);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" D ", "P E", " W ");
            r.setIngredient('D', Material.DIAMOND);
            r.setIngredient('P', new RecipeChoice.ExactChoice(exact));
            r.setIngredient('E', Material.EMERALD);
            r.setIngredient('W', new RecipeChoice.ExactChoice(potion));
        } else {
            r.shape(" D ", "P E", " W ");
            r.setIngredient('D', Material.DIAMOND);
            r.setIngredient('P', new RecipeChoice.ExactChoice(exact));
            r.setIngredient('E', Material.EMERALD);
            r.setIngredient('W', new RecipeChoice.ExactChoice(potion));
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Invisibility Circuit", r);
    }
}
