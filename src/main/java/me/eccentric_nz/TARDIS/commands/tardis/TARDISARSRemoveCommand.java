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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISARSRemoveCommand {

    private final TARDIS plugin;

    TARDISARSRemoveCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean resetARS(Player player) {
        // check they are a timelord
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
            return true;
        }
        int id = rs.getTardisId();
        // get the sign location so we can reset the sign text
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("tardis_id", id);
        wheres.put("type", 10);
        ResultSetControls rsc = new ResultSetControls(plugin, wheres, false);
        if (!rsc.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_ARS");
            return true;
        }
        Location l = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
        if (l != null) {
            Block b = l.getBlock();
            if (Tag.SIGNS.isTagged(b.getType())) {
                Sign sign = (Sign) b.getState();
                SignSide front = sign.getSide(Side.FRONT);
                for (int i = 0; i < 4; i++) {
                    front.setLine(i, "");
                }
                sign.update();
            }
            HashMap<String, Object> del = new HashMap<>();
            del.put("tardis_id", id);
            del.put("type", 10);
            plugin.getQueryFactory().doDelete("controls", del);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_REMOVED");
        }
        return true;
    }
}
