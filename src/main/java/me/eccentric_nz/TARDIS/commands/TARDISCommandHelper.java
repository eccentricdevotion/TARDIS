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
package me.eccentric_nz.TARDIS.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RootCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @author eccentric_nz
 */
public class TARDISCommandHelper {

    private final TARDIS plugin;
    private final List<String> notThese = Arrays.asList("aliases", "description", "usage", "permission", "permission-message");

    public TARDISCommandHelper(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void getCommand(String c, CommandSender sender) {
        if (c.isEmpty()) {
            sender.sendMessage("------");
            sender.sendMessage(ChatColor.GOLD + "TARDIS commands - use " + ChatColor.AQUA + "/tardis? <command> " + ChatColor.RESET + "for more info");
            sender.sendMessage(ChatColor.GRAY + "Online: " + ChatColor.RESET + "https://eccentricdevotion.github.io/TARDIS/commands.html");
            plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands").getKeys(false).forEach((o) -> {
                if (!o.equals("tardis?")) {
                    sender.sendMessage("/" + o);
                }
            });
            sender.sendMessage("------");
        } else {
            String[] split = c.split(" ");
            try {
                RootCommand root = RootCommand.valueOf(split[0].toLowerCase(Locale.ENGLISH));
                if (split.length > 1) {
                    if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + "." + split[1].toLowerCase(Locale.ENGLISH))) {
                        sender.sendMessage("------");
                        sender.sendMessage("Command: " + ChatColor.GOLD + "/" + c.toLowerCase(Locale.ENGLISH));
                        sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + "." + split[1].toLowerCase(Locale.ENGLISH) + ".description"));
                        if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + "." + split[1].toLowerCase(Locale.ENGLISH) + ".usage")) {
                            sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + "." + split[1].toLowerCase(Locale.ENGLISH) + ".usage").replace("<command>", root.toString()));
                        }
                        if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + "." + split[1].toLowerCase(Locale.ENGLISH) + ".permission")) {
                            sender.sendMessage(ChatColor.GRAY + "Permission: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + "." + split[1].toLowerCase(Locale.ENGLISH) + ".permission"));
                        } else if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + ".permission")) {
                            sender.sendMessage(ChatColor.GRAY + "Permission: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".permission"));
                        } else {
                            sender.sendMessage(ChatColor.GRAY + "Permission: " + ChatColor.RESET + "None required");
                        }
                        sender.sendMessage("------");
                    } else {
                        sender.sendMessage("Invalid TARDIS help command argument: " + c);
                    }
                } else {
                    sender.sendMessage("------");
                    Set<String> args = plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands." + root).getKeys(false);
                    if (args.size() > 5) {
                        sender.sendMessage(ChatColor.GOLD + "/" + root + ChatColor.RESET + " commands - use " + ChatColor.AQUA + "/tardis? " + root + " <argument> " + ChatColor.RESET + "for more info");
                        sender.sendMessage(ChatColor.GRAY + "Online: " + ChatColor.RESET + root.URL);
                        sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".description"));
                        sender.sendMessage(ChatColor.GRAY + "Aliases: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".aliases"));
                        if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + ".usage")) {
                            sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".usage").replace("<command>", root.toString()));
                        }
                        args.forEach((m) -> {
                            if (!notThese.contains(m)) {
                                sender.sendMessage("/" + c + " " + m);
                            }
                        });
                    } else {
                        sender.sendMessage("Command: " + ChatColor.GOLD + "/" + root);
                        sender.sendMessage(ChatColor.GRAY + "Online: " + ChatColor.RESET + root.URL);
                        sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".description"));
                        sender.sendMessage(ChatColor.GRAY + "Aliases: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".aliases"));
                        sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".usage").replace("<command>", root.toString()));
                        sender.sendMessage(ChatColor.GRAY + "Permission: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".permission"));
                    }
                    sender.sendMessage("------");
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage("Invalid TARDIS help command argument: " + c);
            }
        }
    }
}
