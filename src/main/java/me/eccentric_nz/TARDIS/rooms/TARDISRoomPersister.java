/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisTimeLord;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.schematic.TARDISSchematicGZip;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TARDISRoomPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private int count = 0;

    public TARDISRoomPersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void saveProgress() {
        try {
            ps = connection.prepareStatement("INSERT INTO " + prefix + "room_progress (`direction`, `room`, `location`, `tardis_id`, `progress_row`, `progress_column`, `progress_level`, `middle_type`, `floor_type`, `post_blocks`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (TARDISRoomData rd : plugin.getTrackerKeeper().getRoomTasks().values()) {
                ps.setString(1, rd.getDirection().toString());
                ps.setString(2, rd.getRoom());
                Location location = rd.getLocation();
                String l = TARDISStaticLocationGetters.makeLocationStr(location);
                ps.setString(3, l);
                ps.setInt(4, rd.getTardis_id());
                ps.setInt(5, rd.getRow());
                ps.setInt(6, rd.getColumn());
                ps.setInt(7, rd.getLevel());
                ps.setString(8, rd.getMiddleType().toString());
                ps.setString(9, rd.getFloorType().toString());
                ps.setString(10, String.join("@", rd.getPostBlocks()));
                count += ps.executeUpdate();
            }
            if (count > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Saved " + count + " room building tasks to resume later.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing insert room_progress statement: " + ex.getMessage());
            }
        }
    }

    public void resume() {
        PreparedStatement delete = null;
        ResultSet rs = null;
        try {
            delete = connection.prepareStatement("DELETE FROM " + prefix + "room_progress WHERE progress_id = ?");
            ps = connection.prepareStatement("SELECT * FROM " + prefix + "room_progress");
            rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String whichroom = rs.getString("room");
                    // get JSON
                    JsonObject obj = TARDISSchematicGZip.getObject(plugin, "rooms", whichroom.toLowerCase(Locale.ENGLISH), plugin.getRoomsConfig().getBoolean("rooms." + whichroom + ".user"));
                    if (obj != null) {
                        TARDISRoomData rd = new TARDISRoomData();
                        rd.setSchematic(obj);
                        rd.setRoom(whichroom);
                        Location location = TARDISStaticLocationGetters.getLocationFromDB(rs.getString("location"));
                        rd.setLocation(location);
                        int id = rs.getInt("tardis_id");
                        rd.setTardis_id(id);
                        rd.setRow(rs.getInt("progress_row"));
                        rd.setColumn(rs.getInt("progress_column"));
                        rd.setLevel(rs.getInt("progress_level"));
                        rd.setDirection(COMPASS.valueOf(rs.getString("direction")));
                        rd.setMiddleType(Material.valueOf(rs.getString("middle_type")));
                        rd.setFloorType(Material.valueOf(rs.getString("floor_type")));
                        List<String> postBlocks = new ArrayList<>(Arrays.asList(rs.getString("post_blocks").split("@")));
                        rd.setPostBlocks(postBlocks);
                        long delay = Math.round(20 / plugin.getConfig().getDouble("growth.room_speed"));
                        // get the player who's tardis this is
                        ResultSetTardisTimeLord rst = new ResultSetTardisTimeLord(plugin);
                        if (rst.fromID(id)) {
                            // resume the room growing
                            TARDISRoomRunnable runnable = new TARDISRoomRunnable(plugin, rd, rst.getUuid(), true);
                            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, delay);
                            runnable.setTask(taskID);
                            // resume tracking progress
                            plugin.getTrackerKeeper().getRoomTasks().put(taskID, rd);
                            // delete record
                            delete.setInt(1, rs.getInt("progress_id"));
                            delete.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("Error reading resume room_progress data: " + e.getMessage());
        } finally {
            try {
                if (delete != null) {
                    delete.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing resume room_progress statement(s): " + ex.getMessage());
            }
        }
    }
}
