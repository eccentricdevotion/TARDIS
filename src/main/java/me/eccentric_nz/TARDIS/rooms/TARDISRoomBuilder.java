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
package me.eccentric_nz.TARDIS.rooms;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISRoomGrowEvent;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Locale;

/**
 * There were at least fourteen bathrooms in the TARDIS, one of which had had a leaky tap for three centuries. Because
 * he had misplaced his washers, the Doctor kept it from flooding the TARDIS by sealing it in a time loop that made the
 * same drop of water leak out over and over again.
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
     * Gets the required data to build a TARDIS room, then starts a repeating task to build it.
     * <p>
     * This needs to be set up to use the actual dimensions from the schematic files, if user supplied room schematics
     * are allowed to be used.
     *
     * @return true or false
     */
    public boolean build() {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(p.getUniqueId().toString())) {
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
            TARDISRoomData roomData = new TARDISRoomData();
            roomData.setTardis_id(rs.getTardis_id());
            // get wall data, default to orange wool if not set
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
            Block b = l.getBlock();
            roomData.setBlock(b);
            roomData.setDirection(d);
            String directory = (plugin.getRoomsConfig().getBoolean("rooms." + r + ".user")) ? "user_schematics" : "schematics";
            String path = plugin.getDataFolder() + File.separator + directory + File.separator + r.toLowerCase(Locale.ENGLISH) + ".tschm";
            // get JSON
            JsonObject obj = TARDISSchematicGZip.unzip(path);
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            int xzoffset = (dimensions.get("width").getAsInt() / 2);
            switch (d) {
                case NORTH -> {
                    l.setX(l.getX() - xzoffset);
                    l.setZ(l.getZ() - dimensions.get("width").getAsInt());
                }
                case WEST -> {
                    l.setX(l.getX() - dimensions.get("width").getAsInt());
                    l.setZ(l.getZ() - xzoffset);
                }
                case SOUTH -> l.setX(l.getX() - xzoffset);
                default -> l.setZ(l.getZ() - xzoffset);
            }
            // set y offset
            int offset = Math.abs(plugin.getRoomsConfig().getInt("rooms." + r + ".offset"));
            l.setY(l.getY() - offset);
            roomData.setLocation(l);
            roomData.setRoom(r);
            roomData.setSchematic(obj);
            // determine how often to place a block (in ticks) - `room_speed` is the number of BLOCKS to place in a second (20 ticks)
            long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
            plugin.getPM().callEvent(new TARDISRoomGrowEvent(p, null, null, roomData));
            TARDISRoomRunnable runnable = new TARDISRoomRunnable(plugin, roomData, p.getUniqueId());
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, delay);
            runnable.setTask(taskID);
            plugin.getTrackerKeeper().getRoomTasks().put(taskID, roomData);
            TARDISMessage.send(p, "ROOM_CANCEL", String.format("%d", taskID));
        }
        return true;
    }
}
