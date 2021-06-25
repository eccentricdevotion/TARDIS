/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TardisArsRemoveCommand {

    private final TardisPlugin plugin;

    TardisArsRemoveCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean resetArs(Player player) {
        // check they are a Time Lord
        ResultSetTardisId rs = new ResultSetTardisId(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            TardisMessage.send(player, "NOT_A_TIMELORD");
            return true;
        }
        int id = rs.getTardisId();
        // get the sign location so we can reset the sign text
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("tardis_id", id);
        wheres.put("type", 10);
        ResultSetControls rsc = new ResultSetControls(plugin, wheres, false);
        if (rsc.resultSet()) {
            Location l = TardisStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
            if (l != null) {
                Block b = l.getBlock();
                if (Tag.SIGNS.isTagged(b.getType())) {
                    Sign sign = (Sign) b.getState();
                    for (int i = 0; i < 4; i++) {
                        sign.setLine(i, "");
                    }
                    sign.update();
                }
                HashMap<String, Object> del = new HashMap<>();
                del.put("tardis_id", id);
                del.put("type", 10);
                plugin.getQueryFactory().doDelete("controls", del);
                TardisMessage.send(player, "ARS_REMOVED");
            }
        } else {
            TardisMessage.send(player, "NO_ARS");
        }
        return true;
    }
}
