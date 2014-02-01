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
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomData;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomRunnable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISARSRunnable implements Runnable {

    private final TARDIS plugin;
    private final TARDISARSSlot slot;
    private final TARDISARS room;
    private final Player p;
    private int id;
    private final int tardis_id;

    public TARDISARSRunnable(TARDIS plugin, TARDISARSSlot slot, TARDISARS room, Player p, int tardis_id) {
        this.plugin = plugin;
        this.slot = slot;
        this.room = room;
        this.p = p;
        this.tardis_id = tardis_id;
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
                int[] wid_data = plugin.tw.blocks.get(rsp.getWall());
                middle_id = wid_data[0];
                middle_data = (byte) wid_data[1];
                int[] fid_data = plugin.tw.blocks.get(rsp.getFloor());
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
            Location l = new Location(w, slot.getX(), slot.getY(), slot.getZ());
            roomData.setDirection(COMPASS.SOUTH);
            short[] dimensions = plugin.room_dimensions.get(whichroom);
            // set y offset - this needs to be how many blocks above ground 0 of the 16x16x16 chunk the room starts
            l.setY(l.getY() + TARDISARS.valueOf(whichroom).getOffset());
            roomData.setLocation(l);
            roomData.setX(1);
            roomData.setZ(1);
            roomData.setRoom(whichroom);
            roomData.setSchematic(plugin.room_schematics.get(whichroom));
            roomData.setDimensions(dimensions);
            long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
            TARDISRoomRunnable runnable = new TARDISRoomRunnable(plugin, roomData, p);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, delay);
            runnable.setTask(taskID);
            QueryFactory qf = new QueryFactory(plugin);
            // remove blocks from condenser table if rooms_require_blocks is true
            if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                HashMap<Integer, Integer> roomBlockCounts = getRoomBlockCounts(room.toString(), p.getName());
                for (Map.Entry<Integer, Integer> entry : roomBlockCounts.entrySet()) {
                    HashMap<String, Object> wherec = new HashMap<String, Object>();
                    wherec.put("tardis_id", tardis_id);
                    wherec.put("block_data", entry.getKey());
                    qf.alterCondenserBlockCount(entry.getValue(), wherec);
                }
            }
            // take their energy!
            int amount = plugin.getRoomsConfig().getInt("rooms." + whichroom + ".cost");
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("owner", p.getName());
            qf.alterEnergyLevel("tardis", -amount, set, p);
            if (p.isOnline()) {
                p.sendMessage(plugin.pluginName + "To cancel growing this [" + whichroom + "] room use the command /tardis abort " + taskID);
            }
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    private HashMap<Integer, Integer> getRoomBlockCounts(String room, String player) {
        HashMap<Integer, Integer> blockIDCount = new HashMap<Integer, Integer>();
        HashMap<String, Integer> roomBlocks = plugin.roomBlockCounts.get(room);
        String wall = "ORANGE_WOOL";
        String floor = "LIGHT_GREY_WOOL";
        HashMap<String, Object> wherepp = new HashMap<String, Object>();
        boolean hasPrefs = false;
        wherepp.put("player", player);
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
        if (rsp.resultSet()) {
            hasPrefs = true;
            wall = rsp.getWall();
            floor = rsp.getFloor();
        }
        for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
            String[] block_data = entry.getKey().split(":");
            int bid = plugin.utils.parseInt(block_data[0]);
            String mat;
            int bdata;
            if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                mat = (block_data[1].equals("1")) ? wall : floor;
                int[] iddata = plugin.tw.blocks.get(mat);
                bdata = iddata[0];
            } else {
                bdata = bid;
            }
            int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
            int required = (tmp > 0) ? tmp : 1;
            if (blockIDCount.containsKey(bdata)) {
                blockIDCount.put(bdata, (blockIDCount.get(bdata) + required));
            } else {
                blockIDCount.put(bdata, required);
            }
        }
        return blockIDCount;
    }
}
