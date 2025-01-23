package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/*
easy_shape:-G-,RER,-G-
easy_ingredients.G:GOLD_INGOT
easy_ingredients.E:DETECTOR_RAIL
easy_ingredients.R:REDSTONE
hard_shape:DGD,GEG,RMR
hard_ingredients.D:REPEATER
hard_ingredients.G:GOLD_INGOT
hard_ingredients.E:DETECTOR_RAIL
hard_ingredients.R:REDSTONE
hard_ingredients.M:GLOWSTONE_DUST=TARDIS Materialisation Circuit
result:GLOWSTONE_DUST
amount:1
lore:Uses left~25
*/

public class TARDISChameleonCircuitRecipe {

    private final TARDIS plugin;

    public TARDISChameleonCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(NamedTextColor.WHITE + "TARDIS Chameleon Circuit");
        im.setItemModel(RecipeItem.TARDIS_CHAMELEON_CIRCUIT.getModel());
        String uses = (plugin.getConfig().getString("circuits.uses.chameleon").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? NamedTextColor.YELLOW + "unlimited"
                : NamedTextColor.YELLOW + plugin.getConfig().getString("circuits.uses.chameleon");
        im.lore(List.of("Uses left", uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_chameleon_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("DGD", "GEG", "RMR");
            r.setIngredient('D', Material.REPEATER);
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName(NamedTextColor.WHITE + "TARDIS Materialisation Circuit");
            em.setItemModel(RecipeItem.TARDIS_MATERIALISATION_CIRCUIT.getModel());
            // set the second line of lore
            List<String> circuit;
            String mat_uses = (plugin.getConfig().getString("circuits.uses.materialisation").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                    ? NamedTextColor.YELLOW + "unlimited"
                    : NamedTextColor.YELLOW + plugin.getConfig().getString("circuits.uses.materialisation");
            circuit = Arrays.asList("Uses left", mat_uses);
            em.lore(circuit);
            exact.setItemMeta(em);
            r.setIngredient('M', new RecipeChoice.ExactChoice(exact));
        } else {
            r.shape(" G ", "RER", " G ");
        }
        r.setIngredient('G', Material.GOLD_INGOT);
        r.setIngredient('E', Material.DETECTOR_RAIL);
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Chameleon Circuit", r);
    }
}
