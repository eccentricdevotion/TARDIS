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

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.enumeration.RecipeCategory;
import me.eccentric_nz.tardis.enumeration.RecipeItem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author eccentric_nz
 */
public class TardisGiveLister {

    private final TardisPlugin plugin;
    private final CommandSender sender;
    private final TreeMap<String, String> dev = new TreeMap<>();

    public TardisGiveLister(TardisPlugin plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
        dev.put("artron", "Artron Energy");
        dev.put("blueprint", "TARDIS Blueprint Disk");
        dev.put("kit", "TARDIS Item Kit");
        dev.put("recipes", "Grant TARDIS recipes");
        dev.put("seed", "TARDIS Seed Block");
        dev.put("mushroom", "Textured mushroom blocks");
    }

    public void list() {
        TardisMessage.message(sender, plugin.getPluginName() + "You can 'give' the following items:");
        TardisMessage.message(sender, ChatColor.GRAY + "Hover over command argument to see a description");
        TardisMessage.message(sender, ChatColor.GRAY + "Click to suggest a command");
        TardisMessage.message(sender, "");
        TardisMessage.message(sender, "Admin & development");
        for (Map.Entry<String, String> entry : dev.entrySet()) {
            TextComponent tcd = new TextComponent(entry.getKey());
            tcd.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
            tcd.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(entry.getValue())));
            tcd.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tardisgive [player] " + entry.getKey() + " "));
            sender.spigot().sendMessage(tcd);
        }
        TardisMessage.message(sender, "");
        for (RecipeCategory category : RecipeCategory.values()) {
            if (category != RecipeCategory.UNUSED && category != RecipeCategory.UNCRAFTABLE) {
                TardisMessage.message(sender, category.getName());
                for (RecipeItem item : RecipeItem.values()) {
                    if (item.getCategory() == category) {
                        TextComponent tci = new TextComponent(item.toTabCompletionString());
                        tci.setColor(category.getColour());
                        tci.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(item.toRecipeString())));
                        tci.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tardisgive [player] " + item.toTabCompletionString() + " "));
                        sender.spigot().sendMessage(tci);
                    }
                }
            }
            TardisMessage.message(sender, "");
        }
    }
}
