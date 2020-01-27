/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.command.CommandSender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISListTardisesCommand {

    private final TARDIS plugin;

    TARDISListTardisesCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean listTardises(CommandSender sender, String[] args) {
        if (args.length > 1 && (args[1].equalsIgnoreCase("save") || args[1].equalsIgnoreCase("portals") || args[1].equalsIgnoreCase("abandoned"))) {
            if (args[1].equalsIgnoreCase("save")) {
                ResultSetTardis rsl = new ResultSetTardis(plugin, new HashMap<>(), "", true, 1);
                if (rsl.resultSet()) {
                    String file = plugin.getDataFolder() + File.separator + "TARDIS_list.txt";
                    try {
                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                            for (Tardis t : rsl.getData()) {
                                HashMap<String, Object> wherecl = new HashMap<>();
                                wherecl.put("tardis_id", t.getTardis_id());
                                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                if (!rsc.resultSet()) {
                                    TARDISMessage.send(sender, "CURRENT_NOT_FOUND");
                                    return true;
                                }
                                String line = "ID: " + t.getTardis_id() + ", Time Lord: " + t.getOwner() + ", Location: " + rsc.getWorld().getName() + ":" + rsc.getX() + ":" + rsc.getY() + ":" + rsc.getZ();
                                bw.write(line);
                                bw.newLine();
                            }
                        }
                    } catch (IOException e) {
                        plugin.debug("Could not create and write to TARDIS_list.txt! " + e.getMessage());
                    }
                }
                TARDISMessage.send(sender, "FILE_SAVED");
                return true;
            } else if (args[1].equalsIgnoreCase("portals")) {
                plugin.getTrackerKeeper().getPortals().forEach((key, value) -> sender.sendMessage("TARDIS id: " + value.getTardisId() + " has a portal open at: " + key.toString()));
                return true;
            } else {
                new TARDISAbandonLister(plugin).list(sender);
                return true;
            }
        } else {
            // get all tardis positions - max 18
            int start = 0, end = 18;
            if (args.length > 1) {
                int tmp = TARDISNumberParsers.parseInt(args[1]);
                start = (tmp * 18) - 18;
                end = tmp * 18;
            }
            String limit = start + ", " + end;
            ResultSetTardis rsl = new ResultSetTardis(plugin, new HashMap<>(), limit, true, 0);
            if (rsl.resultSet()) {
                TARDISMessage.send(sender, "TARDIS_LOCS");
                for (Tardis t : rsl.getData()) {
                    HashMap<String, Object> wherecl = new HashMap<>();
                    wherecl.put("tardis_id", t.getTardis_id());
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (!rsc.resultSet()) {
                        TARDISMessage.send(sender, "CURRENT_NOT_FOUND");
                        return true;
                    }
                    sender.sendMessage("ID: " + t.getTardis_id() + ", Time Lord: " + t.getOwner() + ", Location: " + rsc.getWorld().getName() + ":" + rsc.getX() + ":" + rsc.getY() + ":" + rsc.getZ());
                }
                TARDISMessage.send(sender, "TARDIS_LOCS_INFO");
            } else {
                TARDISMessage.send(sender, "TARDIS_LOCS_NONE");
            }
            return true;
        }
    }
}
