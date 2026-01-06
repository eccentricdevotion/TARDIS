/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.advanced.DamageUtility;
import me.eccentric_nz.TARDIS.api.event.TARDISRoomGrowEvent;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.SchematicGZip;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * There were at least fourteen bathrooms in the TARDIS, one of which had a
 * leaky tap for three centuries. Because he had misplaced his washers, the
 * Doctor kept it from flooding the TARDIS by sealing it in a time loop that
 * made the same drop of water leak out over and over again.
 *
 * @author eccentric_nz
 */
public class StaircaseBuilder {

    private final TARDIS plugin;
    private final Location location;
    private final Player player;
    private final boolean isUp;

    public StaircaseBuilder(TARDIS plugin, Location location, Player player, boolean isUp) {
        this.plugin = plugin;
        this.location = location;
        this.player = player;
        this.isUp = isUp;
    }

    /**
     * Gets the required data to build a TARDIS staircase room, then starts a repeating
     * task to build it.
     *
     * @return true or false
     */
    public boolean build() {
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (rs.fromUUID(player.getUniqueId().toString())) {
            RoomData roomData = new RoomData();
            roomData.setTardis_id(rs.getTardisId());
            roomData.setMiddleType(Material.ORANGE_WOOL);
            roomData.setFloorType(Material.LIGHT_GRAY_WOOL);
            roomData.setDirection(COMPASS.SOUTH);
            // get JSON
            JsonObject obj = SchematicGZip.getObject(plugin, "rooms", "staircase", false);
            if (obj == null) {
                return false;
            }
            roomData.setLocation(location);
            roomData.setRoom("STAIRCASE");
            roomData.setSchematic(obj);
            // trigger removal of WARPED SLABS between rooms
            plugin.getTrackerKeeper().getIsStackedStaircase().put(rs.getTardisId(), isUp);
            // determine how often to place a block (in ticks) - `room_speed` is the number of BLOCKS to place in a second (20 ticks)
            long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
            plugin.getPM().callEvent(new TARDISRoomGrowEvent(player, null, null, roomData));
            RoomRunnable runnable = new RoomRunnable(plugin, roomData, player.getUniqueId());
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, delay);
            runnable.setTask(taskID);
            plugin.getTrackerKeeper().getRoomTasks().put(taskID, roomData);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_CANCEL", String.format("%d", taskID));
            // damage the ARS circuit if configured
            DamageUtility.run(plugin, DiskCircuit.ARS, rs.getTardisId(), player);
        }
        return true;
    }
}
