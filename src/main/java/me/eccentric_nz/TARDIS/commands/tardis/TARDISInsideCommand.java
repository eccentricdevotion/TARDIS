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
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TardisInsideCommand {

    private final TardisPlugin plugin;

    TardisInsideCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean whoIsInside(Player player) {
        // check they are a timelord
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            TardisMessage.send(player, "NOT_A_TIMELORD");
            return true;
        }
        int id = rs.getTardisId();
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, true);
        if (rst.resultSet()) {
            List<UUID> data = rst.getData();
            TardisMessage.send(player, "INSIDE_PLAYERS");
            data.forEach((s) -> {
                Player p = plugin.getServer().getPlayer(s);
                if (p != null) {
                    player.sendMessage(p.getDisplayName());
                }
            });
        } else {
            TardisMessage.send(player, "INSIDE");
        }
        return true;
    }
}
