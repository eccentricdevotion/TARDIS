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
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TardisEjectCommand {

    private final TardisPlugin plugin;

    TardisEjectCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean eject(Player player) {
        if (!TardisPermission.hasPermission(player, "tardis.eject")) {
            TardisMessage.send(player, "NO_PERMS");
            return true;
        }
        // check they are still in the tardis world
        if (!plugin.getUtils().inTardisWorld(player)) {
            TardisMessage.send(player, "CMD_IN_WORLD");
            return true;
        }
        // must have a tardis
        ResultSetTardisId rs = new ResultSetTardisId(plugin);
        if (!rs.fromUuid(player.getUniqueId().toString())) {
            TardisMessage.send(player, "NOT_A_TIMELORD");
            return false;
        }
        int ownerid = rs.getTardisId();
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            TardisMessage.send(player, "NOT_IN_TARDIS");
            return false;
        }
        int thisid = rst.getTardisId();
        // must be timelord of the tardis
        if (thisid != ownerid) {
            TardisMessage.send(player, "CMD_ONLY_TL");
            return false;
        }
        // track the player
        plugin.getTrackerKeeper().getEjecting().put(player.getUniqueId(), thisid);
        return true;
    }
}
