package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResultSetFlightControls {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private final int[] diodes = new int[4];
    private final List<Location> locations = new ArrayList<>();
    private final String prefix;

    public ResultSetFlightControls(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT uuid FROM " + prefix + "interactions WHERE tardis_id = ? AND control IN ('HELMIC_REGULATOR', 'ASTROSEXTANT_RECTIFIER', 'GRAVITIC_ANOMALISER', 'ABSOLUTE_TESSERACTULATOR')";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    try {
                        UUID uuid = UUID.fromString(rs.getString("uuid"));
                        Entity entity = plugin.getServer().getEntity(uuid);
                        if (entity != null) {
                            locations.add(entity.getLocation());
                        }
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for flight interactions table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing flight interactions table! " + e.getMessage());
            }
        }
        return true;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
