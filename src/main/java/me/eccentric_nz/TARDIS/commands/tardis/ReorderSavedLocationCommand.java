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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class ReorderSavedLocationCommand {

    private final TARDIS plugin;

    ReorderSavedLocationCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doReorderSave(Player player, String[] args) {
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
            int id = rs.getTardisId();
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("dest_name", args[1]);
            whered.put("tardis_id", id);
            ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
            if (!rsd.resultSet()) {
                plugin.getMessenger().sendColouredCommand(player, "SAVE_NOT_FOUND", "/tardis list saves", plugin);
                return false;
            }
            if (args[1].equalsIgnoreCase("home")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_REORDER");
                return false;
            }
            if (!TARDISNumberParsers.isNumber(args[2])) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_LAST_NUMBER");
                return false;
            }
            int slot = TARDISNumberParsers.parseInt(args[2]);
            // check slot is not occupied
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("tardis_id", id);
            wheres.put("slot", slot);
            ResultSetDestinations rss = new ResultSetDestinations(plugin, wheres, false);
            if (rss.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_SLOT", rss.getDest_name());
                return true;
            }
            int destID = rsd.getDest_id();
            HashMap<String, Object> did = new HashMap<>();
            did.put("dest_id", destID);
            HashMap<String, Object> set = new HashMap<>();
            set.put("slot", slot);
            plugin.getQueryFactory().doUpdate("destinations", set, did);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_REORDERED", args[2]);
            return true;
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }
}
