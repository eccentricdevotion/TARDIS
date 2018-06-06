/*
 * Copyright (C) 2018 eccentric_nz
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
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISCommandHelper {

    private final TARDIS plugin;
    List<String> notThese = Arrays.asList("aliases", "description", "usage", "permission", "permission-message");

    public TARDISCommandHelper(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void getCommand(String c, CommandSender sender) {
        if (c.isEmpty()) {
            sender.sendMessage("------");
            sender.sendMessage(ChatColor.GOLD + "TARDIS commands - use " + ChatColor.AQUA + "/tardis? <command> " + ChatColor.RESET + "for more info");
            sender.sendMessage(ChatColor.GRAY + "Online: " + ChatColor.RESET + "http://goo.gl/f8lWbP");
            plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands").getKeys(false).forEach((o) -> {
                if (!o.equals("tardis?")) {
                    sender.sendMessage("/" + o);
                }
            });
            sender.sendMessage("------");
        } else {
            String[] split = c.split(" ");
            try {
                ROOT_COMMAND root = ROOT_COMMAND.valueOf(split[0].toLowerCase(Locale.ENGLISH));
                if (split.length > 1) {
                    if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root.toString() + "." + split[1].toLowerCase(Locale.ENGLISH))) {
                        sender.sendMessage("------");
                        sender.sendMessage("Command: " + ChatColor.GOLD + "/" + c.toLowerCase(Locale.ENGLISH));
                        sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + "." + split[1].toLowerCase(Locale.ENGLISH) + ".description"));
                        sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + "." + split[1].toLowerCase(Locale.ENGLISH) + ".usage").replace("<command>", root.toString()));
                        if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root.toString() + "." + split[1].toLowerCase(Locale.ENGLISH) + ".permission")) {
                            sender.sendMessage(ChatColor.GRAY + "Permission: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + "." + split[1].toLowerCase(Locale.ENGLISH) + ".permission"));
                        } else if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root.toString() + ".permission")) {
                            sender.sendMessage(ChatColor.GRAY + "Permission: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + ".permission"));
                        } else {
                            sender.sendMessage(ChatColor.GRAY + "Permission: " + ChatColor.RESET + "None required");
                        }
                        sender.sendMessage("------");
                    } else {
                        sender.sendMessage("Invalid TARDIS help command argument: " + c);
                    }
                } else {
                    sender.sendMessage("------");
                    Set<String> args = plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands." + root.toString()).getKeys(false);
                    if (args.size() > 5) {
                        sender.sendMessage(ChatColor.GOLD + "/" + root.toString() + ChatColor.RESET + " commands - use " + ChatColor.AQUA + "/tardis? " + root.toString() + " <argument> " + ChatColor.RESET + "for more info");
                        sender.sendMessage(ChatColor.GRAY + "Online: " + ChatColor.RESET + root.URL);
                        sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + ".description"));
                        sender.sendMessage(ChatColor.GRAY + "Aliases: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + ".aliases"));
                        args.forEach((m) -> {
                            if (!notThese.contains(m)) {
                                sender.sendMessage("/" + c + " " + m);
                            }
                        });
                    } else {
                        sender.sendMessage("Command: " + ChatColor.GOLD + "/" + root.toString());
                        sender.sendMessage(ChatColor.GRAY + "Online: " + ChatColor.RESET + root.URL);
                        sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + ".description"));
                        sender.sendMessage(ChatColor.GRAY + "Aliases: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + ".aliases"));
                        sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + ".usage").replace("<command>", root.toString()));
                        sender.sendMessage(ChatColor.GRAY + "Permission: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + ".permission"));
                    }
                    sender.sendMessage("------");
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage("Invalid TARDIS help command argument: " + c);
            }
        }
    }

    public static enum ROOT_COMMAND {

        tardis("http://goo.gl/55uTqL"),
        tardistravel("http://goo.gl/5rZR1T"),
        tardisadmin("http://goo.gl/jWFyLX"),
        tardisgive("http://goo.gl/LGQgy5"),
        tardisroom("http://goo.gl/zh9RKK"),
        tardisprefs("http://goo.gl/6k3RqD"),
        tardisarea("http://goo.gl/AJM2i3"),
        tardisartron("http://goo.gl/00ueX0"),
        tardisbind("http://goo.gl/sedpK4"),
        tardisgravity("http://goo.gl/vczqjf"),
        tardisbook("http://goo.gl/BGPh3t"),
        tardistexture("http://goo.gl/FPuxoa"),
        tardisrecipe("http://goo.gl/WSHA6N"),
        tardissay("http://goo.gl/iphcoM"),
        tardisremote("http://goo.gl/8GpxUV"),
        tardisschematic("http://goo.gl/BG4TtW"),
        tardisnetherportal("http://goo.gl/B2M36Y");

        final String URL;

        private ROOT_COMMAND(String URL) {
            this.URL = URL;
        }
    }
}
