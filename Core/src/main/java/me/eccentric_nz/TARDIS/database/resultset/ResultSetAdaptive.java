package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetAdaptive {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private int adaption;

    public ResultSetAdaptive(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public boolean fromID(int id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT adapti_on FROM " + prefix + "tardis WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                adaption = rs.getInt("adapti_on");
                return true;
            }
            return false;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis [adapti_on fromID]! " + e.getMessage());
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
                plugin.debug("Error closing tardis [adapti_on fromID]! " + e.getMessage());
            }
        }
    }

    public int getAdaption() {
        return adaption;
    }
}
