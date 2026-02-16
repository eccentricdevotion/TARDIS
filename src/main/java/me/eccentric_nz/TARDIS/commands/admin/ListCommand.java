/*
 * Copyright (C) 2026 eccentric_nz
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
import org.bukkit.command.CommandSender;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class ListCommand {

    private final TARDIS plugin;

    public ListCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean listStuff(CommandSender sender, String what) {
        if (what.equalsIgnoreCase("save") || what.equalsIgnoreCase("portals") || what.equalsIgnoreCase("abandoned")) {
            if (what.equalsIgnoreCase("save")) {
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
            } else if (what.equalsIgnoreCase("portals")) {
                plugin.getTrackerKeeper().getPortals().forEach((key, value) -> sender.sendMessage("TARDIS id: " + value.getTardisId() + " has a portal open at: " + key.toString()));
                return true;
            } else if (what.equalsIgnoreCase("abandoned")) { // abandoned
                new AbandonedLister(plugin).list(sender);
                return true;
            }
        }
        return false;
    }
}
