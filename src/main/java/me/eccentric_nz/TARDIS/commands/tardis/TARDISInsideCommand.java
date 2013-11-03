/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISInsideCommand {

    private final TARDIS plugin;

    public TARDISInsideCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean whosInside(Player player, String[] args) {
        // check they are a timelord
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", player.getName());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            player.sendMessage(plugin.pluginName + "You must be the Timelord of a TARDIS to use this command!");
            return true;
        }
        int id = rs.getTardis_id();
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, true);
        if (rst.resultSet()) {
            List<String> data = rst.getData();
            player.sendMessage(plugin.pluginName + "The players inside your TARDIS are:");
            for (String s : data) {
                player.sendMessage(s);
            }
        } else {
            player.sendMessage(plugin.pluginName + "Nobody is inside your TARDIS.");
        }
        return true;
    }
}
