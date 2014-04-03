/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.rooms;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * There were at least fourteen bathrooms in the TARDIS, one of which had had a
 * leaky tap for three centuries. Because he had misplaced his washers, the
 * Doctor kept it from flooding the TARDIS by sealing it in a time loop that
 * made the same drop of water leak out over and over again.
 *
 * @author eccentric_nz
 */
public class TARDISRoomBuilder {

    private final TARDIS plugin;
    private final String r;
    private final Location l;
    private final COMPASS d;
    private final Player p;

    public TARDISRoomBuilder(TARDIS plugin, String r, Location l, COMPASS d, Player p) {
        this.plugin = plugin;
        this.r = r;
        this.l = l;
        this.d = d;
        this.p = p;
    }

    /**
     * Gets the required data to build a TARDIS room, then starts a repeating
     * task to build it.
     *
     * This needs to be set up to use the actual dimensions from the schematic
     * files, if user supplied room schematics are allowed to be used.
     *
     * @return true or false
     */
    public boolean build() {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", p.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("uuid", p.getUniqueId().toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            TARDISRoomData roomData = new TARDISRoomData();
            roomData.setTardis_id(rs.getTardis_id());
            // get middle data, default to orange wool if not set
            int middle_id, floor_id;
            byte middle_data, floor_data;
            if (rsp.resultSet()) {
                int[] wid_data = plugin.getTardisWalls().blocks.get(rsp.getWall());
                middle_id = wid_data[0];
                middle_data = (byte) wid_data[1];
                int[] fid_data = plugin.getTardisWalls().blocks.get(rsp.getFloor());
                floor_id = fid_data[0];
                floor_data = (byte) fid_data[1];
            } else {
                middle_id = 35;
                middle_data = 1;
                floor_id = 35;
                floor_data = 8;
            }
            roomData.setMiddle_id(middle_id);
            roomData.setMiddle_data(middle_data);
            roomData.setFloor_id(floor_id);
            roomData.setFloor_data(floor_data);
            // get start locations
            Block b = l.getBlock();
            roomData.setBlock(b);
            roomData.setDirection(d);
            short[] dimensions = plugin.getBuildKeeper().getRoomDimensions().get(r);
            int xzoffset = (dimensions[1] / 2);
            switch (d) {
                case NORTH:
                    l.setX(l.getX() - xzoffset);
                    l.setZ(l.getZ() - dimensions[1]);
                    break;
                case WEST:
                    l.setX(l.getX() - dimensions[1]);
                    l.setZ(l.getZ() - xzoffset);
                    break;
                case SOUTH:
                    l.setX(l.getX() - xzoffset);
                    break;
                default:
                    l.setZ(l.getZ() - xzoffset);
                    break;
            }
            // set y offset
            int offset = Math.abs(plugin.getRoomsConfig().getInt("rooms." + r + ".offset"));
            l.setY(l.getY() - offset);
            roomData.setLocation(l);
            roomData.setX(1);
            roomData.setZ(1);
            roomData.setRoom(r);
            roomData.setSchematic(plugin.getBuildKeeper().getRoomSchematics().get(r));
            roomData.setDimensions(dimensions);

            // determine how often to place a block (in ticks) - `room_speed` is the number of blocks to place in a second (20 ticks)
            long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
            TARDISRoomRunnable runnable = new TARDISRoomRunnable(plugin, roomData, p);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, delay);
            runnable.setTask(taskID);
            TARDISMessage.send(p, plugin.getPluginName() + "To cancel growing this room use the command /tardis abort " + taskID);
        }
        return true;
    }
}
