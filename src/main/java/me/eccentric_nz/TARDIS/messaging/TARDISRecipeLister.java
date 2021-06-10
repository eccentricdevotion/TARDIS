/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.messaging;

import me.eccentric_nz.tardis.enumeration.RecipeCategory;
import me.eccentric_nz.tardis.enumeration.RecipeItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
public class TARDISRecipeLister {

    private final CommandSender sender;

    public TARDISRecipeLister(CommandSender sender) {
        this.sender = sender;
    }

    public void list() {
        TARDISMessage.send(sender, "RECIPE_VIEW");
        TARDISMessage.message(sender, ChatColor.GRAY + "Hover over command argument to see a description");
        TARDISMessage.message(sender, ChatColor.GRAY + "Click to view the recipe");
        TARDISMessage.message(sender, "");
        for (RecipeCategory category : RecipeCategory.values()) {
            if (category != RecipeCategory.UNUSED && category != RecipeCategory.UNCRAFTABLE) {
                TARDISMessage.message(sender, category.getName());
                for (RecipeItem item : RecipeItem.values()) {
                    if (item.getCategory() == category) {
                        TextComponent tci = new TextComponent(item.toTabCompletionString());
                        tci.setColor(category.getColour());
                        tci.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(item.toRecipeString())));
                        tci.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardisrecipe " + item.toTabCompletionString()));
                        sender.spigot().sendMessage(tci);
                    }
                }
                TARDISMessage.message(sender, "");
            }
        }
    }
}
