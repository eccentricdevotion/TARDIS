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
package me.eccentric_nz.tardis.commands.sudo;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class SudoAssemble {

    private final TardisPlugin plugin;

    SudoAssemble(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean restore(CommandSender sender, UUID uuid, String player) {
        // turn off dispersal for this player
        plugin.getTrackerKeeper().getDispersed().remove(uuid);
        // get players TARDIS
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
        return true;
    }
}
