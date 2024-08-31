package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.GameMode;
import org.bukkit.Location;

import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class TARDISPreviewPersister {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private int count = 0;

    public TARDISPreviewPersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void save() {
        if (!plugin.getTrackerKeeper().getPreviewers().isEmpty()) {
            PreparedStatement ps = null;
            try {
                connection.setAutoCommit(false);
                ps = connection.prepareStatement("INSERT INTO " + prefix + "previewers (uuid, world, x, y, z, yaw, pitch, gamemode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                for (Map.Entry<UUID, PreviewData> entry : plugin.getTrackerKeeper().getPreviewers().entrySet()) {
                    ps.setString(1, entry.getKey().toString());
                    Location location = entry.getValue().location();
                    GameMode gameMode = entry.getValue().gamemode();
                    ps.setString(2, location.getWorld().getName());
                    ps.setDouble(3, location.getX());
                    ps.setDouble(4, location.getY());
                    ps.setDouble(5, location.getZ());
                    ps.setFloat(6, location.getYaw());
                    ps.setFloat(7, location.getPitch());
                    ps.setString(8, gameMode.toString());
                    ps.addBatch();
                    count++;
                }
                ps.executeBatch();
                connection.setAutoCommit(true);
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Saved " + count + " desktop previewers.");
            } catch (SQLException e) {
                plugin.debug("Insert error for previewers query: " + e.getMessage());
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException e) {
                    plugin.debug("Error closing previewers statement: " + e.getMessage());
                }
            }
        }
    }

    public void load() {
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "previewers";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    try {
                        UUID uuid = UUID.fromString(rs.getString("uuid"));
                        String world = rs.getString("world");
                        float x = rs.getFloat("x");
                        float y = rs.getFloat("y");
                        float z = rs.getFloat("z");
                        float yaw = rs.getFloat("yaw");
                        float pitch = rs.getFloat("pitch");
                        Location location = new Location(TARDISAliasResolver.getWorldFromAlias(world), x, y, z, yaw, pitch);
                        String gm = rs.getString("gamemode");
                        GameMode gamemode = GameMode.valueOf(gm);
                        plugin.getTrackerKeeper().getPreviewers().put(uuid, new PreviewData(location, gamemode));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for previewers table! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing previewers table! " + e.getMessage());
            }
        }
    }
}
