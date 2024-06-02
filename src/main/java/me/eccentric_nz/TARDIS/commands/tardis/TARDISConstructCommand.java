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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetConstructSign;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISConstructCommand {

    private final TARDIS plugin;
    private final List<String> lineNumbers = Arrays.asList("1", "2", "3", "4");

    TARDISConstructCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setLine(Player player, String[] args) {
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return true;
        }
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
            return true;
        }
        int id = rs.getTardisId();
        // must have a construct
        ResultSetConstructSign rscs = new ResultSetConstructSign(plugin, id);
        if (!rscs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_CONSTRUCT");
            return true;
        }
        // check line number
        if (!lineNumbers.contains(args[1])) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CONSTRUCT_LINE_NUM");
            return true;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<>();
        int l = TARDISNumberParsers.parseInt(args[1]);
        String raw = ChatColor.translateAlternateColorCodes('&', String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
        // strip color codes and check length
        if (ChatColor.stripColor(raw).length() > 16) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CONSTRUCT_LINE_LEN");
            return true;
        }
        set.put("line" + l, raw);
        // save it
        plugin.getQueryFactory().doUpdate("chameleon", set, where);
        plugin.getMessenger().send(player, TardisModule.TARDIS, "CONSTRUCT_LINE_SAVED");
        return true;
    }
}
