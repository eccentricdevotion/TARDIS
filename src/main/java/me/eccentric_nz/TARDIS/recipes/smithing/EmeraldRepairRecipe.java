package me.eccentric_nz.TARDIS.recipes.smithing;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/*
base:BLAZE_ROD
addition:GLOWSTONE_DUST=Emerald Environment Circuit
result:BLAZE_ROD

*/

public class EmeraldRepairRecipe {

    private final TARDIS plugin;

    public EmeraldRepairRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        // result
        ItemStack result = new ItemStack(Material.BLAZE_ROD, 1);
        // template
        RecipeChoice template = new RecipeChoice.MaterialChoice(Material.REDSTONE);
        // base material to upgrade
        RecipeChoice base = new RecipeChoice.MaterialChoice(Material.BLAZE_ROD);
        // addition
        ItemStack isa = new ItemStack(Material.GLOWSTONE_DUST, 1);
        ItemMeta ima = isa.getItemMeta();
        ima.setDisplayName(NamedTextColor.WHITE + "Emerald Environment Circuit");
        ima.setItemModel(RecipeItem.EMERALD_ENVIRONMENT_CIRCUIT.getModel());
        isa.setItemMeta(ima);
        RecipeChoice addition = new RecipeChoice.ExactChoice(isa);
        NamespacedKey key = new NamespacedKey(plugin, "emerald_repair");
        SmithingRecipe r = new SmithingTransformRecipe(key, result, template, base, addition);
        plugin.getServer().addRecipe(r);
    }
}
