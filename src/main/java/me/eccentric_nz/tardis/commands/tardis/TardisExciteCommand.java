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
import me.eccentric_nz.tardis.control.TardisAtmosphericExcitation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TardisExciteCommand {

    private final TardisPlugin plugin;

    TardisExciteCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean excite(Player player) {
        if (plugin.getTrackerKeeper().getExcitation().contains(player.getUniqueId())) {
            TardisMessage.send(player, "CMD_EXCITE");
            return true;
        }
        // get TARDIS id
        ResultSetTardisId rs = new ResultSetTardisId(plugin);
        if (rs.fromUuid(player.getUniqueId().toString())) {
            new TardisAtmosphericExcitation(plugin).excite(rs.getTardisId(), player);
            plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
            return true;
        }
        return true;
    }
}
