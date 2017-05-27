package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;

public class TARDISRecordingTask implements Runnable {

    private final TARDIS plugin;

    public TARDISRecordingTask(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void save() {
        if (!TARDISRecordingQueue.getQUEUE().isEmpty()) {
            insertIntoDatabase();
        }
    }

    public void insertIntoDatabase() {
        PreparedStatement s = null;
        Connection connection;
        try {
            int perBatch = 100;
            if (!TARDISRecordingQueue.getQUEUE().isEmpty()) {
                //plugin.debug("Beginning batch insert from queue. " + System.currentTimeMillis());
                TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
                connection = service.getConnection();
                // Handle dead connections
                if (connection == null || connection.isClosed()) {
                    if (TARDISRecordingManager.failedDbConnectionCount == 0) {
                        plugin.debug("TARDIS database error. Connection should be there but it's not. Leaving actions to log in queue.");
                    }
                    TARDISRecordingManager.failedDbConnectionCount++;
                    if (TARDISRecordingManager.failedDbConnectionCount > 5) {
                        plugin.debug("Too many problems connecting. Giving up for a bit.");
                        scheduleNextRecording();
                    }
                    plugin.debug("Database connection still missing, incrementing count.");
                    return;
                } else {
                    TARDISRecordingManager.failedDbConnectionCount = 0;
                }
                // Connection valid, proceed
                connection.setAutoCommit(false);
                s = connection.prepareStatement("INSERT INTO blocks (tardis_id,location,block,data,police_box) VALUES (?,?,?,?,1)");
                int i = 0;
                while (!TARDISRecordingQueue.getQUEUE().isEmpty()) {
                    if (connection.isClosed()) {
                        plugin.debug("TARDIS database error. We have to bail in the middle of building primary bulk insert query.");
                        break;
                    }
                    final String a = TARDISRecordingQueue.getQUEUE().poll();
                    // poll() returns null if queue is empty
                    if (a == null) {
                        break;
                    }
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("location", a);
                    ResultSetBlocks rs = new ResultSetBlocks(plugin, where, false);
                    if (rs.resultSet()) {
                        String loco = plugin.getLocationUtils().getLocationFromBukkitString(a).add(0.0d, -1.0d, 0.0d).toString();
                        s.setInt(1, rs.getReplacedBlock().getTardis_id());
                        s.setString(2, loco);
                        s.setInt(3, 208);
                        s.setInt(4, 0);
                        s.addBatch();
                    }
                    // Break out of the loop and just commit what we have
                    if (i >= perBatch) {
                        plugin.debug("Recorder: Batch max exceeded, running insert. Queue remaining: " + TARDISRecordingQueue.getQUEUE().size());
                        break;
                    }
                    i++;
                }
                s.executeBatch();
                if (connection.isClosed()) {
                    plugin.debug("TARDIS database error. We have to bail in the middle of building primary bulk insert query.");
                } else {
                    connection.commit();
                    connection.setAutoCommit(true);
                    //plugin.debug("Batch insert was commited: " + System.currentTimeMillis());
                }
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (final SQLException ignored) {
            }
        }
    }

    @Override
    public void run() {
        save();
        scheduleNextRecording();
    }

    protected int getTickDelayForNextBatch() {

        // If we have too many rejected connections, increase the schedule
        if (TARDISRecordingManager.failedDbConnectionCount > 5) {
            return TARDISRecordingManager.failedDbConnectionCount * 20;
        }
        return 5;
    }

    protected void scheduleNextRecording() {
        if (!plugin.isEnabled()) {
            plugin.debug("Can't schedule new recording tasks as plugin is now disabled. If you're shutting down the server, ignore me.");
            return;
        }
        plugin.setRecordingTask(plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new TARDISRecordingTask(plugin), getTickDelayForNextBatch()));
    }
}
