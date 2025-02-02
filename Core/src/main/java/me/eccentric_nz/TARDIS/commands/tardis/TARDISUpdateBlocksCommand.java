/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayBlockRoomConverter;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TARDISUpdateBlocksCommand {

    private final TARDIS plugin;

    public TARDISUpdateBlocksCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean convert(Player player) {
        // find all console and room chunks and convert the item displays there
        // get players tardis_id
        ResultSetTardisID rst = new ResultSetTardisID(plugin);
        if (rst.fromUUID(player.getUniqueId().toString())) {
            int id = rst.getTardisId();
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetARS rsa = new ResultSetARS(plugin, where);
            if (rsa.resultSet()) {
                String[][][] json = TARDISARSMethods.getGridFromJSON(rsa.getJson());
                Chunk c = plugin.getLocationUtils().getTARDISChunk(id);
                for (int l = 0; l < 3; l++) {
                    for (int row = 0; row < 9; row++) {
                        for (int col = 0; col < 9; col++) {
                            if (!json[l][row][col].equalsIgnoreCase("STONE")) {
                                // get ARS slot
                                TARDISARSSlot slot = new TARDISARSSlot();
                                slot.setChunk(c);
                                slot.setY(l);
                                slot.setX(row);
                                slot.setZ(col);
                                TARDISDisplayBlockRoomConverter roomConverter = new TARDISDisplayBlockRoomConverter(plugin, player, slot);
                                int roomTaskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, roomConverter, 5, 1);
                                roomConverter.setTaskId(roomTaskId);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
