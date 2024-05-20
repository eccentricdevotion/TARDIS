package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/*
easy_shape:-C-,-W-,R-R
easy_ingredients.C:GLOWSTONE_DUST=TARDIS Chameleon Circuit
easy_ingredients.W:CLOCK
easy_ingredients.R:REDSTONE
hard_shape:-C-,IWI,R-R
hard_ingredients.C:GLOWSTONE_DUST=TARDIS Chameleon Circuit
hard_ingredients.I:IRON_INGOT
hard_ingredients.W:CLOCK
hard_ingredients.R:REDSTONE
result:CLOCK
amount:1
*/

public class FobWatchRecipe {

    private final TARDIS plugin;

    public FobWatchRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.CLOCK, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Fob Watch");
        im.setCustomModelData(10000001);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "fob_watch");
        ShapedRecipe r = new ShapedRecipe(key, is);
        ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta em = exact.getItemMeta();
        em.setDisplayName("TARDIS Chameleon Circuit");
        em.setCustomModelData(RecipeItem.TARDIS_CHAMELEON_CIRCUIT.getCustomModelData());
        // set the second line of lore
        List<String> circuit;
        String uses = (plugin.getConfig().getString("circuits.uses.chameleon").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? ChatColor.YELLOW + "unlimited"
                : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.chameleon");
        circuit = Arrays.asList("Uses left", uses);
        em.setLore(circuit);
        exact.setItemMeta(em);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" C ", "IWI", "R R");
            r.setIngredient('C', new RecipeChoice.ExactChoice(exact));
            r.setIngredient('I', Material.IRON_INGOT);
            r.setIngredient('W', Material.CLOCK);
            r.setIngredient('R', Material.REDSTONE);

        } else {
            r.shape(" C ", " W ", "R R");
            r.setIngredient('C', new RecipeChoice.ExactChoice(exact));
            r.setIngredient('W', Material.CLOCK);
            r.setIngredient('R', Material.REDSTONE);

        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Fob Watch", r);
    }
}
