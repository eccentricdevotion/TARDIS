/*
 * Copyright (C) 2016 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            String occupied;
            QueryFactory qf = new QueryFactory(plugin);
            if (rst.resultSet()) {
                // only if they're not in the TARDIS world
                if (!plugin.getUtils().inTARDISWorld(player)) {
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("uuid", player.getUniqueId().toString());
                    qf.doDelete("travellers", whered);
                    occupied = ChatColor.RED + plugin.getLanguage().getString("OCCUPY_OUT");
                } else {
                    TARDISMessage.send(player, "OCCUPY_MUST_BE_OUT");
                    return true;
                }
            } else if (plugin.getUtils().inTARDISWorld(player)) {
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (!rs.fromUUID(player.getUniqueId().toString())) {
                    TARDISMessage.send(player, "NOT_A_TIMELORD");
                    return false;
                }
                int id = rs.getTardis_id();
                HashMap<String, Object> wherei = new HashMap<>();
                wherei.put("tardis_id", id);
                wherei.put("uuid", player.getUniqueId().toString());
                qf.doInsert("travellers", wherei);
                occupied = ChatColor.GREEN + plugin.getLanguage().getString("OCCUPY_IN");
            } else {
                TARDISMessage.send(player, "OCCUPY_MUST_BE_IN");
                return true;
            }
            TARDISMessage.send(player, "OCCUPY_SET", occupied);
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
