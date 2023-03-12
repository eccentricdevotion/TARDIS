/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMMessage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TVMResultSetOutbox {

    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDISVortexManipulator plugin;
    private final String where;
    private final int start, limit;
    private final List<TVMMessage> mail = new ArrayList<>();
    private final String prefix;

    public TVMResultSetOutbox(TARDISVortexManipulator plugin, String where, int start, int limit) {
        this.plugin = plugin;
        this.where = where;
        this.start = start;
        this.limit = limit;
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
        String query = String.format("SELECT * FROM " + prefix + "messages WHERE uuid_from = ? ORDER BY date DESC LIMIT %d, %d", start, start + limit);
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, where);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    TVMMessage tvmm = new TVMMessage();
                    tvmm.setId(rs.getInt("message_id"));
                    tvmm.setWho(UUID.fromString(rs.getString("uuid_to")));
                    tvmm.setMessage(rs.getString("message"));
                    tvmm.setDate(getFormattedDate(rs.getLong("date")));
//                    tvmm.setRead(rs.getBoolean("read"));
                    mail.add(tvmm);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("Outbox error for messages table! " + e.getMessage());
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
                plugin.debug("Error closing messages table! " + e.getMessage());
            }
        }
        return true;
    }

    private String getFormattedDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat(plugin.getConfig().getString("date_format"));
        Date theDate = new Date(millis);
        return sdf.format(theDate);
    }

    public List<TVMMessage> getMail() {
        return mail;
    }
}
