/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISAssembleCommand {

    private final TARDIS plugin;

    TARDISAssembleCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean assemble(CommandSender sender, String player) {
        if (player.equalsIgnoreCase("all")) {
            plugin.getTrackerKeeper().getDispersed().clear();
            plugin.getTrackerKeeper().getDispersedTARDII().clear();
            TARDISMessage.send(sender, "ASSEMBLE_ALL");
            return true;
        } else if (player.equalsIgnoreCase("list")) {
            plugin.getTrackerKeeper().getDispersedTARDII().forEach((d) -> plugin.debug("TARDIS id: " + d));
            return true;
        } else {
            // turn off dispersal for this player
            Player p = plugin.getServer().getPlayer(player);
            if (p != null) {
                UUID uuid = p.getUniqueId();
                plugin.getTrackerKeeper().getDispersed().remove(uuid);
                // get players TARDIS
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                if (rs.resultSet()) {
                    Tardis tardis = rs.getTardis();
                    while (plugin.getTrackerKeeper().getDispersedTARDII().contains(tardis.getTardis_id())) {
                        plugin.getTrackerKeeper().getDispersedTARDII().remove(tardis.getTardis_id());
                    }
                    TARDISMessage.send(sender, "ASSEMBLE_PLAYER", player);
                }
            } else {
                TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                return true;
            }
        }
        return true;
    }
}
