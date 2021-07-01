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
import me.eccentric_nz.tardis.database.resultset.ResultSetCount;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TardisRepairCommand {

    private final TardisPlugin plugin;

    TardisRepairCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean setFreeCount(CommandSender sender, String[] args) {
        if (args.length < 3) {
            TardisMessage.send(sender, "TOO_FEW_ARGS");
            return true;
        }
        // Look up this player's UUID
        OfflinePlayer op = TardisStaticUtils.getOfflinePlayer(args[1]);
        if (op == null) {
            TardisMessage.send(sender, "COULD_NOT_FIND_NAME");
            return true;
        }
        String uuid = op.getUniqueId().toString();
        ResultSetCount rs = new ResultSetCount(plugin, uuid);
        if (!rs.resultSet()) {
            TardisMessage.send(sender, "PLAYER_NO_Tardis");
            return true;
        }
        // set repair
        int r = 1;
        if (args.length == 3) {
            r = TardisNumberParsers.parseInt(args[2]);
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        HashMap<String, Object> set = new HashMap<>();
        set.put("repair", r);
        plugin.getQueryFactory().doUpdate("t_count", set, where);
        TardisMessage.send(sender, "REPAIR_SET", args[1], "" + r);
        return true;
    }
}
