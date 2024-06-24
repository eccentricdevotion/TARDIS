package me.eccentric_nz.TARDIS.recipes.shaped;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/*
easy_shape:-I-,RWR,-I-
easy_ingredients.I:IRON_INGOT
easy_ingredients.R:REDSTONE
easy_ingredients.W:CLOCK
hard_shape:-I-,RWR,QIQ
hard_ingredients.I:IRON_INGOT
hard_ingredients.R:REDSTONE
hard_ingredients.W:CLOCK
hard_ingredients.Q:QUARTZ
result:GLOWSTONE_DUST
amount:1
lore:Uses left~20
*/

public class TARDISTemporalCircuitRecipe {

    private final TARDIS plugin;

    public TARDISTemporalCircuitRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Temporal Circuit");
        im.setCustomModelData(10001974);
        im.setLore(List.of("Uses left", "20"));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "tardis_temporal_circuit");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape(" I ", "RWR", "QIQ");
            r.setIngredient('Q', Material.QUARTZ);
        } else {
            r.shape(" I ", "RWR", " I ");
        }
        r.setIngredient('I', Material.IRON_INGOT);
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('W', Material.CLOCK);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("TARDIS Temporal Circuit", r);
    }
}
