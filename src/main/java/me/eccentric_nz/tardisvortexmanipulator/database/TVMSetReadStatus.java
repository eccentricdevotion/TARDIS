/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author eccentric_nz
 */
public class TVMSetReadStatus implements Runnable {

    private final TARDISVortexManipulator plugin;
    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final int id;
    private final String prefix;

    /**
     * Sets the read status of a message. This method builds an SQL query string from the parameters supplied and then
     * executes the query.
     *
     * @param plugin an instance of the main plugin class
     * @param id     the message_id to set the status of
     */
    public TVMSetReadStatus(TARDISVortexManipulator plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        Statement statement = null;
        String query = "UPDATE " + prefix + "messages SET read = 1 WHERE message_id = " + id;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.debug("Read status update error! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Read status error closing messages table! " + e.getMessage());
            }
        }
    }
}
