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
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
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
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return true;
            }
            int id = rs.getTardisId();
            if (!plugin.getTrackerKeeper().getIsSiegeCube().contains(id)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NOT_SIEGED");
                return true;
            }
            // get the player who is carrying the Siege cube
            for (Map.Entry<UUID, Integer> map : plugin.getTrackerKeeper().getSiegeCarrying().entrySet()) {
                if (map.getValue() == id) {
                    String p = plugin.getServer().getPlayer(map.getKey()).getName();
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_CARRIER", p);
                    return true;
                }
            }
            // not found
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_CARRIER", "no one!");
            return true;
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }
}
