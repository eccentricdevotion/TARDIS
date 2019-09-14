package me.eccentric_nz.TARDIS.chemistry.block;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChemistryBlockRecipes {

    private final TARDIS plugin;
    private final List<RecipeData> recipes = new ArrayList<>();

    public ChemistryBlockRecipes(TARDIS plugin) {
        this.plugin = plugin;
        recipes.add(new RecipeData("Atomic elements", "creative_block", Arrays.asList("A creative inventory", "of atomic elements."), Material.DIAMOND));
        recipes.add(new RecipeData("Chemical compounds", "compound_block", Arrays.asList("Create over thirty compounds", "by combining elements."), Material.REDSTONE));
        recipes.add(new RecipeData("Material reducer", "reducer_block", Arrays.asList("Learn about the natural world", "by reducing Minecraft blocks", "to their component elements."), Material.GOLD_NUGGET));
        recipes.add(new RecipeData("Element constructor", "constructor_block", Arrays.asList("Build elements by choosing the number", "of protons, electrons, and neutrons."), Material.LAPIS_LAZULI));
        recipes.add(new RecipeData("Lab table", "lab_block", Arrays.asList("Combine substances to", "get new results."), Material.COAL));
        recipes.add(new RecipeData("Product crafting", "crafting_block", Arrays.asList("Combine substances to", "make fun products."), Material.IRON_NUGGET));
    }

    public void addRecipes() {
        int cmd = 40;
        for (RecipeData data : recipes) {
            ItemStack is = new ItemStack(Material.RED_MUSHROOM_BLOCK, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(data.getDisplayName());
            im.setLore(data.getLore());
            im.setCustomModelData(cmd);
            is.setItemMeta(im);
            NamespacedKey key = new NamespacedKey(plugin, data.getNameSpacedKey());
            ShapedRecipe recipe = new ShapedRecipe(key, is);
            recipe.shape("AAA", "ACA", "AAA");
            recipe.setIngredient('A', data.getCraftMaterial());
            recipe.setIngredient('C', Material.CRAFTING_TABLE);
            plugin.getServer().addRecipe(recipe);
            cmd++;
        }
    }
}
