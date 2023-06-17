/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.Map;
import java.util.TreeMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
public class TARDISGiveLister {

    private final TARDIS plugin;
    private final CommandSender sender;
    private final TreeMap<String, String> dev = new TreeMap<>();

    public TARDISGiveLister(TARDIS plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
        dev.put("artron", "Artron Energy");
        dev.put("blueprint", "TARDIS Blueprint Disk");
        dev.put("kit", "TARDIS Item Kit");
        dev.put("recipes", "Grant TARDIS recipes");
        dev.put("seed", "TARDIS Seed Block");
    }

    public void list() {
        plugin.getMessenger().message(sender, TardisModule.TARDIS, "You can 'give' the following items:");
        plugin.getMessenger().messageWithColour(sender, "Hover over command argument to see a description", "#AAAAAA");
        plugin.getMessenger().messageWithColour(sender, "Click to suggest a command", "#AAAAAA");
        plugin.getMessenger().message(sender, "");
        plugin.getMessenger().message(sender, "Admin & development");
        for (Map.Entry<String, String> entry : dev.entrySet()) {
            // TODO add to messengers so we can use Adventure
            TextComponent tcd = new TextComponent(entry.getKey());
            tcd.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
            tcd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(entry.getValue())));
            tcd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tardisgive [player] " + entry.getKey() + " "));
            sender.spigot().sendMessage(tcd);
        }
        plugin.getMessenger().message(sender, "");
        for (RecipeCategory category : RecipeCategory.values()) {
            if (category != RecipeCategory.UNUSED && category != RecipeCategory.UNCRAFTABLE && category != RecipeCategory.CUSTOM_BLOCKS && category != RecipeCategory.ROTORS && category != RecipeCategory.MISC) {
                plugin.getMessenger().message(sender, category.getName());
                for (RecipeItem item : RecipeItem.values()) {
                    if (item.getCategory() == category) {
                        TextComponent tci = new TextComponent(item.toTabCompletionString());
                        tci.setColor(category.getColour());
                        tci.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(item.toRecipeString())));
                        tci.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tardisgive [player] " + item.toTabCompletionString() + " "));
                        sender.spigot().sendMessage(tci);
                    }
                }
                plugin.getMessenger().message(sender, "");
            }
        }
        TextComponent more = new TextComponent("Show more...");
        more.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click me!")));
        more.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tardisgive list_more"));
        sender.spigot().sendMessage(more);
    }

    public void listMore() {
        plugin.getMessenger().message(sender, "");
        for (RecipeCategory category : RecipeCategory.values()) {
            if (category == RecipeCategory.CUSTOM_BLOCKS || category == RecipeCategory.ROTORS || category == RecipeCategory.MISC) {
                plugin.getMessenger().message(sender, category.getName());
                for (RecipeItem item : RecipeItem.values()) {
                    if (item.getCategory() == category) {
                        TextComponent tci = new TextComponent(item.toTabCompletionString());
                        tci.setColor(category.getColour());
                        tci.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(item.toRecipeString())));
                        tci.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tardisgive [player] " + item.toTabCompletionString() + " "));
                        sender.spigot().sendMessage(tci);
                    }
                }
                plugin.getMessenger().message(sender, "");
            }
        }
    }
}
