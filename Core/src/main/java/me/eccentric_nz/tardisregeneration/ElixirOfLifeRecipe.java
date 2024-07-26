package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

/*
easy_shape:GPG,-G-,-G-
easy_ingredients.G:GOLD_NUGGET
easy_ingredients.P:POTION>AWKWARD
hard_shape:GPG,-G-,GGG
hard_ingredients.G:GOLD_NUGGET
hard_ingredients.P:POTION>AWKWARD
result:GOLD_INGOT
amount:1
*/

public class ElixirOfLifeRecipe {

    private final TARDIS plugin;

    public ElixirOfLifeRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is = ElixirOfLife.create();
        NamespacedKey key = new NamespacedKey(plugin, "elixir_of_life");
        ShapedRecipe r = new ShapedRecipe(key, is);
        if (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) {
            r.shape("GPG", " G ", "GGG");
        } else {
            r.shape("GPG", " G ", " G ");
        }
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta pm = (PotionMeta) potion.getItemMeta();
        pm.setBasePotionType(PotionType.AWKWARD);
        potion.setItemMeta(pm);
        r.setIngredient('G', Material.GOLD_NUGGET);
        r.setIngredient('P', new RecipeChoice.ExactChoice(potion));
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Elixir of Life", r);
    }
}
