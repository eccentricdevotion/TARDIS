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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class JettisonCommand {

    private final TARDIS plugin;

    public JettisonCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void startJettison(Player player, String arg) {
        if (TARDISPermission.hasPermission(player, "tardis.jettison")) {
            String room = arg.toUpperCase(Locale.ROOT);
            if (room.equals("GRAVITY") || room.equals("ANTIGRAVITY")) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "GRAVITY_INFO","/tardisgravity remove");
            }
            if (!plugin.getGeneralKeeper().getRoomArgs().contains(room)) {
                StringBuilder buf = new StringBuilder();
                plugin.getGeneralKeeper().getRoomArgs().forEach((rl) -> buf.append(rl).append(", "));
                String roomlist = buf.substring(0, buf.length() - 2);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_NOT_VALID", roomlist);
                return;
            }
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return;
            }
            int id = rs.getTardisId();
            // check they are in the tardis
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            wheret.put("tardis_id", id);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
                return;
            }
            plugin.getTrackerKeeper().getJettison().put(player.getUniqueId(), room);
            String seed = plugin.getArtronConfig().getString("jettison_seed");
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_JETT_INFO", seed, room, seed);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
        }
    }
}
