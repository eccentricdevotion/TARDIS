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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSQLUpdate implements Runnable {

    private final TARDIS plugin;
    TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    Connection connection = service.getConnection();
    private final String table;
    private final HashMap<String, Object> data;
    private final HashMap<String, Object> where;

    /**
     * Updates data in an SQLite database table. This method builds an SQL query
     * string from the parameters supplied and then executes the update.
     *
     * @param plugin an instance of the main plugin class
     * @param table the database table name to update.
     * @param data a HashMap<String, Object> of table fields and values update.
     * @param where a HashMap<String, Object> of table fields and values to
     * select the records to update.
     */
    public TARDISSQLUpdate(TARDIS plugin, String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.table = table;
        this.data = data;
        this.where = where;
    }

    @Override
    public void run() {
        PreparedStatement statement = null;
        String updates;
        String wheres;
        StringBuilder sbu = new StringBuilder();
        StringBuilder sbw = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sbu.append(entry.getKey()).append(" = ?,");
        }
        for (Map.Entry<String, Object> entry : where.entrySet()) {
            sbw.append(entry.getKey()).append(" = ");
            if (entry.getValue().getClass().equals(String.class)) {
                sbw.append("'").append(entry.getValue()).append("' AND ");
            } else {
                sbw.append(entry.getValue()).append(" AND ");
            }
        }
        where.clear();
        updates = sbu.toString().substring(0, sbu.length() - 1);
        wheres = sbw.toString().substring(0, sbw.length() - 5);
        String query = "UPDATE " + table + " SET " + updates + " WHERE " + wheres;
        try {
            statement = connection.prepareStatement(query);
            int s = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue().getClass().equals(String.class)) {
                    statement.setString(s, entry.getValue().toString());
                }
                if (entry.getValue() instanceof Integer) {
                    statement.setInt(s, (Integer) entry.getValue());
                }
                if (entry.getValue() instanceof Long) {
                    statement.setLong(s, (Long) entry.getValue());
                }
                s++;
            }
            data.clear();
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Update error for " + table + "! " + e.getMessage());
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
