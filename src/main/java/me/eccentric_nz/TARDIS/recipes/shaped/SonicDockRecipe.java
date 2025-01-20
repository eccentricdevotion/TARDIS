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

/*
easy_shape:RGR,GSG,BGB
easy_ingredients.R:REDSTONE
easy_ingredients.G:GOLD_NUGGET
easy_ingredients.S:REPEATER
easy_ingredients.B:BLACKSTONE
hard_shape:RGR,GSG,BGB
hard_ingredients.R:REDSTONE
hard_ingredients.G:GOLD_INGOT
hard_ingredients.S:GLOWSTONE_DUST=Sonic Oscillator
hard_ingredients.B:BLACKSTONE
result:FLOWER_POT
amount:1
*/

public class SonicDockRecipe {

    private final TARDIS plugin;

    public SonicDockRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = new ItemStack(Material.FLOWER_POT, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(NamedTextColor.WHITE + "Sonic Dock");
        im.setItemModel(RecipeItem.SONIC_DOCK.getModel());
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "sonic_dock");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("RGR", "GSG", "BGB");
        r.setIngredient('R', Material.REDSTONE);
        r.setIngredient('B', Material.BLACKSTONE);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.setIngredient('G', Material.GOLD_INGOT);
            ItemStack exact = new ItemStack(Material.GLOWSTONE_DUST, 1);
            ItemMeta em = exact.getItemMeta();
            em.setDisplayName(NamedTextColor.WHITE + "Sonic Oscillator");
            em.setItemModel(RecipeItem.SONIC_OSCILLATOR.getModel());
            exact.setItemMeta(em);
            r.setIngredient('S', new RecipeChoice.ExactChoice(exact));
        } else {
            r.shape("RGR", "GSG", "BGB");
            r.setIngredient('G', Material.GOLD_NUGGET);
            r.setIngredient('S', Material.REPEATER);
        }
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Sonic Dock", r);
    }
}
