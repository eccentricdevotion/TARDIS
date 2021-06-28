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
package me.eccentric_nz.tardis.commands.handles;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;

class TardisHandlesRemoveCommand {

    private final TardisPlugin plugin;

    TardisHandlesRemoveCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean purge(Player player) {
        ResultSetTardisId rs = new ResultSetTardisId(plugin);
        if (rs.fromUuid(player.getUniqueId().toString())) {
            HashMap<String, Object> whereh = new HashMap<>();
            whereh.put("tardis_id", rs.getTardisId());
            whereh.put("type", 26);
            plugin.getQueryFactory().doDelete("controls", whereh);
            TardisMessage.send(player, "HANDLES_DELETED");
        } else {
            TardisMessage.send(player, "NOT_A_TIMELORD");
        }
        return true;
    }
}
