/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCount;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISRepairCommand {

    private final TARDIS plugin;

    public TARDISRepairCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setFreeCount(CommandSender sender, String[] args) {
        // Look up this player's UUID
        OfflinePlayer op = plugin.getServer().getOfflinePlayer(args[1]);
        if (op == null) {
            TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
            return true;
        }
        String uuid = op.getUniqueId().toString();
        ResultSetCount rs = new ResultSetCount(plugin, uuid);
        if (!rs.resultSet()) {
            TARDISMessage.send(sender, "PLAYER_NO_TARDIS");
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
        new QueryFactory(plugin).doUpdate("t_count", set, where);
        TARDISMessage.send(sender, "REPAIR_SET", args[1], args[2]);
        return true;
    }
}
