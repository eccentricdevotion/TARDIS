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
package me.eccentric_nz.TARDIS.ARS;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomData;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomRunnable;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISARSRunnable implements Runnable {

    private final TARDIS plugin;
    private TARDISARSSlot slot;
    private TARDISARS room;
    private Player p;
    private int id;

    public TARDISARSRunnable(TARDIS plugin, TARDISARSSlot slot, TARDISARS room, Player p) {
        this.plugin = plugin;
        this.slot = slot;
        this.room = room;
        this.p = p;
    }

    @Override
    public void run() {
        String whichroom = room.toString();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", p.getName());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            String[] chunk_data = rs.getChunk().split(":");
            World w = plugin.getServer().getWorld(chunk_data[0]);
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
            Location l = new Location(w, slot.getX(), slot.getY(), slot.getZ());
            roomData.setDirection(TARDISConstants.COMPASS.SOUTH);
            short[] dimensions = plugin.room_dimensions.get(whichroom);
            // set y offset - this needs to be how many blocks above ground 0 of the 16x16x16 chunk the room starts
            l.setY(l.getY() + TARDISARS.valueOf(whichroom).getOffset());
            roomData.setLocation(l);
            roomData.setX(1);
            roomData.setZ(1);
            roomData.setRoom(whichroom);
            roomData.setSchematic(plugin.room_schematics.get(whichroom));
            roomData.setDimensions(dimensions);
            long delay = Math.round(20 / plugin.getConfig().getDouble("room_speed"));
            TARDISRoomRunnable runnable = new TARDISRoomRunnable(plugin, roomData, p);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, delay);
            runnable.setTask(taskID);
            // take their energy!
            int amount = plugin.getRoomsConfig().getInt("rooms." + whichroom + ".cost");
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("owner", p.getName());
            qf.alterEnergyLevel("tardis", -amount, set, p);
            p.sendMessage(plugin.pluginName + "To cancel growing this [" + whichroom + "] room use the command /tardis abort " + taskID);
        }
    }

    public void setId(int id) {
        this.id = id;
    }
}
