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
package me.eccentric_nz.tardis.commands.admin;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TardisAssembleCommand {

    private final TardisPlugin plugin;

    TardisAssembleCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean assemble(CommandSender sender, String player) {
        if (player.equalsIgnoreCase("all")) {
            plugin.getTrackerKeeper().getDispersed().clear();
            plugin.getTrackerKeeper().getDispersedTardises().clear();
            TardisMessage.send(sender, "ASSEMBLE_ALL");
            return true;
        } else if (player.equalsIgnoreCase("list")) {
            plugin.getTrackerKeeper().getDispersedTardises().forEach((d) -> plugin.debug("tardis id: " + d));
            return true;
        } else {
            // turn off dispersal for this player
            Player p = plugin.getServer().getPlayer(player);
            if (p != null) {
                UUID uuid = p.getUniqueId();
                plugin.getTrackerKeeper().getDispersed().remove(uuid);
                // get player's TARDIS
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", uuid.toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                if (rs.resultSet()) {
                    Tardis tardis = rs.getTardis();
                    while (plugin.getTrackerKeeper().getDispersedTardises().contains(tardis.getTardisId())) {
                        plugin.getTrackerKeeper().getDispersedTardises().remove(tardis.getTardisId());
                    }
                    TardisMessage.send(sender, "ASSEMBLE_PLAYER", player);
                }
            } else {
                TardisMessage.send(sender, "COULD_NOT_FIND_NAME");
                return true;
            }
        }
        return true;
    }
}
