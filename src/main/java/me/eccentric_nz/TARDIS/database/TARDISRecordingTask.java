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
package me.eccentric_nz.tardis.database;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetBlocks;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public class TardisRecordingTask implements Runnable {

    private final TardisPlugin plugin;
    private final String prefix;

    public TardisRecordingTask(TardisPlugin plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    private void save() {
        if (!TardisRecordingQueue.getQUEUE().isEmpty()) {
            insertIntoDatabase();
        }
    }

    private void insertIntoDatabase() {
        PreparedStatement s = null;
        Connection connection;
        try {
            int perBatch = 100;
            if (!TardisRecordingQueue.getQUEUE().isEmpty()) {
                TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
                connection = service.getConnection();
                // Handle dead connections
                if (connection == null || connection.isClosed()) {
                    if (TardisRecordingManager.failedDbConnectionCount == 0) {
                        plugin.debug("tardis database error. Connection should be there but it's not. Leaving actions to log in queue.");
                    }
                    TardisRecordingManager.failedDbConnectionCount++;
                    if (TardisRecordingManager.failedDbConnectionCount > 5) {
                        plugin.debug("Too many problems connecting. Giving up for a bit.");
                        scheduleNextRecording();
                    }
                    plugin.debug("Database connection still missing, incrementing count.");
                    return;
                } else {
                    TardisRecordingManager.failedDbConnectionCount = 0;
                }
                // Connection valid, proceed
                connection.setAutoCommit(false);
                s = connection.prepareStatement("INSERT INTO " + prefix + "blocks (tardis_id, location, data, police_box) VALUES (?, ?, ?, 1)");
                int i = 0;
                while (!TardisRecordingQueue.getQUEUE().isEmpty()) {
                    if (connection.isClosed()) {
                        plugin.debug("tardis database error. We have to bail in the middle of building primary bulk insert query.");
                        break;
                    }
                    String a = TardisRecordingQueue.getQUEUE().poll();
                    // poll() returns null if queue is empty
                    if (a == null) {
                        break;
                    }
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("location", a);
                    ResultSetBlocks rs = new ResultSetBlocks(plugin, where, false);
                    if (rs.resultSet()) {
                        String loco = Objects.requireNonNull(TardisStaticLocationGetters.getLocationFromBukkitString(a)).add(0.0d, -1.0d, 0.0d).toString();
                        s.setInt(1, rs.getReplacedBlock().getTardisId());
                        s.setString(2, loco);
                        s.setString(3, "minecraft:grass_path");
                        s.addBatch();
                    }
                    // Break out of the loop and just commit what we have
                    if (i >= perBatch) {
                        plugin.debug("Recorder: Batch max exceeded, running insert. Queue remaining: " + TardisRecordingQueue.getQUEUE().size());
                        break;
                    }
                    i++;
                }
                s.executeBatch();
                if (connection.isClosed()) {
                    plugin.debug("tardis database error. We have to bail in the middle of building primary bulk insert query.");
                } else {
                    connection.commit();
                    connection.setAutoCommit(true);
                    //plugin.debug("Batch insert was committed: " + System.currentTimeMillis());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void run() {
        save();
        scheduleNextRecording();
    }

    private int getTickDelayForNextBatch() {
        // If we have too many rejected connections, increase the schedule
        if (TardisRecordingManager.failedDbConnectionCount > 5) {
            return TardisRecordingManager.failedDbConnectionCount * 20;
        }
        return 5;
    }

    private void scheduleNextRecording() {
        if (!plugin.isEnabled()) {
            plugin.debug("Can't schedule new recording tasks as plugin is now disabled. If you're shutting down the server, ignore me.");
            return;
        }
        plugin.setRecordingTask(plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new TardisRecordingTask(plugin), getTickDelayForNextBatch()));
    }
}
