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
package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RoomRequiredLister {

    public static void listCondensables(TARDIS plugin, String name, Player player) {
        HashMap<String, Integer> blockTypes = plugin.getBuildKeeper().getRoomBlockCounts().get(name);
        boolean hasPrefs = false;
        String wall = "ORANGE_WOOL";
        String floor = "LIGHT_GRAY_WOOL";
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
        if (rsp.resultSet()) {
            hasPrefs = true;
            wall = rsp.getWall();
            floor = rsp.getFloor();
        }
        // get the TARDIS id
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            TARDISMessage.send(player, "CONDENSE_REQUIRE", name);
            HashMap<String, Integer> item_counts = new HashMap<>();
            for (Map.Entry<String, Integer> entry : blockTypes.entrySet()) {
                String bkey;
                if (hasPrefs && (entry.getKey().equals("ORANGE_WOOL") || entry.getKey().equals("LIGHT_GRAY_WOOL"))) {
                    bkey = (entry.getKey().equals("ORANGE_WOOL")) ? wall : floor;
                } else {
                    bkey = entry.getKey();
                }
                int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
                int amount = (tmp > 0) ? tmp : 1;
                if (item_counts.containsKey(bkey)) {
                    item_counts.put(bkey, item_counts.get(bkey) + amount);
                } else {
                    item_counts.put(bkey, amount);
                }
            }
            int total = 0;
            for (Map.Entry<String, Integer> map : item_counts.entrySet()) {
                // get the amount of this block that the player has condensed
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("tardis_id", rs.getTardis_id());
                wherec.put("block_data", map.getKey());
                ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec);
                int has = (rsc.resultSet()) ? rsc.getBlock_count() : 0;
                int required = map.getValue() - has;
                if (required > 0) {
                    String line = map.getKey() + ", " + required;
                    player.sendMessage(line);
                    total += required;
                }
            }
            if (total == 0) {
                TARDISMessage.send(player, "CONDENSE_NONE");
            }
            TARDISMessage.send(player, "ROOM_ENERGY", name, plugin.getRoomsConfig().getString("rooms." + name + ".cost"));
        } else {
            TARDISMessage.send(player, "ID_NOT_FOUND");
        }
    }
}
