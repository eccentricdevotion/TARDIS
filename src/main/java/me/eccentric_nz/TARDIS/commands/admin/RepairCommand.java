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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class RepairCommand {

    private final TARDIS plugin;

    public RepairCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setFreeCount(CommandSender sender, Player player, int count) {

        String uuid = player.getUniqueId().toString();
        ResultSetCount rs = new ResultSetCount(plugin, uuid);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NO_TARDIS");
            return true;
        }
        // set repair

        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        HashMap<String, Object> set = new HashMap<>();
        set.put("repair", count);
        plugin.getQueryFactory().doUpdate("t_count", set, where);
        plugin.getMessenger().send(sender, TardisModule.TARDIS, "REPAIR_SET", player.getName(), "" + count);
        return true;
    }
}
