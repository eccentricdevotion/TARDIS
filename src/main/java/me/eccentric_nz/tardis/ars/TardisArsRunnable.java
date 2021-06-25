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
package me.eccentric_nz.tardis.ars;

import com.google.gson.JsonObject;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.event.TardisRoomGrowEvent;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.rooms.TardisRoomData;
import me.eccentric_nz.tardis.rooms.TardisRoomRunnable;
import me.eccentric_nz.tardis.schematic.TardisSchematicGZip;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Builds rooms determined by the Architectural Reconfiguration System.
 *
 * @author eccentric_nz
 */
class TardisArsRunnable implements Runnable {

    private final TardisPlugin plugin;
    private final TardisArsSlot slot;
    private final Ars room;
    private final Player p;
    private final int tardisId;

    TardisArsRunnable(TardisPlugin plugin, TardisArsSlot slot, Ars room, Player p, int tardisId) {
        this.plugin = plugin;
        this.slot = slot;
        this.room = room;
        this.p = p;
        this.tardisId = tardisId;
    }

    @Override
    public void run() {
        String whichRoom = room.getConfigPath();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", p.getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            World w = TardisStaticLocationGetters.getWorld(tardis.getChunk());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
            TardisRoomData roomData = new TardisRoomData();
            roomData.setTardisId(tardis.getTardisId());
            // get middle data, default to orange wool if not set
            Material wall_type, floor_type;
            if (rsp.resultSet()) {
                wall_type = Material.getMaterial(rsp.getWall());
                floor_type = Material.getMaterial(rsp.getFloor());
            } else {
                wall_type = Material.ORANGE_WOOL;
                floor_type = Material.LIGHT_GRAY_WOOL;
            }
            roomData.setMiddleType(wall_type);
            roomData.setFloorType(floor_type);
            // get start locations
            Location l = new Location(w, slot.getX(), slot.getY(), slot.getZ());
            roomData.setDirection(CardinalDirection.SOUTH);
            String directory = (plugin.getRoomsConfig().getBoolean("rooms." + whichRoom + ".user")) ? "user_schematics" : "schematics";
            String path = plugin.getDataFolder() + File.separator + directory + File.separator + whichRoom.toLowerCase(Locale.ENGLISH) + ".tschm";
            // get JSON
            JsonObject obj = TardisSchematicGZip.unzip(path);
            // set y offset - this needs to be how many BLOCKS above ground 0 of the 16x16x16 chunk the room starts
            l.setY(l.getY() + room.getOffset());
            roomData.setLocation(l);
            roomData.setRoom(whichRoom);
            roomData.setSchematic(obj);
            // determine how often to place a block (in ticks) - `room_speed` is the number of BLOCKS to place in a second (20 ticks)
            long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
            plugin.getPluginManager().callEvent(new TardisRoomGrowEvent(p, tardis, slot, roomData));
            TardisRoomRunnable runnable = new TardisRoomRunnable(plugin, roomData, p);
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, delay);
            runnable.setTask(taskID);
            plugin.getTrackerKeeper().getRoomTasks().put(taskID, roomData);
            // remove BLOCKS from condenser table if rooms_require_blocks is true
            if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                HashMap<String, Integer> roomBlockCounts = getRoomBlockCounts(whichRoom, p.getUniqueId().toString());
                roomBlockCounts.forEach((key, value) -> {
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardisId", tardisId);
                    wherec.put("block_data", key);
                    plugin.getQueryFactory().alterCondenserBlockCount(value, wherec);
                });
            }
            // take their energy!
            int amount = plugin.getRoomsConfig().getInt("rooms." + whichRoom + ".cost");
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", p.getUniqueId().toString());
            plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, set, p);
            if (p.isOnline()) {
                TardisMessage.send(p, "ARS_CANCEL", whichRoom, String.format("%d", taskID));
            }
        }
    }

    private HashMap<String, Integer> getRoomBlockCounts(String room, String uuid) {
        HashMap<String, Integer> blockIDCount = new HashMap<>();
        HashMap<String, Integer> roomBlocks = plugin.getBuildKeeper().getRoomBlockCounts().get(room);
        String wall = "ORANGE_WOOL";
        String floor = "LIGHT_GRAY_WOOL";
        boolean hasPrefs = false;
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid);
        if (rsp.resultSet()) {
            hasPrefs = true;
            wall = rsp.getWall();
            floor = rsp.getFloor();
        }
        for (Map.Entry<String, Integer> entry : roomBlocks.entrySet()) {
            String bid = entry.getKey();
            String bData;
            if (hasPrefs && (bid.equals("ORANGE_WOOL") || bid.equals("LIGHT_GRAY_WOOL"))) {
                bData = (bid.equals("ORANGE_WOOL")) ? wall : floor;
            } else {
                bData = bid;
            }
            int tmp = Math.round((entry.getValue() / 100.0F) * plugin.getConfig().getInt("growth.rooms_condenser_percent"));
            int required = (tmp > 0) ? tmp : 1;
            if (blockIDCount.containsKey(bData)) {
                blockIDCount.put(bData, (blockIDCount.get(bData) + required));
            } else {
                blockIDCount.put(bData, required);
            }
        }
        return blockIDCount;
    }
}
