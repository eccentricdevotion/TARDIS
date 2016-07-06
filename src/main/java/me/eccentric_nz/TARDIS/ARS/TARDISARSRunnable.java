/*
 * Copyright (C) 2016 eccentric_nz
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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.JSON.JSONObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISRoomGrowEvent;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomData;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomRunnable;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls.Pair;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Builds rooms determined by the Architectural Reconfiguration System.
 *
 * @author eccentric_nz
 */
public class TARDISARSRunnable implements Runnable {

    private final TARDIS plugin;
    private final TARDISARSSlot slot;
    private final ARS room;
    private final Player p;
    private int id;
    private final int tardis_id;

    public TARDISARSRunnable(TARDIS plugin, TARDISARSSlot slot, ARS room, Player p, int tardis_id) {
        this.plugin = plugin;
        this.slot = slot;
        this.room = room;
        this.p = p;
        this.tardis_id = tardis_id;
    }

    @Override
    public void run() {
        String whichroom = room.getActualName();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", p.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            String[] chunk_data = tardis.getChunk().split(":");
            World w = plugin.getServer().getWorld(chunk_data[0]);
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("uuid", p.getUniqueId().toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            TARDISRoomData roomData = new TARDISRoomData();
            roomData.setTardis_id(tardis.getTardis_id());
            // get middle data, default to orange wool if not set
            Material wall_type, floor_type;
            byte wall_data, floor_data;
            if (rsp.resultSet()) {
                Pair wid_data = plugin.getTardisWalls().blocks.get(rsp.getWall());
                wall_type = wid_data.getType();
                wall_data = wid_data.getData();
                Pair fid_data = plugin.getTardisWalls().blocks.get(rsp.getFloor());
                floor_type = fid_data.getType();
                floor_data = fid_data.getData();
            } else {
                wall_type = Material.WOOL;
                wall_data = 1;
                floor_type = Material.WOOL;
                floor_data = 8;
            }
            roomData.setMiddleType(wall_type);
            roomData.setMiddleData(wall_data);
            roomData.setFloorType(floor_type);
            roomData.setFloorData(floor_data);
            // get start locations
            Location l = new Location(w, slot.getX(), slot.getY(), slot.getZ());
            roomData.setDirection(COMPASS.SOUTH);
            String directory = (plugin.getRoomsConfig().getBoolean("rooms." + whichroom + ".user")) ? "user_schematics" : "schematics";
            String path = plugin.getDataFolder() + File.separator + directory + File.separator + whichroom.toLowerCase() + ".tschm";
            // get JSON
            JSONObject obj = TARDISSchematicGZip.unzip(path);
            // set y offset - this needs to be how many blocks above ground 0 of the 16x16x16 chunk the room starts
            l.setY(l.getY() + room.getOffset());
            roomData.setLocation(l);
            roomData.setRoom(whichroom);
            roomData.setSchematic(obj);
            long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
            plugin.getPM().callEvent(new TARDISRoomGrowEvent(p, tardis, slot, roomData));
            TARDISRoomRunnable runnable = new TARDISRoomRunnable(plugin, roomData, p);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, delay);
            runnable.setTask(taskID);
            QueryFactory qf = new QueryFactory(plugin);
            // remove blocks from condenser table if rooms_require_blocks is true
            if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                HashMap<String, Integer> roomBlockCounts = getRoomBlockCounts(whichroom, p.getUniqueId().toString());
                for (Map.Entry<String, Integer> entry : roomBlockCounts.entrySet()) {
                    HashMap<String, Object> wherec = new HashMap<String, Object>();
                    wherec.put("tardis_id", tardis_id);
                    wherec.put("block_data", entry.getKey());
                    qf.alterCondenserBlockCount(entry.getValue(), wherec);
                }
            }
            // take their energy!
            int amount = plugin.getRoomsConfig().getInt("rooms." + whichroom + ".cost");
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("uuid", p.getUniqueId().toString());
            qf.alterEnergyLevel("tardis", -amount, set, p);
            if (p.isOnline()) {
                TARDISMessage.send(p, "ARS_CANCEL", whichroom, String.format("%d", taskID));
            }
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    private HashMap<String, Integer> getRoomBlockCounts(String room, String uuid) {
        HashMap<String, Integer> blockIDCount = new HashMap<String, Integer>();
        HashMap<String, Integer> roomBlocks = plugin.getBuildKeeper().getRoomBlockCounts().get(room);
        String wall = "ORANGE_WOOL";
        String floor = "LIGHT_GREY_WOOL";
        HashMap<String, Object> wherepp = new HashMap<String, Object>();
        boolean hasPrefs = false;
        wherepp.put("uuid", uuid);
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
        if (rsp.resultSet()) {
            hasPrefs = true;
            wall = rsp.getWall();
            floor = rsp.getFloor();
        }
        for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
            String[] block_data = entry.getKey().split(":");
            String bid = block_data[0];
            String mat;
            String bdata;
            if (hasPrefs && block_data.length == 2 && (block_data[1].equals("1") || block_data[1].equals("8"))) {
                mat = (block_data[1].equals("1")) ? wall : floor;
                Pair iddata = plugin.getTardisWalls().blocks.get(mat);
                bdata = iddata.getType().toString();
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
