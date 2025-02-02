/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.control.TARDISAtmosphericExcitation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISExciteCommand {

    private final TARDIS plugin;

    TARDISExciteCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean excite(Player player) {
        if (plugin.getTrackerKeeper().getExcitation().contains(player.getUniqueId())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_EXCITE");
            return true;
        }
        // get TARDIS id
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            new TARDISAtmosphericExcitation(plugin).excite(rs.getTardisId(), player);
            plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
            return true;
        }
        return true;
    }
}
