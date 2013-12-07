/*
 *  Copyright 2013 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRecipeUtilities {

    TARDIS plugin;

    public TARDISRecipeUtilities(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Method to find the configuration section a recipe is in. Thought this
     * might be useful to allow custom recipes to contain items that have been
     * crafted from another custom recipe. But you can't set an ingredient to an
     * ItemStack so it is pretty much useless.
     *
     * @param s the recipe to search for
     * @return the recipe type
     */
    public RecipeType findConfigSection(String s) {
        if (plugin.getRecipesConfig().isConfigurationSection("shaped." + s)) {
            return RecipeType.SHAPED;
        }
        if (plugin.getRecipesConfig().isConfigurationSection("shapeless." + s)) {
            return RecipeType.SHAPELESS;
        }
        if (plugin.getRecipesConfig().isConfigurationSection("furnace." + s)) {
            return RecipeType.FURNACE;
        }
        return RecipeType.NONE;
    }

    public enum RecipeType {

        SHAPED, SHAPELESS, FURNACE, NONE
    }
}
