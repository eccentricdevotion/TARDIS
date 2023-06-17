/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;

class TARDISSaveIconCommand {

    private final TARDIS plugin;

    TARDISSaveIconCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean changeIcon(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.save")) {
            if (args.length < 3) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                return false;
            }
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return false;
            }
            int id = rs.getTardis_id();
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("dest_name", args[1]);
            whered.put("tardis_id", id);
            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
            if (!rsd.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NOT_FOUND");
                return false;
            }
            Material material;
            try {
                material = Material.valueOf(args[2].toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "MATERIAL_NOT_VALID");
                return false;
            }
            int destID = rsd.getDest_id();
            HashMap<String, Object> did = new HashMap<>();
            did.put("dest_id", destID);
            HashMap<String, Object> set = new HashMap<>();
            set.put("icon", material.toString());
            plugin.getQueryFactory().doUpdate("destinations", set, did);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_ICON", material.toString());
            return true;
        }
        return true;
    }
}
