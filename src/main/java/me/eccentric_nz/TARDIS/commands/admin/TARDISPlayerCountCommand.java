/*
 * Copyright (C) 2026 eccentric_nz
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
class TARDISPlayerCountCommand {

    private final TARDIS plugin;

    TARDISPlayerCountCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean countPlayers(CommandSender sender, String[] args) {
        int max_count = plugin.getConfig().getInt("creation.count");
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        if (player.getName() == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NOT_VALID");
            return true;
        }
        String uuid = player.getUniqueId().toString();
        ResultSetCount rsc = new ResultSetCount(plugin, uuid);
        if (rsc.resultSet()) {
            if (args.length == 3) {
                // set count
                int count = TARDISNumberParsers.parseInt(args[2]);
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("count", count);
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("uuid", uuid);
                plugin.getQueryFactory().doUpdate("t_count", setc, wherec);
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COUNT_SET", args[1], count, max_count);
            } else {
                // display count
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COUNT_IS", args[1], rsc.getCount(), max_count);
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COUNT_NOT_FOUND");
        }
        return true;
    }
}
