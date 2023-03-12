/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMMessage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TVMResultSetMessageById {

    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISVortexManipulator plugin;
    private final int id;
    private final String prefix;
    private TVMMessage message;

    public TVMResultSetMessageById(TARDISVortexManipulator plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the messages table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "messages WHERE message_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                message = new TVMMessage();
                message.setId(rs.getInt("message_id"));
                message.setWho(UUID.fromString(rs.getString("uuid_from")));
                message.setMessage(rs.getString("message"));
                message.setDate(getFormattedDate(rs.getLong("date")));
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("Warp error for saves table! " + e.getMessage());
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
                plugin.debug("Error closing saves table for warp! " + e.getMessage());
            }
        }
        return true;
    }

    public TVMMessage getMessage() {
        return message;
    }

    private String getFormattedDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(plugin.getConfig().getString("date_format"));
        Date theDate = new Date(millis);
        return sdf.format(theDate);
    }
}
