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
package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

public class TARDISAutonomousAreaCommand {

    private final TARDIS plugin;

    public TARDISAutonomousAreaCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean processArea(CommandSender sender, String[] args) {
        if (args.length < 3) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return true;
        }
        String area = args[1];
        // check the area exists
        HashMap<String, Object> where = new HashMap<>();
        where.put("area_name", area);
        ResultSetAreas rsa = new ResultSetAreas(plugin, where, false, true);
        if (!rsa.resultSet()) {
            plugin.getMessenger().sendColouredCommand(sender, "AREA_NOT_FOUND", "/tardis list areas", plugin);
            return true;
        }
        List<String> autoAreas = plugin.getConfig().getStringList("autonomous_areas");
        if (args[2].equalsIgnoreCase("add")) {
            if (autoAreas.contains(area)) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "AREA_ALREADY_ADDED", area);
                return true;
            }
            autoAreas.add(area);
        } else {
            // remove
            if (!autoAreas.contains(area)) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "AREA_NOT_IN_LIST", area);
                return true;
            }
            autoAreas.remove(area);
        }
        plugin.getConfig().set("autonomous_areas", autoAreas);
        plugin.saveConfig();
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "AREA_LIST_UPDATED");
        return true;
    }
}
