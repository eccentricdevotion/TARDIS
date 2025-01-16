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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCount;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISRepairCommand {

    private final TARDIS plugin;

    TARDISRepairCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setFreeCount(CommandSender sender, String[] args) {
        if (args.length < 3) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return true;
        }
        // Look up this player's UUID
        OfflinePlayer op = Bukkit.getOfflinePlayer(args[1]);
        if (op.getName() == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
            return true;
        }
        String uuid = op.getUniqueId().toString();
        ResultSetCount rs = new ResultSetCount(plugin, uuid);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NO_TARDIS");
            return true;
        }
        // set repair
        int r = 1;
        if (args.length == 3) {
            r = TARDISNumberParsers.parseInt(args[2]);
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        HashMap<String, Object> set = new HashMap<>();
        set.put("repair", r);
        plugin.getQueryFactory().doUpdate("t_count", set, where);
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "REPAIR_SET", args[1], "" + r);
        return true;
    }
}
