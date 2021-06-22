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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.move.TardisDoorToggler;
import org.bukkit.entity.Player;

public class TardisDoorCommand {

    private final TardisPlugin plugin;

    public TardisDoorCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean toggleDoors(Player player, String[] args) {
        if (!TardisPermission.hasPermission(player, "tardis.use")) {
            TardisMessage.send(player, "NO_PERMS");
            return true;
        }
        // must have a tardis
        ResultSetTardisId rs = new ResultSetTardisId(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            TardisMessage.send(player, "NOT_A_TIMELORD");
            return false;
        }
        if (args.length < 2) {
            TardisMessage.send(player, "TOO_FEW_ARGS");
            return false;
        }
        boolean open = (args[1].equalsIgnoreCase("close"));
        // toggle the door
        new TardisDoorToggler(plugin, player.getLocation().getBlock(), player, false, open, rs.getTardisId()).toggleDoors();
        return true;
    }
}
