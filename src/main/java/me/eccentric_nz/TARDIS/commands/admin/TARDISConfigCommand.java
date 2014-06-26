/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISConfigCommand {

    private final TARDIS plugin;
    private final List<String> sections = new ArrayList<String>();

    public TARDISConfigCommand(TARDIS plugin) {
        this.plugin = plugin;
        sections.add("storage");
        sections.add("creation");
        sections.add("police_box");
        sections.add("travel");
        sections.add("preferences");
        sections.add("allow");
        sections.add("growth");
    }

    public boolean showConfigOptions(CommandSender sender, String[] args) {
        String section = args[1].toLowerCase();
        TARDISMessage.send(sender, "CONFIG");
        if (sections.contains(section)) {
            sender.sendMessage(ChatColor.AQUA + section + ":" + ChatColor.RESET);
            Set<String> options = plugin.getConfig().getConfigurationSection(section).getKeys(false);
            for (String o : options) {
                if (o.equals("mysql")) {
                    sender.sendMessage("    " + ChatColor.GREEN + o + "mysql:");
                    sender.sendMessage("        " + ChatColor.GREEN + o + "url: " + plugin.getConfig().getString(section + ".mysql.url"));
                    sender.sendMessage("        " + ChatColor.GREEN + o + "user: " + plugin.getConfig().getString(section + ".mysql.user"));
                    sender.sendMessage("        " + ChatColor.GREEN + o + "password: " + plugin.getConfig().getString(section + ".mysql.password"));
                } else {
                    sender.sendMessage("    " + ChatColor.GREEN + o + ": " + plugin.getConfig().getString(section + "." + o));
                }
            }
            return true;
        }
        if (section.equals("worlds")) {
            sender.sendMessage(ChatColor.AQUA + section + ":" + ChatColor.RESET);
            Set<String> worldNames = plugin.getConfig().getConfigurationSection("worlds").getKeys(false);
            for (String wname : worldNames) {
                String enabled = plugin.getConfig().getString("worlds." + wname);
                sender.sendMessage("    " + ChatColor.GREEN + wname + ": " + ChatColor.RESET + enabled);
            }
            return true;
        }
        if (section.equals("rechargers")) {
            sender.sendMessage(ChatColor.AQUA + section + ":" + ChatColor.RESET);
            Set<String> chargerNames = plugin.getConfig().getConfigurationSection("rechargers").getKeys(false);
            for (String charname : chargerNames) {
                sender.sendMessage("    " + ChatColor.GREEN + charname + ":");
                sender.sendMessage("        world: " + plugin.getConfig().getString("rechargers." + charname + ".world"));
                sender.sendMessage("        x: " + plugin.getConfig().getString("rechargers." + charname + ".x"));
                sender.sendMessage("        y: " + plugin.getConfig().getString("rechargers." + charname + ".y"));
                sender.sendMessage("        z: " + plugin.getConfig().getString("rechargers." + charname + ".z"));
            }
            return true;
        }
        if (section.equals("rooms")) {
            sender.sendMessage(ChatColor.AQUA + section + ":" + ChatColor.RESET);
            Set<String> roomNames = plugin.getRoomsConfig().getConfigurationSection("rooms").getKeys(false);
            for (String r : roomNames) {
                sender.sendMessage("    " + ChatColor.GREEN + r + ":");
                sender.sendMessage("        enabled: " + plugin.getRoomsConfig().getString("rooms." + r + ".enabled"));
                sender.sendMessage("        cost: " + plugin.getRoomsConfig().getString("rooms." + r + ".cost"));
                sender.sendMessage("        offset: " + plugin.getRoomsConfig().getString("rooms." + r + ".offset"));
                sender.sendMessage("        seed: " + plugin.getRoomsConfig().getString("rooms." + r + ".seed"));
            }
            return true;
        }
        return false;
    }
}
