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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISOccupyCommand {

    private final TARDIS plugin;

    public TARDISOccupyCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggleOccupancy(Player player) {
        if (player.hasPermission("tardis.timetravel")) {
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                player.sendMessage(plugin.getPluginName() + MESSAGE.NOT_A_TIMELORD.getText());
                return false;
            }
            int id = rs.getTardis_id();
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            //wheret.put("tardis_id", id);
            wheret.put("player", player.getName());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            String occupied;
            QueryFactory qf = new QueryFactory(plugin);
            if (rst.resultSet()) {
                // only if they're not in the TARDIS world
                if (!plugin.getUtils().inTARDISWorld(player)) {
                    HashMap<String, Object> whered = new HashMap<String, Object>();
                    whered.put("player", player.getName());
                    qf.doDelete("travellers", whered);
                    occupied = ChatColor.RED + "UNOCCUPIED";
                } else {
                    player.sendMessage(plugin.getPluginName() + "You cannot set occupancy to UNOCCUPIED unless you are outside the TARDIS!");
                    return true;
                }
            } else {
                if (plugin.getUtils().inTARDISWorld(player)) {
                    HashMap<String, Object> wherei = new HashMap<String, Object>();
                    wherei.put("tardis_id", id);
                    wherei.put("player", player.getName());
                    qf.doInsert("travellers", wherei);
                    occupied = ChatColor.GREEN + "OCCUPIED";
                } else {
                    player.sendMessage(plugin.getPluginName() + "You cannot set occupancy to OCCUPIED unless you are inside the TARDIS!");
                    return true;
                }
            }
            player.sendMessage(plugin.getPluginName() + " TARDIS occupation was set to: " + occupied);
            return true;
        } else {
            player.sendMessage(plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
