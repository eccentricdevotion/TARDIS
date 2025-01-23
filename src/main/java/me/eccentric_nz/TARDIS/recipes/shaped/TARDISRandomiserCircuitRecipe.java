package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:-D-,NCE,-W-
easy_ingredients.D:DIRT
easy_ingredients.N:NETHERRACK
easy_ingredients.C:COMPASS
easy_ingredients.E:END_STONE
easy_ingredients.W:WATER_BUCKET
hard_shape:-D-,NCE,-W-
hard_ingredients.D:DIRT
hard_ingredients.N:NETHERRACK
hard_ingredients.C:COMPASS
hard_ingredients.E:END_STONE
hard_ingredients.W:WATER_BUCKET
result:GLOWSTONE_DUST
amount:1
lore:Uses left~50
*/

public class TARDISRandomiserCircuitRecipe {

    private final TARDIS plugin;

    public TARDISRandomiserCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(NamedTextColor.WHITE + "TARDIS Randomiser Circuit");
        im.setItemModel(RecipeItem.TARDIS_RANDOMISER_CIRCUIT.getModel());
        String uses = (plugin.getConfig().getString("circuits.uses.randomiser").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? NamedTextColor.YELLOW + "unlimited"
                : NamedTextColor.YELLOW + plugin.getConfig().getString("circuits.uses.randomiser");
        im.lore(List.of("Uses left", uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_randomiser_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape(" D ", "NCE", " W ");
        r.setIngredient('D', Material.DIRT);
        r.setIngredient('N', Material.NETHERRACK);
        r.setIngredient('C', Material.COMPASS);
        r.setIngredient('E', Material.END_STONE);
        r.setIngredient('W', Material.WATER_BUCKET);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Randomiser Circuit", r);
    }
}
