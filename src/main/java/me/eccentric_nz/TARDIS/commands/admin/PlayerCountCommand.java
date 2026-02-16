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
public class PlayerCountCommand {

    private final TARDIS plugin;

    public PlayerCountCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean countPlayers(CommandSender sender, Player player, int count) {
        int max_count = plugin.getConfig().getInt("creation.count");
        String uuid = player.getUniqueId().toString();
        ResultSetCount rsc = new ResultSetCount(plugin, uuid);
        if (rsc.resultSet()) {
            if (count != -1) {
                // set count
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("count", count);
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("uuid", uuid);
                plugin.getQueryFactory().doUpdate("t_count", setc, wherec);
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COUNT_SET", player.getName(), count, max_count);
            } else {
                // display count
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COUNT_IS", player.getName(), rsc.getCount(), max_count);
            }
        } else {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "COUNT_NOT_FOUND");
        }
        return true;
    }
}
