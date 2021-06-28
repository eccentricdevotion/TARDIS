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
package me.eccentric_nz.tardis.commands;

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TardisCommandHelper {

    private final TardisPlugin plugin;
    private final List<String> notThese = Arrays.asList("aliases", "description", "usage", "permission", "permission-message");

    public TardisCommandHelper(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public void getCommand(String c, CommandSender sender) {
        if (c.isEmpty()) {
            sender.sendMessage("------");
            sender.sendMessage(ChatColor.GOLD + "TARDIS commands - use " + ChatColor.AQUA + "/tardis? <command> " + ChatColor.RESET + "for more info");
            sender.sendMessage(ChatColor.GRAY + "Online: " + ChatColor.RESET + "http://goo.gl/f8lWbP");
            Objects.requireNonNull(plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands")).getKeys(false).forEach((o) -> {
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
                    if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + "." + split[1].toLowerCase(Locale.ENGLISH))) {
                        sender.sendMessage("------");
                        sender.sendMessage("Command: " + ChatColor.GOLD + "/" + c.toLowerCase(Locale.ENGLISH));
                        sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + "." + split[1].toLowerCase(Locale.ENGLISH) + ".description"));
                        if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + "." + split[1].toLowerCase(Locale.ENGLISH) + ".usage")) {
                            sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.RESET + Objects.requireNonNull(plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + "." + split[1].toLowerCase(Locale.ENGLISH) + ".usage")).replace("<command>", root.toString()));
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
                    Set<String> args = Objects.requireNonNull(plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands." + root)).getKeys(false);
                    if (args.size() > 5) {
                        sender.sendMessage(ChatColor.GOLD + "/" + root + ChatColor.RESET + " commands - use " + ChatColor.AQUA + "/tardis? " + root + " <argument> " + ChatColor.RESET + "for more info");
                        sender.sendMessage(ChatColor.GRAY + "Online: " + ChatColor.RESET + root.URL);
                        sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".description"));
                        sender.sendMessage(ChatColor.GRAY + "Aliases: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".aliases"));
                        if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root + ".usage")) {
                            sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.RESET + Objects.requireNonNull(plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".usage")).replace("<command>", root.toString()));
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
                        sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.RESET + Objects.requireNonNull(plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".usage")).replace("<command>", root.toString()));
                        sender.sendMessage(ChatColor.GRAY + "Permission: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root + ".permission"));
                    }
                    sender.sendMessage("------");
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                sender.sendMessage("Invalid TARDIS help command argument: " + c);
            }
        }
    }

    public enum ROOT_COMMAND {

        tardis("https://goo.gl/55uTqL"),
        tardistravel("https://goo.gl/5rZR1T"),
        tardisadmin("https://goo.gl/jWFyLX"),
        tardisgive("https://goo.gl/LGQgy5"),
        tardisroom("https://goo.gl/zh9RKK"),
        tardisprefs("https://goo.gl/6k3RqD"),
        tardisarea("https://goo.gl/AJM2i3"),
        tardisartron("https://goo.gl/00ueX0"),
        tardisbind("https://goo.gl/sedpK4"),
        tardisgravity("https://goo.gl/vczqjf"),
        tardisbook("https://goo.gl/BGPh3t"),
        tardistexture("https://goo.gl/FPuxoa"),
        tardisrecipe("https://goo.gl/WSHA6N"),
        tardissay("https://goo.gl/iphcoM"),
        tardisremote("https://goo.gl/8GpxUV"),
        tardisschematic("https://goo.gl/BG4TtW"),
        tardisnetherportal("https://goo.gl/B2M36Y"),
        tardisjunk("https://goo.gl/xNcBd5"),
        handles("https://tardis.page.link/handles"),
        tardisteleport("Not yet available"),
        tardisworld("Not yet available"),
        tardisgamemode("Not yet available"),
        tardischemistry("Not yet available"),
        tardissudo("Not yet available"),
        tardistime("Not yet available"),
        tardisweather("Not yet available");

        final String URL;

        ROOT_COMMAND(String URL) {
            this.URL = URL;
        }
    }
}
