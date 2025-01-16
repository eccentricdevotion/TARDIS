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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS library.
 *
 * @author eccentric_nz
 */
public class ResultSetLibrary {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String location;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     * @param location  the location of the smelter chest.
     */
    public ResultSetLibrary(TARDIS plugin, String location) {
        this.plugin = plugin;
        this.location = location;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the vaults table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "vaults WHERE location = ? AND chest_type = 'LIBRARY'";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, location);
            rs = statement.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            plugin.debug("ResultSet error for [vaults] library table! " + e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing [vaults] library table! " + e.getMessage());
            }
        }
    }
}
