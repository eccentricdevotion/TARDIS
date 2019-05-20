/*
 * Copyright (C) 2019 eccentric_nz
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISSQLCondenserUpdate implements Runnable {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final int amount;
    private final HashMap<String, Object> where;
    private final String prefix;

    /**
     * Removes condenser block counts from an SQLite database table. This method builds an SQL query string from the
     * parameters supplied and then executes the query.
     *
     * @param plugin an instance of the main plugin class
     * @param amount the amount of blocks to remove
     * @param where  a HashMap&lt;String, Object&gt; of table fields and values to select the records to alter.
     */
    TARDISSQLCondenserUpdate(TARDIS plugin, int amount, HashMap<String, Object> where) {
        this.plugin = plugin;
        this.amount = amount;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    @Override
    public void run() {
        Statement statement = null;
        String wheres;
        StringBuilder sbw = new StringBuilder();
        where.forEach((key, value) -> {
            sbw.append(key).append(" = ");
            if (value instanceof String) {
                sbw.append("'").append(value).append("' AND ");
            } else {
                sbw.append(value).append(" AND ");
            }
        });
        where.clear();
        wheres = sbw.toString().substring(0, sbw.length() - 5);
        String query = "UPDATE " + prefix + "condenser SET block_count = block_count - " + amount + " WHERE " + wheres;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            plugin.debug("Block count update error for condenser! " + e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing condenser table! " + e.getMessage());
            }
        }
    }
}
