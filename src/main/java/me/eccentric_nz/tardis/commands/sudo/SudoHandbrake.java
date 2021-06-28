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
import me.eccentric_nz.tardis.commands.tardis.TardisHandbrakeCommand;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class SudoHandbrake {

    private final TardisPlugin plugin;

    SudoHandbrake(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean toggle(CommandSender sender, String[] args, UUID uuid) {
        if (args.length < 3) {
            TardisMessage.send(sender, "TOO_FEW_ARGS");
            return true;
        }
        ResultSetTardisId rs = new ResultSetTardisId(plugin);
        if (rs.fromUuid(uuid.toString())) {
            return new TardisHandbrakeCommand(plugin).toggle(null, rs.getTardisId(), args, true);
        }
        return true;
    }
}
