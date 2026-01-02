/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.bStats;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class CondenserCounts {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the chameleon table.
     *
     * @param plugin an instance of the main class.
     */
    CondenserCounts(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the TARDIS table and counts the number of records.
     *
     * @return a map of blocks and counts
     */
    public HashMap<String, Integer> getCounts() {
        HashMap<String, Integer> data = new HashMap<>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT block_data, block_count FROM " + prefix + "condenser";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                if (rs.isBeforeFirst()) {
                    while (rs.next()) {
                        String block = rs.getString("block_data");
                        int amount = rs.getInt("block_count");
                        int count = (data.containsKey(block)) ? data.get(block) + amount : amount;
                        data.put(block, count);
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for condenser getting block counts! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing condenser table getting block counts! " + e.getMessage());
            }
        }
        return data;
    }
}
