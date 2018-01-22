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
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISARSRemoveCommand {

    private final TARDIS plugin;

    public TARDISARSRemoveCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean resetARS(Player player) {
        // check they are a timelord
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            TARDISMessage.send(player, "NOT_A_TIMELORD");
            return true;
        }
        int id = rs.getTardis_id();
        // get the sign location so we can reset the sign text
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("tardis_id", id);
        wheres.put("type", 10);
        ResultSetControls rsc = new ResultSetControls(plugin, wheres, false);
        if (rsc.resultSet()) {
            Location l = plugin.getLocationUtils().getLocationFromBukkitString(rsc.getLocation());
            if (l != null) {
                Block b = l.getBlock();
                if (b.getType().equals(Material.WALL_SIGN) || b.getType().equals(Material.SIGN)) {
                    Sign sign = (Sign) b.getState();
                    for (int i = 0; i < 4; i++) {
                        sign.setLine(i, "");
                    }
                    sign.update();
                }
                HashMap<String, Object> del = new HashMap<>();
                del.put("tardis_id", id);
                del.put("type", 10);
                new QueryFactory(plugin).doDelete("controls", del);
                TARDISMessage.send(player, "ARS_REMOVED");
            }
            return true;
        } else {
            TARDISMessage.send(player, "NO_ARS");
            return true;
        }
    }
}
