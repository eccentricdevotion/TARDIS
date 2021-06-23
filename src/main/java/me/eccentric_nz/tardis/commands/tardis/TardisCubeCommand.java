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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TardisCubeCommand {

    private final TardisPlugin plugin;

    TardisCubeCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean whoHasCube(Player player) {
        // check they have tardis
        if (TardisPermission.hasPermission(player, "tardis.find")) {
            ResultSetTardisId rs = new ResultSetTardisId(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TardisMessage.send(player, "NO_TARDIS");
                return true;
            }
            int id = rs.getTardisId();
            if (!plugin.getTrackerKeeper().getIsSiegeCube().contains(id)) {
                TardisMessage.send(player, "SIEGE_NOT_SIEGED");
                return true;
            }
            // get the player who is carrying the Siege cube
            for (Map.Entry<UUID, Integer> map : plugin.getTrackerKeeper().getSiegeCarrying().entrySet()) {
                if (map.getValue() == id) {
                    String p = Objects.requireNonNull(plugin.getServer().getPlayer(map.getKey())).getName();
                    TardisMessage.send(player, "SIEGE_CARRIER", p);
                    return true;
                }
            }
            // not found
            TardisMessage.send(player, "SIEGE_CARRIER", "no one!");
            return true;
        } else {
            TardisMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
