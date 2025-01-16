package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:IRI,-L-,QRQ
easy_ingredients.I:IRON_INGOT
easy_ingredients.R:REDSTONE
easy_ingredients.L:BLUE_DYE
easy_ingredients.Q:QUARTZ
hard_shape:IDI,DLD,QRQ
hard_ingredients.I:ENDER_EYE
hard_ingredients.D:REPEATER
hard_ingredients.R:REDSTONE
hard_ingredients.L:BLUE_DYE
hard_ingredients.Q:QUARTZ
result:GLOWSTONE_DUST
amount:1
lore:Uses left~50
*/

public class TARDISMaterialisationCircuitRecipe {

    private final TARDIS plugin;

    public TARDISMaterialisationCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Materialisation Circuit");
        im.setItemModel(RecipeItem.TARDIS_MATERIALISATION_CIRCUIT.getModel());
        String uses = (plugin.getConfig().getString("circuits.uses.materialisation").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? ChatColor.YELLOW + "unlimited"
                : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.materialisation");
        im.setLore(List.of("Uses left", uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_materialisation_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("IDI", "DLD", "QRQ");
            r.setIngredient('I', Material.ENDER_EYE);
            r.setIngredient('D', Material.REPEATER);
        } else {
            r.shape("IRI", " L ", "QRQ");
            r.setIngredient('I', Material.IRON_INGOT);
        }
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('L', Material.BLUE_DYE);
        r.setIngredient('Q', Material.QUARTZ);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Materialisation Circuit", r);
    }
}
