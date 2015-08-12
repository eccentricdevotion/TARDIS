/*
 * Copyright (C) 2015 eccentric_nz
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
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISCubeCommand {

    private final TARDIS plugin;

    public TARDISCubeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean whoHasCube(Player player) {
        // check they have TARDIS
        if (player.hasPermission("tardis.find")) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NO_TARDIS");
                return true;
            }
            if (!plugin.getTrackerKeeper().getIsSiegeCube().contains(rs.getTardis_id())) {
                TARDISMessage.send(player, "SIEGE_NOT_SIEGED");
                return true;
            }
            // get the player who is carrying the Siege cube
            for (Map.Entry<UUID, Integer> map : plugin.getTrackerKeeper().getSiegeCarrying().entrySet()) {
                if (map.getValue() == rs.getTardis_id()) {
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
