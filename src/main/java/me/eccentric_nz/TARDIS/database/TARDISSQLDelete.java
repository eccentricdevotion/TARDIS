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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSQLDelete implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final String table;
    private final HashMap<String, Object> where;

    /**
     * Deletes rows from an SQLite database table. This method builds an SQL
     * query string from the parameters supplied and then executes the delete.
     *
     * @param plugin an instance of the main plugin class
     * @param table the database table name to insert the data into.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to delete.
     */
    public TARDISSQLDelete(TARDIS plugin, String table, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.table = table;
        this.where = where;
    }

    @Override
    public void run() {
        Statement statement = null;
        String values;
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("' AND ");
            } else {
                sbw.append(entry.getValue()).append(" AND ");
            }
        }
        where.clear();
        values = sbw.toString().substring(0, sbw.length() - 5);
        String query = "DELETE FROM " + table + " WHERE " + values;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.debug("Delete error for " + table + "! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }
}
