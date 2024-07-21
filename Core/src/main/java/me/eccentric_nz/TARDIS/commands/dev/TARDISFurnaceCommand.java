package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

import java.util.Iterator;

public class TARDISFurnaceCommand {

    private final TARDIS plugin;
    public TARDISFurnaceCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean list(CommandSender sender) {
        Iterator<Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            Recipe r = recipes.next();
            if (r instanceof FurnaceRecipe f) {
                RecipeChoice c = f.getInputChoice();
                if (c instanceof RecipeChoice.MaterialChoice m) {
                    for (Material a : m.getChoices()) {
                        plugin.debug(a.toString());
                    }
                } else if (c instanceof RecipeChoice.ExactChoice e) {
                    plugin.debug(e.getChoices().toString());
                    for (ItemStack i : e.getChoices()) {
                        plugin.debug(i.getType().toString());
                    }
                } else {
                    plugin.debug(f.getInput().getType().toString());
                }
            }
        }
        return true;
    }
}
