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
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.TardisInteriorPositioning;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TardisOccupyCommand {

    private final TardisPlugin plugin;

    TardisOccupyCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    boolean toggleOccupancy(Player player) {
        if (TardisPermission.hasPermission(player, "tardis.timetravel")) {
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            String occupied;
            if (rst.resultSet()) {
                // only if they're not in the tardis world
                if (!plugin.getUtils().inTardisWorld(player)) {
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("uuid", player.getUniqueId().toString());
                    plugin.getQueryFactory().doDelete("travellers", whered);
                    occupied = ChatColor.RED + plugin.getLanguage().getString("OCCUPY_OUT");
                } else {
                    TardisMessage.send(player, "OCCUPY_MUST_BE_OUT");
                    return true;
                }
            } else if (plugin.getUtils().inTardisWorld(player)) {
                ResultSetTardisId rsid = new ResultSetTardisId(plugin);
                // if TIPS determine tardis_id from player location
                if (plugin.getConfig().getBoolean("creation.default_world") && !player.hasPermission("tardis.create_world")) {
                    int slot = TardisInteriorPositioning.getTipsSlot(player.getLocation());
                    if (!rsid.fromTipsSlot(slot)) {
                        TardisMessage.send(player, "OCCUPY_MUST_BE_IN");
                        return false;
                    }
                } else if (!rsid.fromUuid(player.getUniqueId().toString())) {
                    TardisMessage.send(player, "NOT_A_TIMELORD");
                    return false;
                }
                int id = rsid.getTardisId();
                HashMap<String, Object> wherei = new HashMap<>();
                wherei.put("tardis_id", id);
                wherei.put("uuid", player.getUniqueId().toString());
                plugin.getQueryFactory().doInsert("travellers", wherei);
                occupied = ChatColor.GREEN + plugin.getLanguage().getString("OCCUPY_IN");
            } else {
                TardisMessage.send(player, "OCCUPY_MUST_BE_IN");
                return true;
            }
            TardisMessage.send(player, "OCCUPY_SET", occupied);
            return true;
        } else {
            TardisMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
