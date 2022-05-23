/*
 * Copyright (C) 2022 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.data.Farm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... everything about the construction of
 * the TARDIS itself.
 *
 * @author eccentric_nz
 */
public class ResultSetFarming {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int tardis_id;
    private final String prefix;
    private Farm data;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the farming table.
     *
     * @param plugin    an instance of the main class.
     * @param tardis_id the id of the TARDIS to get the farm locations for.
     */
    public ResultSetFarming(TARDIS plugin, int tardis_id) {
        this.plugin = plugin;
        this.tardis_id = tardis_id;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the farming table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;

        String query = "SELECT * FROM " + prefix + "farming WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, tardis_id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                data = new Farm(
                        rs.getInt("tardis_id"),
                        rs.getString("apiary"),
                        rs.getString("aquarium"),
                        rs.getString("bamboo"),
                        rs.getString("birdcage"),
                        rs.getString("farm"),
                        rs.getString("geode"),
                        rs.getString("hutch"),
                        rs.getString("igloo"),
                        rs.getString("stable"),
                        rs.getString("stall"),
                        rs.getString("village")
                );
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for farming table! " + e.getMessage());
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
                plugin.debug("Error closing farming table! " + e.getMessage());
            }
        }
        return true;
    }

    public Farm getFarming() {
        return data;
    }
}
