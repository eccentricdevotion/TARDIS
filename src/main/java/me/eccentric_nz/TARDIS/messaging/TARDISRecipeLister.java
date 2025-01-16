/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.messaging;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
public class TARDISRecipeLister {

    private final TARDIS plugin;
    private final CommandSender sender;

    public TARDISRecipeLister(TARDIS plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
    }

    public void list() {
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "RECIPE_VIEW");
        plugin.getMessenger().messageWithColour(sender, "Hover over command argument to see a description", "#AAAAAA");
        plugin.getMessenger().messageWithColour(sender, "Click to view the recipe", "#AAAAAA");
        plugin.getMessenger().message(sender, "");
        for (RecipeCategory category : RecipeCategory.values()) {
            if (category != RecipeCategory.UNUSED && category != RecipeCategory.UNCRAFTABLE && category != RecipeCategory.CUSTOM_BLOCKS && category != RecipeCategory.ROTORS && category != RecipeCategory.MISC) {
                plugin.getMessenger().message(sender, category.getName());
                for (RecipeItem item : RecipeItem.values()) {
                    if (item.getCategory() == category) {
                        plugin.getMessenger().sendRunCommand(sender, item.toTabCompletionString(), item.toRecipeString(), category.getColour());
                    }
                }
                plugin.getMessenger().message(sender, "");
            }
        }
        plugin.getMessenger().sendShowMore(sender, "tardisrecipe");
    }

    public void listMore() {
        plugin.getMessenger().message(sender, "");
        for (RecipeCategory category : RecipeCategory.values()) {
            if (category == RecipeCategory.CUSTOM_BLOCKS || category == RecipeCategory.ROTORS || category == RecipeCategory.MISC) {
                plugin.getMessenger().message(sender, category.getName());
                for (RecipeItem item : RecipeItem.values()) {
                    if (item.getCategory() == category) {
                        plugin.getMessenger().sendRunCommand(sender, item.toTabCompletionString(), item.toRecipeString(), category.getColour());
                    }
                }
                plugin.getMessenger().message(sender, "");
            }
        }
    }
}
