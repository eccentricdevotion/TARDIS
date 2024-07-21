package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

/*
easy_shape:-S-,SES,-S-
easy_ingredients.S:SLIME_BALL
easy_ingredients.E:EMERALD
hard_shape:-S-,SPS,ESE
hard_ingredients.S:SLIME_BALL
hard_ingredients.P:POTION>AWKWARD
hard_ingredients.E:EMERALD
result:DAYLIGHT_DETECTOR
amount:1
lore:Allow companions to~use TARDIS commands
*/

public class TARDISTelepathicCircuitRecipe {

    private final TARDIS plugin;

    public TARDISTelepathicCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Telepathic Circuit");
        im.setCustomModelData(10001962);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_telepathic_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" S ", "SPS", "ESE");
            ItemStack potion = new ItemStack(Material.POTION, 1);
            PotionMeta pm = (PotionMeta) potion.getItemMeta();
            pm.setBasePotionType(PotionType.AWKWARD);
            potion.setItemMeta(pm);
            r.setIngredient('P', new RecipeChoice.ExactChoice(potion));
        } else {
            r.shape(" S ", "SES", " S ");
        }
        r.setIngredient('S', Material.SLIME_BALL);
        r.setIngredient('E', Material.EMERALD);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Telepathic Circuit", r);
    }
}
