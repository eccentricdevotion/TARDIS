/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TVMResultSetManipulator {

    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISVortexManipulator plugin;
    private final String u;
    private final String prefix;
    private UUID uuid;
    private int tachyonLevel;

    public TVMResultSetManipulator(TARDISVortexManipulator plugin, String u) {
        this.plugin = plugin;
        this.u = u;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the manipulator table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "manipulator WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, u);
            rs = statement.executeQuery();
            if (rs.next()) {
                uuid = UUID.fromString(rs.getString("uuid"));
                tachyonLevel = rs.getInt("tachyon_level");
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

    public UUID getUuid() {
        return uuid;
    }

    public int getTachyonLevel() {
        return tachyonLevel;
    }
}
