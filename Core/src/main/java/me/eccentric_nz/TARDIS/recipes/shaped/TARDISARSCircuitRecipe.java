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
easy_shape:SSS,T-T,RRR
easy_ingredients.S:WHEAT_SEEDS
easy_ingredients.T:REDSTONE_TORCH
easy_ingredients.R:REDSTONE
hard_shape:SSS,DPT,RRR
hard_ingredients.S:WHEAT_SEEDS
hard_ingredients.D:REPEATER
hard_ingredients.P:PISTON
hard_ingredients.T:REDSTONE_TORCH
hard_ingredients.R:REDSTONE
result:GLOWSTONE_DUST
amount:1
lore:Uses left~20
*/

public class TARDISARSCircuitRecipe {

    private final TARDIS plugin;

    public TARDISARSCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS ARS Circuit");
        im.setItemModel(RecipeItem.TARDIS_ARS_CIRCUIT.getModel());
        String uses = (plugin.getConfig().getString("circuits.uses.ars").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? ChatColor.YELLOW + "unlimited"
                : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.ars");
        im.setLore(List.of("Uses left", uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_ars_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("SSS", "DPT", "RRR");
            r.setIngredient('D', Material.REPEATER);
            r.setIngredient('P', Material.PISTON);
        } else {
            r.shape("SSS", "T T", "RRR");
        }
        r.setIngredient('S', Material.WHEAT_SEEDS);
        r.setIngredient('T', Material.REDSTONE_TORCH);
        r.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS ARS Circuit", r);
    }
}
