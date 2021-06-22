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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
class TardisJettisonCommand {

    private final TardisPlugin plugin;

    TardisJettisonCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean startJettison(Player player, String[] args) {
        if (TardisPermission.hasPermission(player, "tardis.jettison")) {
            if (args.length < 2) {
                TardisMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            String room = args[1].toUpperCase(Locale.ENGLISH);
            if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                TardisMessage.send(player, "GRAVITY_INFO", ChatColor.AQUA + "/tardisgravity remove" + ChatColor.RESET);
            }
            if (!plugin.getGeneralKeeper().getRoomArgs().contains(room)) {
                StringBuilder buf = new StringBuilder(args[1]);
                plugin.getGeneralKeeper().getRoomArgs().forEach((rl) -> buf.append(rl).append(", "));
                String roomList = buf.substring(0, buf.length() - 2);
                TardisMessage.send(player, "ROOM_NOT_VALID", roomList);
                return true;
            }
            ResultSetTardisId rs = new ResultSetTardisId(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TardisMessage.send(player, "NOT_A_TIMELORD");
                return true;
            }
            int id = rs.getTardisId();
            // check they are in the tardis
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            wheret.put("tardis_id", id);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                TardisMessage.send(player, "NOT_IN_TARDIS");
                return true;
            }
            plugin.getTrackerKeeper().getJettison().put(player.getUniqueId(), room);
            String seed = plugin.getArtronConfig().getString("jettison_seed");
            TardisMessage.send(player, "ROOM_JETT_INFO", seed, room, seed);
            return true;
        } else {
            TardisMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
