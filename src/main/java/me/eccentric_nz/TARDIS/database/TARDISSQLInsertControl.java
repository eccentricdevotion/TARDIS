/*
 * Copyright (C) 2013 eccentric_nz
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSQLInsertControl implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final int id;
    private final int type;
    private final String l;
    private final int s;

    /**
     * Updates data in an SQLite database table. This method builds an SQL query
     * string from the parameters supplied and then executes the update.
     *
     * @param plugin an instance of the main plugin class
     * @param id the unique TARDIS identifier
     * @param type the type of control to insert
     * @param l the location of the control
     * @param s whether the control is a secondary control
     */
    public TARDISSQLInsertControl(TARDIS plugin, int id, int type, String l, int s) {
        this.plugin = plugin;
        this.id = id;
        this.type = type;
        this.l = l;
        this.s = s;
    }

    @Override
    public void run() {
        Statement statement = null;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            String select = "SELECT c_id FROM controls WHERE tardis_id = " + id + " AND type = " + type + " AND secondary = " + s;
            ResultSet rs = statement.executeQuery(select);
            if (rs.isBeforeFirst()) {
                // update
                String update = "UPDATE controls SET location = '" + l + "' WHERE c_id = " + rs.getInt("c_id");
                statement.executeUpdate(update);
            } else {
                // insert
                String insert = "INSERT INTO controls (tardis_id, type, location, secondary) VALUES (" + id + ", " + type + ", '" + l + "', " + s + ")";
                statement.executeUpdate(insert);
            }
        } catch (SQLException e) {
            plugin.debug("Insert control error! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing insert control statement! " + e.getMessage());
            }
        }
    }
}
