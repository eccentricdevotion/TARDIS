/*
 * Copyright (C) 2015 eccentric_nz
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
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
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
            for (String o : plugin.getGeneralKeeper().getPluginYAML().getConfigurationSection("commands").getKeys(false)) {
                if (!o.equals("tardis?")) {
                    sender.sendMessage("/" + o);
                }
            }
            sender.sendMessage("------");
        } else {
            String[] split = c.split(" ");
            try {
                ROOT_COMMAND root = ROOT_COMMAND.valueOf(split[0].toLowerCase());
                if (split.length > 1) {
                    if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root.toString() + "." + split[1].toLowerCase())) {
                        sender.sendMessage("------");
                        sender.sendMessage("Command: " + ChatColor.GOLD + "/" + c.toLowerCase());
                        sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + "." + split[1].toLowerCase() + ".description"));
                        sender.sendMessage(ChatColor.GRAY + "Usage: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + "." + split[1].toLowerCase() + ".usage").replace("<command>", root.toString()));
                        if (plugin.getGeneralKeeper().getPluginYAML().contains("commands." + root.toString() + "." + split[1].toLowerCase() + ".permission")) {
                            sender.sendMessage(ChatColor.GRAY + "Permission: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + "." + split[1].toLowerCase() + ".permission"));
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
                        sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + ".description"));
                        sender.sendMessage(ChatColor.GRAY + "Aliases: " + ChatColor.RESET + plugin.getGeneralKeeper().getPluginYAML().getString("commands." + root.toString() + ".aliases"));
                        for (String m : args) {
                            if (!notThese.contains(m)) {
                                sender.sendMessage("/" + c + " " + m);
                            }
                        }
                    } else {
                        sender.sendMessage("Command: " + ChatColor.GOLD + "/" + root.toString());
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

        tardis, tardistravel, tardisadmin, tardisgive, tardisroom, tardisprefs, tardisarea, tardisartron, tardisbind, tardisgravity, tardisbook, tardistexture, tardisrecipe, tardissay, tardisremote, tardisschematic, tardisnetherportal
    }
}
