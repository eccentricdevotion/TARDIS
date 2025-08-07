/*
 * Copyright (C) 2025 eccentric_nz
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISSQLUpdate implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String table;
    private final HashMap<String, Object> data;
    private final HashMap<String, Object> where;
    private final String prefix;

    /**
     * Updates data in an SQLite database table. This method builds an SQL query string from the parameters supplied and
     * then executes the update.
     *
     * @param plugin an instance of the main plugin class
     * @param table  the database table name to update.
     * @param data   a HashMap<String, Object> of table fields and values update.
     * @param where  a HashMap<String, Object> of table fields and values to select the records to update.
     */
    TARDISSQLUpdate(TARDIS plugin, String table, HashMap<String, Object> data, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.table = table;
        this.data = data;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        PreparedStatement ps = null;
        String updates;
        String wheres;
        StringBuilder sbu = new StringBuilder();
        StringBuilder sbw = new StringBuilder();
        data.forEach((key, value) -> sbu.append(key).append(" = ?,"));
        where.forEach((key, value) -> {
            sbw.append(key).append(" = ");
            if (value instanceof String || value instanceof UUID) {
                sbw.append("'").append(value).append("' AND ");
            } else {
                sbw.append(value).append(" AND ");
            }
        });
        where.clear();
        updates = sbu.substring(0, sbu.length() - 1);
        wheres = sbw.substring(0, sbw.length() - 5);
        String query = "UPDATE " + prefix + table + " SET " + updates + " WHERE " + wheres;
//        plugin.debug(query);
        try {
            service.testConnection(connection);
            ps = connection.prepareStatement(query);
            int s = 1;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue() instanceof String || entry.getValue() instanceof UUID) {
                    ps.setString(s, entry.getValue().toString());
                }
                switch (entry.getValue()) {
                    case Integer i -> ps.setInt(s, i);
                    case Double v -> ps.setDouble(s, v);
                    case Float v -> ps.setFloat(s, v);
                    case Long l -> ps.setLong(s, l);
                    case null, default -> ps.setObject(s, entry.getValue());
                }
                s++;
            }
            data.clear();
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Update error for " + table + "! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing " + table + "! " + e.getMessage());
            }
        }
    }
}
