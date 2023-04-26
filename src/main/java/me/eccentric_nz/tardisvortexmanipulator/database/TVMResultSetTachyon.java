/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMTachyon;

/**
 * @author eccentric_nz
 */
public class TVMResultSetTachyon {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final List<TVMTachyon> vms = new ArrayList<>();
    private final String prefix;

    public TVMResultSetTachyon(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the beacons table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "manipulator";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    TVMTachyon tvmt = new TVMTachyon(UUID.fromString(rs.getString("uuid")), rs.getInt("tachyon_level"));
                    vms.add(tvmt);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for manipulator table! " + e.getMessage());
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
                plugin.debug("Error closing manipulator table! " + e.getMessage());
            }
        }
        return true;
    }

    public List<TVMTachyon> getMaipulators() {
        return vms;
    }
}
