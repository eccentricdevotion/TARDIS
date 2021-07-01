/*
 * Copyright (C) 2021 eccentric_nz
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
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISCubeCommand {

    private final TARDIS plugin;

    TARDISCubeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean whoHasCube(Player player) {
        // check they have TARDIS
        if (TARDISPermission.hasPermission(player, "tardis.find")) {
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TARDISMessage.send(player, "NO_TARDIS");
                return true;
            }
            int id = rs.getTardis_id();
            if (!plugin.getTrackerKeeper().getIsSiegeCube().contains(id)) {
                TARDISMessage.send(player, "SIEGE_NOT_SIEGED");
                return true;
            }
            // get the player who is carrying the Siege cube
            for (Map.Entry<UUID, Integer> map : plugin.getTrackerKeeper().getSiegeCarrying().entrySet()) {
                if (map.getValue() == id) {
                    String p = plugin.getServer().getPlayer(map.getKey()).getName();
                    TARDISMessage.send(player, "SIEGE_CARRIER", p);
                    return true;
                }
            }
            // not found
            TARDISMessage.send(player, "SIEGE_CARRIER", "no one!");
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
