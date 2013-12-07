/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConfigCommand {

    private final TARDIS plugin;

    public TARDISConfigCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean showConfigOptions(CommandSender sender, String[] args) {
        if (args.length == 1) {
            Set<String> configNames = plugin.getConfig().getKeys(false);
            sender.sendMessage(plugin.pluginName + ChatColor.RED + " Here are the current plugin config options!");
            for (String cname : configNames) {
                String value = plugin.getConfig().getString(cname);
                if (!cname.equals("worlds") && !cname.equals("rooms") && !cname.equals("rechargers")) {
                    sender.sendMessage(ChatColor.AQUA + cname + ": " + ChatColor.RESET + value);
                }
            }
            return true;
        }
        if (args.length == 2) {
            Set<String> configNames = plugin.getConfig().getKeys(false);
            sender.sendMessage(plugin.pluginName + ChatColor.RED + " Here are the current plugin " + args[1] + " options!");
            for (String cname : configNames) {
                if (cname.equals("worlds") || cname.equals("rooms") || cname.equals("rechargers")) {
                    if (cname.equals("worlds") && args[1].equalsIgnoreCase("worlds")) {
                        sender.sendMessage(ChatColor.AQUA + cname + ":" + ChatColor.RESET);
                        Set<String> worldNames = plugin.getConfig().getConfigurationSection("worlds").getKeys(false);
                        for (String wname : worldNames) {
                            String enabled = plugin.getConfig().getString("worlds." + wname);
                            sender.sendMessage("      " + ChatColor.GREEN + wname + ": " + ChatColor.RESET + enabled);
                        }
                    }
                    if (cname.equals("rooms") && args[1].equalsIgnoreCase("rooms")) {
                        sender.sendMessage(ChatColor.AQUA + cname + ":" + ChatColor.RESET);
                        Set<String> roomNames = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
                        for (String r : roomNames) {
                            sender.sendMessage("      " + ChatColor.GREEN + r + ":");
                            sender.sendMessage("            enabled: " + plugin.getRoomsConfig().getString("rooms." + r + ".enabled"));
                            sender.sendMessage("            cost: " + plugin.getRoomsConfig().getString("rooms." + r + ".cost"));
                            sender.sendMessage("            offset: " + plugin.getRoomsConfig().getString("rooms." + r + ".offset"));
                            sender.sendMessage("            seed: " + plugin.getRoomsConfig().getString("rooms." + r + ".seed"));
                        }
                    }
                    if (cname.equals("rechargers") && args[1].equalsIgnoreCase("rechargers")) {
                        sender.sendMessage(ChatColor.AQUA + cname + ":" + ChatColor.RESET);
                        Set<String> chargerNames = plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
                        for (String charname : chargerNames) {
                            sender.sendMessage("      " + ChatColor.GREEN + charname + ":");
                            sender.sendMessage("            world: " + plugin.getConfig().getString("rechargers." + charname + ".world"));
                            sender.sendMessage("            x: " + plugin.getConfig().getString("rechargers." + charname + ".x"));
                            sender.sendMessage("            y: " + plugin.getConfig().getString("rechargers." + charname + ".y"));
                            sender.sendMessage("            z: " + plugin.getConfig().getString("rechargers." + charname + ".z"));
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
