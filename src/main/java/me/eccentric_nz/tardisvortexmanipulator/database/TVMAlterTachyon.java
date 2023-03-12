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
public class TVMAlterTachyon implements Runnable {

    private final TARDISVortexManipulator plugin;
    private final TVMDatabase service = TVMDatabase.getInstance();
    private final Connection connection = service.getConnection();
    private final int amount;
    private final String uuid;
    private final String prefix;

    /**
     * Adds or removes tachyons from a database table. This method builds an SQL query string from the parameters
     * supplied and then executes the query.
     *
     * @param plugin an instance of the main plugin class
     * @param amount the amount of energy to add or remove (use a negative value)
     * @param uuid   a player's UUID.
     */
    public TVMAlterTachyon(TARDISVortexManipulator plugin, int amount, String uuid) {
        this.plugin = plugin;
        this.amount = amount;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        Statement statement = null;
        String query = "UPDATE " + prefix + "manipulator SET tachyon_level = tachyon_level + " + amount + " WHERE uuid = '" + uuid + "'";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.debug("Tachyon update error! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Tachyon error closing manipulator table! " + e.getMessage());
            }
        }
    }
}
