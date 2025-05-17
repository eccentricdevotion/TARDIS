/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    public boolean list() {
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
