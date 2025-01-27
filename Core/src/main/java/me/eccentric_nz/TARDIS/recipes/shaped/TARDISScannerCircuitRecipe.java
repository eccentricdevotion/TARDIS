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
easy_shape:GRG,RGR,GRG
easy_ingredients.G:GLASS_PANE
easy_ingredients.R:REDSTONE
hard_shape:GRG,RER,GRG
hard_ingredients.G:GLASS
hard_ingredients.E:SPIDER_EYE
hard_ingredients.R:REDSTONE
result:GLOWSTONE_DUST
amount:1
lore:Uses left~20
*/

public class TARDISScannerCircuitRecipe {

    private final TARDIS plugin;

    public TARDISScannerCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Scanner Circuit");
        im.setItemModel(RecipeItem.TARDIS_SCANNER_CIRCUIT.getModel());
        String uses = (plugin.getConfig().getString("circuits.uses.scanner").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? ChatColor.YELLOW + "unlimited"
                : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.scanner");
        im.setLore(List.of("Uses left", uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_scanner_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("GRG", "RER", "GRG");
            r.setIngredient('G', Material.GLASS);
            r.setIngredient('E', Material.SPIDER_EYE);
        } else {
            r.shape("GRG", "RGR", "GRG");
            r.setIngredient('G', Material.GLASS_PANE);
        }
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Scanner Circuit", r);
    }
}
