package me.eccentric_nz.TARDIS.chemistry.block;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashMap;

public class ChemistryBlock {

    public static HashMap<String, RecipeData> RECIPES = new HashMap<String, RecipeData>() {
        {
            put("creative", new RecipeData("Atomic elements", "creative_block", Arrays.asList("A creative inventory", "of atomic elements."), Material.DIAMOND, 10000040));
            put("compound", new RecipeData("Chemical compounds", "compound_block", Arrays.asList("Create over thirty compounds", "by combining elements."), Material.REDSTONE, 10000041));
            put("reduce", new RecipeData("Material reducer", "reducer_block", Arrays.asList("Learn about the natural world", "by reducing Minecraft blocks", "to their component elements."), Material.GOLD_NUGGET, 10000042));
            put("construct", new RecipeData("Element constructor", "constructor_block", Arrays.asList("Build elements by choosing the number", "of protons, electrons, and neutrons."), Material.LAPIS_LAZULI, 10000043));
            put("lab", new RecipeData("Lab table", "lab_block", Arrays.asList("Combine substances to", "get new results."), Material.COAL, 10000044));
            put("product", new RecipeData("Product crafting", "crafting_block", Arrays.asList("Combine substances to", "make fun products."), Material.IRON_NUGGET, 10000045));
        }
    };
}
