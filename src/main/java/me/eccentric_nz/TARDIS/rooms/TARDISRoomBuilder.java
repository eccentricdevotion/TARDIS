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
package me.eccentric_nz.TARDIS.rooms;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
    private String r;
    private Location l;
    private COMPASS d;
    private Player p;

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
        where.put("owner", p.getName());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("player", p.getName());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            TARDISRoomData roomData = new TARDISRoomData();
            roomData.setTardis_id(rs.getTardis_id());
            // get middle data, default to orange wool if not set
            int middle_id, floor_id;
            byte middle_data, floor_data;
            if (rsp.resultSet()) {
                TARDISWalls tw = new TARDISWalls();
                Integer[] wid_data = tw.blocks.get(rsp.getWall());
                middle_id = wid_data[0].intValue();
                middle_data = wid_data[1].byteValue();
                Integer[] fid_data = tw.blocks.get(rsp.getFloor());
                floor_id = fid_data[0].intValue();
                floor_data = fid_data[1].byteValue();
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
            short[] dimensions = plugin.room_dimensions.get(r);
            if (r.equalsIgnoreCase("GRAVITY") || r.equalsIgnoreCase("ANTIGRAVITY")) {
                l.setX(l.getX() - 6);
                l.setZ(l.getZ() - 6);
            } else {
                int xzoffset = (dimensions[1] / 2);
                switch (d) {
                    case NORTH:
                        l.setX(l.getX() + xzoffset);
                        break;
                    case WEST:
                        l.setZ(l.getZ() + xzoffset);
                        break;
                    case SOUTH:
                        l.setX(l.getX() - xzoffset);
                        break;
                    default:
                        l.setZ(l.getZ() - xzoffset);
                        break;
                }
            }
            // set y offset
            int offset = Math.abs(plugin.getRoomsConfig().getInt("rooms." + r + ".offset"));
            l.setY(l.getY() - offset);
            roomData.setLocation(l);
            if (d.equals(COMPASS.EAST) || d.equals(COMPASS.SOUTH) || r.equalsIgnoreCase("GRAVITY") || r.equalsIgnoreCase("ANTIGRAVITY")) {
                roomData.setX(1);
                roomData.setZ(1);
            } else {
                roomData.setX(-1);
                roomData.setZ(-1);
            }
            roomData.setRoom(r);
            String whichroom;
            if (r.equals("PASSAGE") || r.equals("LONG")) {
                whichroom = (d.equals(COMPASS.EAST) || d.equals(COMPASS.WEST)) ? r + "_EW" : r;
            } else {
                whichroom = r;
            }
            roomData.setSchematic(plugin.room_schematics.get(whichroom));
            roomData.setDimensions(dimensions);

            // set door space to air
            b.setTypeId(0);
            b.getRelative(BlockFace.UP).setTypeId(0);
            // build faster if in debug mode
            long delay = (plugin.getConfig().getBoolean("debug")) ? 2 : 5;
            TARDISRoomRunnable runnable = new TARDISRoomRunnable(plugin, roomData, p);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, delay);
            runnable.setTask(taskID);
            p.sendMessage(plugin.pluginName + "To cancel growing this room use the command /tardis abort " + taskID);
        }
        return true;
    }
}
