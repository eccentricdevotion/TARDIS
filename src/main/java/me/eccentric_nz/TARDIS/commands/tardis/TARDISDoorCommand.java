/*
 * Copyright (C) 2024 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.doors.TARDISDoorToggler;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

public class TARDISDoorCommand {

    private final TARDIS plugin;

    public TARDISDoorCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggleDoors(Player player, String[] args) {
        if (!TARDISPermission.hasPermission(player, "tardis.use")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return true;
        }
        // must have a TARDIS
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
            return false;
        }
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return false;
        }
        boolean open = (args[1].equalsIgnoreCase("close"));
        // toggle the door
        new TARDISDoorToggler(plugin, player.getLocation().getBlock(), player, false, open, rs.getTardisId()).toggleDoors();
        return true;
    }
}
