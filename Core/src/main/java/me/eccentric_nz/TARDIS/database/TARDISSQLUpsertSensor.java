/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.database;

import me.eccentric_nz.TARDIS.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author eccentric_nz
 */
class TARDISSQLUpsertSensor implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final int id;
    private final String type;
    private final String location;
    private final String prefix;

    /**
     * Updates data in an SQLite database table. This method builds an SQL query string from the parameters supplied and
     * then executes the update.
     *
     * @param plugin   an instance of the main plugin class
     * @param id       the unique TARDIS identifier
     * @param type     the type of sensor to insert
     * @param location the location of the sensor
     */
    TARDISSQLUpsertSensor(TARDIS plugin, int id, String type, String location) {
        this.plugin = plugin;
        this.id = id;
        this.type = type;
        this.location = location;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        Statement statement = null;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            String select = "SELECT sensor_id FROM " + prefix + "sensors WHERE tardis_id = " + id;
            ResultSet rs = statement.executeQuery(select);
            if (rs.isBeforeFirst()) {
                rs.next();
                // update
                String update = "UPDATE " + prefix + "sensors SET " + type + " = '" + location + "' WHERE sensor_id = " + rs.getInt("sensor_id");
                statement.executeUpdate(update);
            } else {
                // insert
                String insert = "INSERT INTO " + prefix + "sensors (tardis_id, " + type + ") VALUES (" + id + ", '" + location + "')";
                statement.executeUpdate(insert);
            }
        } catch (SQLException e) {
            plugin.debug("Insert sensor error! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing insert sensor statement! " + e.getMessage());
            }
        }
    }
}
