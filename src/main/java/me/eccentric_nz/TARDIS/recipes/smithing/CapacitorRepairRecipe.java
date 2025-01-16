package me.eccentric_nz.TARDIS.recipes.smithing;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmithingTransformRecipe;

/*
base:BLAZE_ROD
addition:GLOWSTONE_DUST=Redstone Activator Circuit
result:BLAZE_ROD

*/

public class CapacitorRepairRecipe {

    private final TARDIS plugin;

    public CapacitorRepairRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        // result
        ItemStack result = new ItemStack(Material.BUCKET, 1);
        // template
        RecipeChoice template = new RecipeChoice.MaterialChoice(Material.REDSTONE);
        // base material to upgrade
        RecipeChoice base = new RecipeChoice.MaterialChoice(Material.BUCKET);
        // addition
        RecipeChoice addition = new RecipeChoice.MaterialChoice(Material.COPPER_INGOT);
        NamespacedKey key = new NamespacedKey(plugin, "capacitor_repair");
        SmithingRecipe r = new SmithingTransformRecipe(key, result, template, base, addition);
        plugin.getServer().addRecipe(r);
    }
}
