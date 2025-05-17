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
package me.eccentric_nz.tardisshop.database;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

public class DeleteShopItem {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public DeleteShopItem(TARDIS plugin) {
        this.plugin = plugin;
        this.prefix = this.plugin.getPrefix();
    }

    /**
     * Deletes rows from an SQLite database table. This method builds an SQL
     * query string from the parameters supplied and then executes the delete action.
     *
     * @param table the database table name to insert the data into.
     * @param where a HashMap&lt;String, Object&gt; of table fields and values to
     * select the records to delete.
     */
    public void removeRecord(String table, HashMap<String, Object> where) {
        Statement statement = null;
        String values;
        StringBuilder sbw = new StringBuilder();
        where.forEach((key, value) -> {
            sbw.append(key).append(" = ");
            if (value instanceof String || value instanceof UUID) {
                sbw.append("'").append(value).append("' AND ");
            } else {
                sbw.append(value).append(" AND ");
            }
        });
        where.clear();
        values = sbw.substring(0, sbw.length() - 5);
        String query = "DELETE FROM " + prefix + "items WHERE " + values;
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

    public int removeByLocation(String location) {
        PreparedStatement ps = null;
        final String query = "DELETE FROM " + prefix + "items WHERE location = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, location);
            return ps.executeUpdate();
        } catch (SQLException e) {
            plugin.debug("Delete error for items table! " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing items table! " + e.getMessage());
            }
        }
        return 0;
    }
}
