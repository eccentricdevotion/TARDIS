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
easy_shape:-T-,TCT,-T-
easy_ingredients.T:REDSTONE_TORCH
easy_ingredients.C:CHEST
hard_shape:RTR,TCT,RTR
hard_ingredients.R:RED_SAND
hard_ingredients.T:REDSTONE_TORCH
hard_ingredients.C:TRAPPED_CHEST
result:GLOWSTONE_DUST
amount:1
lore:Uses left~20
*/

public class TARDISMemoryCircuitRecipe {

    private final TARDIS plugin;

    public TARDISMemoryCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "TARDIS Memory Circuit");
        im.setItemModel(RecipeItem.TARDIS_MEMORY_CIRCUIT.getModel());
        String uses = (plugin.getConfig().getString("circuits.uses.memory").equals("0") || !plugin.getConfig().getBoolean("circuits.damage"))
                ? ChatColor.YELLOW + "unlimited"
                : ChatColor.YELLOW + plugin.getConfig().getString("circuits.uses.memory");
        im.setLore(List.of("Uses left", uses));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_memory_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("RTR", "TCT", "RTR");
            r.setIngredient('R', Material.RED_SAND);
            r.setIngredient('C', Material.TRAPPED_CHEST);
        } else {
            r.shape(" T ", "TCT", " T ");
            r.setIngredient('C', Material.CHEST);
        }
        r.setIngredient('T', Material.REDSTONE_TORCH);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Memory Circuit", r);
    }
}
