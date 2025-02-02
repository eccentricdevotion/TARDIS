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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISEjectCommand {

    private final TARDIS plugin;

    TARDISEjectCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean eject(Player player) {
        if (!TARDISPermission.hasPermission(player, "tardis.eject")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return true;
        }
        // check they are still in the TARDIS world
        if (!plugin.getUtils().inTARDISWorld(player)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_IN_WORLD");
            return true;
        }
        // must have a TARDIS
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
            return false;
        }
        int ownerid = rs.getTardisId();
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
            return false;
        }
        int thisid = rst.getTardis_id();
        // must be timelord of the TARDIS
        if (thisid != ownerid) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_ONLY_TL");
            return false;
        }
        // track the player
        plugin.getTrackerKeeper().getEjecting().put(player.getUniqueId(), thisid);
        return true;
    }
}
