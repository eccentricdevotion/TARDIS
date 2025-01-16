package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetConsoleLabel {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final int id;
    private Location location;

    public ResultSetConsoleLabel(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT uuid FROM " + prefix + "interactions WHERE tardis_id = ? AND control = 'CENTRE'";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                location = TARDISStaticLocationGetters.getLocationFromDB(rs.getString("uuid"));
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for interactions centre location! " + e.getMessage());
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
                plugin.debug("Error closing interactions centre location! " + e.getMessage());
            }
        }
    }

    public Location getLocation() {
        return location;
    }
}
