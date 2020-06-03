/*
 * Copyright (C) 2020 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.update;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.UPDATEABLE;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class UpdateDoor {

    private final TARDIS plugin;

    public UpdateDoor(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(UPDATEABLE updateable, Block block, boolean secondary, int id, Player player) {
        HashMap<String, Object> set = new HashMap<>();
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        String location = block.getWorld().getName() + ":" + block.getX() + ":" + block.getY() + ":" + block.getZ();

        if (updateable.equals(UPDATEABLE.DOOR) && !secondary) {
            // if portals are on, remove the current portal first
            if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
                ResultSetDoorBlocks rsdb = new ResultSetDoorBlocks(plugin, id);
                if (rsdb.resultSet()) {
                    plugin.getTrackerKeeper().getPortals().remove(rsdb.getInnerBlock().getLocation());
                }
            }
            // get door data this should let us determine the direction
            Directional d = (Directional) block.getBlockData();
            set.put("door_location", location);
            set.put("door_direction", d.getFacing().toString());
            where.put("door_type", 1);
        }
        if ((updateable.equals(UPDATEABLE.BACKDOOR) || (updateable.equals(UPDATEABLE.DOOR) && secondary))) {
            // get player direction
            String d = TARDISStaticUtils.getPlayersDirection(player, true);
            set.put("door_location", location);
            set.put("door_direction", d);
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("tardis_id", id);
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            int type;
            if (rst.resultSet()) {
                type = (secondary) ? 4 : 3;
                // check the world
                if (!plugin.getUtils().inTARDISWorld(player)) {
                    TARDISMessage.send(player, "NOT_IN_TARDIS");
                    return;
                }
            } else {
                type = 2;
                if (plugin.getUtils().inTARDISWorld(player)) {
                    TARDISMessage.send(player, "TARDIS_OUTSIDE");
                    return;
                }
            }
            where.put("door_type", type);
            // check if we have a backdoor yet
            HashMap<String, Object> whered = new HashMap<>();
            whered.put("tardis_id", id);
            whered.put("door_type", type);
            ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
            if (!rsd.resultSet()) {
                // insert record
                HashMap<String, Object> setd = new HashMap<>();
                setd.put("tardis_id", id);
                setd.put("door_type", type);
                setd.put("door_location", location);
                setd.put("door_direction", d);
                plugin.getQueryFactory().doInsert("doors", setd);
            }
        }
        if (!secondary) {
            plugin.getQueryFactory().doUpdate("doors", set, where);
        }
    }
}
