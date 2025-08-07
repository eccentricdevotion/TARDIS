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
package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RootCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISCommandHelper {

    private final TARDIS plugin;
    private final List<String> notThese = List.of("aliases", "description", "usage", "permission", "permission-message");

    public TARDISCommandHelper(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void getCommand(String c, CommandSender sender) {
        if (c.isEmpty()) {
            sender.sendMessage("------");
            plugin.getMessenger().sendCommand(sender, "TARDIS", "/tardis? <command>");
            plugin.getMessenger().sendWithColours(sender, "Online: ", "#AAAAAA", "https://tardis.pages.dev/commands", "#FFFFFF");
            plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands").getKeys(false).forEach((o) -> {
                if (!o.equals("tardis?")) {
                    sender.sendMessage("/" + o);
                }
            });
            sender.sendMessage("------");
        } else {
            String[] split = c.split(" ");
            try {
                RootCommand root = RootCommand.valueOf(split[0].toLowerCase(Locale.ROOT));
                if (split.length > 1) {
                    if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + "." + split[1].toLowerCase(Locale.ROOT))) {
                        sender.sendMessage("------");
                        plugin.getMessenger().sendWithColours(sender, "Command: ", "#FFFFFF", "/" + c.toLowerCase(Locale.ROOT), "#FFAA00");
                        plugin.getMessenger().sendWithColours(sender, "Description: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + "." + split[1].toLowerCase(Locale.ROOT) + ".description"), "#FFFFFF");
                        if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + "." + split[1].toLowerCase(Locale.ROOT) + ".usage")) {
                            plugin.getMessenger().sendWithColours(sender, "Usage: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + "." + split[1].toLowerCase(Locale.ROOT) + ".usage").replace("<command>", root.toString()), "#FFFFFF");
                        }
                        if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + "." + split[1].toLowerCase(Locale.ROOT) + ".permission")) {
                            plugin.getMessenger().sendWithColours(sender, "Permission: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + "." + split[1].toLowerCase(Locale.ROOT) + ".permission"), "#FFFFFF");
                        } else if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + ".permission")) {
                            plugin.getMessenger().sendWithColours(sender, "Permission: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".permission"), "#FFFFFF");
                        } else {
                            plugin.getMessenger().sendWithColours(sender, "Permission: ", "#AAAAAA", "None required", "#FFFFFF");
                        }
                        sender.sendMessage("------");
                    } else {
                        sender.sendMessage("Invalid TARDIS help command argument: " + c);
                    }
                } else {
                    sender.sendMessage("------");
                    Set<String> args = plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands." + root).getKeys(false);
                    if (args.size() > 5) {
                        plugin.getMessenger().sendCommand(sender, "/" + root, "/tardis? " + root + " <argument>");
                        plugin.getMessenger().sendWithColours(sender, "Online: ", "#AAAAAA", root.URL, "#FFFFFF");
                        plugin.getMessenger().sendWithColours(sender, "Description: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".description"), "#FFFFFF");
                        plugin.getMessenger().sendWithColours(sender, "Aliases: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".aliases"), "#FFFFFF");
                        if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + ".usage")) {
                            plugin.getMessenger().sendWithColours(sender, "Usage: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".usage").replace("<command>", root.toString()), "#FFFFFF");
                        }
                        args.forEach((m) -> {
                            if (!notThese.contains(m)) {
                                sender.sendMessage("/" + c + " " + m);
                            }
                        });
                    } else {
                        plugin.getMessenger().sendWithColours(sender, "Command: ", "#FFFFFF", "/" + root, "#FFAA00");
                        plugin.getMessenger().sendWithColours(sender, "Online: ", "#AAAAAA", root.URL, "#FFFFFF");
                        plugin.getMessenger().sendWithColours(sender, "Description: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".description"), "#FFFFFF");
                        plugin.getMessenger().sendWithColours(sender, "Aliases: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".aliases"), "#FFFFFF");
                        plugin.getMessenger().sendWithColours(sender, "Usage: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".usage").replace("<command>", root.toString()), "#FFFFFF");
                        plugin.getMessenger().sendWithColours(sender, "Permission: ", "#AAAAAA", plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".permission"), "#FFFFFF");
                    }
                    sender.sendMessage("------");
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage("Invalid TARDIS help command argument: " + c);
            }
        }
    }
}
