/*
 * Copyright (C) 2021 eccentric_nz
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
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS vaults.
 * <p>
 * Control types: 0 = handbrake 1 = random button 2 = x-repeater 3 = z-repeater 4 = multiplier-repeater 5 =
 * environment-repeater 6 = artron button
 *
 * @author eccentric_nz
 */
public class ResultSetTardisCompanions {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private String companions;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetTardisCompanions(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Gets to the companions of a TARDIS. This method builds an SQL query string from the parameters supplied and then
     * executes the query.
     *
     * @param uuid the Time Lord uuid to check
     * @return true or false depending on whether the TARDIS is powered on
     */
    public boolean fromUUID(String uuid) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT companions FROM " + prefix + "tardis WHERE uuid = ? AND abandoned = 0";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                companions = rs.getString("companions");
                return true;
            }
            return false;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis [companions fromUUID] table! " + e.getMessage());
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
                plugin.debug("Error closing tardis [companions fromUUID] table! " + e.getMessage());
            }
        }
    }

    /**
     * Gets to the companions of a TARDIS. This method builds an SQL query string from the parameters supplied and then
     * executes the query.
     *
     * @param id the Tardis ID to check
     * @return true or false depending on whether the TARDIS is powered on
     */
    public boolean fromID(int id) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT companions FROM " + prefix + "tardis WHERE tardis_id = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                companions = rs.getString("companions");
                return true;
            }
            return false;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis [companions fromID] table! " + e.getMessage());
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
                plugin.debug("Error closing tardis [companions fromID] table! " + e.getMessage());
            }
        }
    }

    public String getCompanions() {
        return companions;
    }
}
