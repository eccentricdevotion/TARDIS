/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISRemoveSavedLocationCommand {

    private final TARDIS plugin;

    TARDISRemoveSavedLocationCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doRemoveSave(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.save")) {
            if (args.length < 2) {
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
                plugin.getMessenger().sendColouredCommand(player, "SAVE_NOT_FOUND", "/tardis list saves", plugin);
                return false;
            }
            int destID = rsd.getDest_id();
            HashMap<String, Object> did = new HashMap<>();
            did.put("dest_id", destID);
            plugin.getQueryFactory().doDelete("destinations", did);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_DELETED", args[1]);
            return true;
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }
}
