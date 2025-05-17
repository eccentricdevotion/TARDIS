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
package me.eccentric_nz.TARDIS.rooms;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.api.event.TARDISRoomGrowEvent;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Locale;

/**
 * There were at least fourteen bathrooms in the TARDIS, one of which had a
 * leaky tap for three centuries. Because he had misplaced his washers, the
 * Doctor kept it from flooding the TARDIS by sealing it in a time loop that
 * made the same drop of water leak out over and over again.
 *
 * @author eccentric_nz
 */
public class TARDISRoomBuilder {

    private final TARDIS plugin;
    private final String room;
    private final Location location;
    private final COMPASS direction;
    private final Player player;

    public TARDISRoomBuilder(TARDIS plugin, String room, Location location, COMPASS direction, Player player) {
        this.plugin = plugin;
        this.room = room;
        this.location = location;
        this.direction = direction;
        this.player = player;
    }

    /**
     * Gets the required data to build a TARDIS room, then starts a repeating
     * task to build it.
     * <p>
     * This needs to be set up to use the actual dimensions from the schematic
     * files, if user supplied room schematics are allowed to be used.
     *
     * @return true or false
     */
    public boolean build() {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
            TARDISRoomData roomData = new TARDISRoomData();
            roomData.setTardis_id(rs.getTardisId());
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
            Block b = location.getBlock();
            roomData.setBlock(b);
            roomData.setDirection(direction);
            // get JSON
            JsonObject obj = TARDISSchematicGZip.getObject(plugin, "rooms", room.toLowerCase(Locale.ROOT), plugin.getRoomsConfig().getBoolean("rooms." + room + ".user"));
            if (obj == null) {
                return false;
            }
            JsonObject dimensions = obj.get("dimensions").getAsJsonObject();
            int xzoffset = (dimensions.get("width").getAsInt() / 2);
            switch (direction) {
                case NORTH -> {
                    location.setX(location.getX() - xzoffset);
                    location.setZ(location.getZ() - dimensions.get("width").getAsInt());
                }
                case WEST -> {
                    location.setX(location.getX() - dimensions.get("width").getAsInt());
                    location.setZ(location.getZ() - xzoffset);
                }
                case SOUTH -> location.setX(location.getX() - xzoffset);
                default -> location.setZ(location.getZ() - xzoffset);
            }
            // set y offset
            int offset = Math.abs(plugin.getRoomsConfig().getInt("rooms." + room + ".offset"));
            location.setY(location.getY() - offset);
            roomData.setLocation(location);
            roomData.setRoom(room);
            roomData.setSchematic(obj);
            // determine how often to place a block (in ticks) - `room_speed` is the number of BLOCKS to place in a second (20 ticks)
            long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
            plugin.getPM().callEvent(new TARDISRoomGrowEvent(player, null, null, roomData));
            TARDISRoomRunnable runnable = new TARDISRoomRunnable(plugin, roomData, player.getUniqueId());
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, delay);
            runnable.setTask(taskID);
            plugin.getTrackerKeeper().getRoomTasks().put(taskID, roomData);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_CANCEL", String.format("%d", taskID));
            // damage the ARS circuit if configured
            if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.ars") > 0) {
                // get the id of the TARDIS this player is in
                TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, rs.getTardisId());
                tcc.getCircuits();
                // decrement uses
                int uses_left = tcc.getArsUses();
                new TARDISCircuitDamager(plugin, DiskCircuit.ARS, uses_left, rs.getTardisId(), player).damage();
            }
        }
        return true;
    }
}
