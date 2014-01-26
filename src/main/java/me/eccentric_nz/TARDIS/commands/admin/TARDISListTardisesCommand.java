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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISListTardisesCommand {

    private final TARDIS plugin;

    public TARDISListTardisesCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean listTardises(CommandSender sender, String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("save")) {
            ResultSetTardis rsl = new ResultSetTardis(plugin, null, "", true);
            if (rsl.resultSet()) {
                ArrayList<HashMap<String, String>> data = rsl.getData();
                String file = plugin.getDataFolder() + File.separator + "TARDIS_list.txt";
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
                    for (HashMap<String, String> map : data) {
                        HashMap<String, Object> wherecl = new HashMap<String, Object>();
                        wherecl.put("tardis_id", plugin.utils.parseInt(map.get("tardis_id")));
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                        if (!rsc.resultSet()) {
                            sender.sendMessage(plugin.pluginName + MESSAGE.NO_CURRENT.getText());
                            return true;
                        }
                        String line = "Timelord: " + map.get("owner") + ", Location: " + rsc.getWorld().getName() + ":" + rsc.getX() + ":" + rsc.getY() + ":" + rsc.getZ();
                        bw.write(line);
                        bw.newLine();
                    }
                    bw.close();
                } catch (IOException e) {
                    plugin.debug("Could not create and write to TARDIS_list.txt! " + e.getMessage());
                }
            }
            sender.sendMessage(plugin.pluginName + "File saved to 'plugins/TARDIS/TARDIS_list.txt'");
            return true;
        } else {
            // get all tardis positions - max 18
            int start = 0, end = 18;
            if (args.length > 1) {
                int tmp = plugin.utils.parseInt(args[1]);
                start = (tmp * 18) - 18;
                end = tmp * 18;
            }
            String limit = start + ", " + end;
            ResultSetTardis rsl = new ResultSetTardis(plugin, null, limit, true);
            if (rsl.resultSet()) {
                sender.sendMessage(plugin.pluginName + "TARDIS locations.");
                ArrayList<HashMap<String, String>> data = rsl.getData();
                for (HashMap<String, String> map : data) {
                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                    wherecl.put("tardis_id", plugin.utils.parseInt(map.get("tardis_id")));
                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                    if (!rsc.resultSet()) {
                        sender.sendMessage(plugin.pluginName + MESSAGE.NO_CURRENT.getText());
                        return true;
                    }
                    sender.sendMessage("Timelord: " + map.get("owner") + ", Location: " + rsc.getWorld().getName() + ":" + rsc.getX() + ":" + rsc.getY() + ":" + rsc.getZ());
                }
                sender.sendMessage(plugin.pluginName + "To see more locations, type: /tardisadmin list 2,  /tardisadmin list 3 etc.");
            } else {
                sender.sendMessage(plugin.pluginName + "There are no more records to display.");
            }
            return true;
        }
    }
}
