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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISListCommand {

    private final TARDIS plugin;

    TARDISListCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean listStuff(CommandSender sender, String[] args) {
        if (args.length > 1 && (args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("portals") || args[1].equalsIgnoreCase("abandoned"))) {
            if (args[1].equalsIgnoreCase("save")) {
                HashMap<String, Object> where = new HashMap<>();
                where.put("abandoned", 1);
                ResultSetTardis rsl = new ResultSetTardis(plugin, where, "", true);
                if (rsl.resultSet()) {
                    String file = plugin.getDataFolder() + File.separator + "TARDIS_list.txt";
                    try {
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                            for (Tardis tardis : rsl.getData()) {
                                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, tardis.getTardisId());
                                if (!rsc.resultSet()) {
                                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                                    return true;
                                }
                                Current current = rsc.getCurrent();
                                String line = "ID: " + tardis.getTardisId() + ", Time Lord: " + tardis.getOwner() + ", Location: " + current.location().getWorld().getName() + ":" + current.location().getBlockX() + ":" + current.location().getBlockY() + ":" + current.location().getBlockZ();
                                bw.write(line);
                                bw.newLine();
                            }
                        }
                    } catch (IOException e) {
                        plugin.debug("Could not create and write to TARDIS_list.txt! " + e.getMessage());
                    }
                }
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "FILE_SAVED");
                return true;
            } else if (args[1].equalsIgnoreCase("portals")) {
                plugin.getTrackerKeeper().getPortals().forEach((key, value) -> sender.sendMessage("TARDIS id: " + value.getTardisId() + " has a portal open at: " + key.toString()));
                return true;
            } else if (args[1].equalsIgnoreCase("abandoned")) { // abandoned
                new TARDISAbandonLister(plugin).list(sender);
                return true;
            }
        } else if (args.length > 2 && args[1].equalsIgnoreCase("blueprints")) {
            new TARDISBlueprintLister(plugin).list(sender, args[2]);
            return true;
        } else {
            // get all tardis positions - max 18
            int start = 0, end = 18;
            if (args.length > 1) {
                int tmp = TARDISNumberParsers.parseInt(args[1]);
                start = (tmp * 18) - 18;
                end = tmp * 18;
            }
            String limit = start + ", " + end;
            HashMap<String, Object> where = new HashMap<>();
            where.put("abandoned", 0);
            ResultSetTardis rsl = new ResultSetTardis(plugin, where, limit, true);
            if (rsl.resultSet()) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TARDIS_LOCS");
                if (sender instanceof Player) {
                    plugin.getMessenger().message(sender, "Hover to see location (world x, y, z)");
                    plugin.getMessenger().message(sender, "Click to enter the TARDIS");
                }
                plugin.getMessenger().message(sender, "");
                for (Tardis tardis : rsl.getData()) {
                    ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, tardis.getTardisId());
                    if (!rsc.resultSet()) {
                        plugin.getMessenger().message(sender, ChatColor.GREEN + "" + tardis.getTardisId() + " " + tardis.getOwner() + ChatColor.RESET + " TARDIS is in an unloaded world!");
                        continue;
                    }
                    Current current = rsc.getCurrent();
                    String world = (!plugin.getPlanetsConfig().getBoolean("planets." + current.location().getWorld().getName() + ".enabled") && plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) ? plugin.getMVHelper().getAlias(current.location().getWorld()) : TARDISAliasResolver.getWorldAlias(current.location().getWorld());
                    plugin.getMessenger().sendTARDISForList(sender, tardis, world, current.location().getBlockX(), current.location().getBlockY(), current.location().getBlockZ());
                }
                if (rsl.getData().size() > 18) {
                    plugin.getMessenger().sendColouredCommand(sender, "TARDIS_LOCS_INFO", "/tardisadmin list 2", plugin);
                }
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TARDIS_LOCS_NONE");
            }
            return true;
        }
        return false;
    }
}
