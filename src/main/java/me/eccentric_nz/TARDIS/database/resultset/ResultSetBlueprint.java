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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS vaults.
 *
 * @author eccentric_nz
 */
public class ResultSetBlueprint {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetBlueprint(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Attempts to see whether the supplied player uuid has the required permission record in the blueprint table. This
     * method builds an SQL query string from the parameters supplied and then executes the query.
     *
     * @param uuid the Time Lord uuid to check
     * @param node the permission node to check
     * @return true or false depending on whether the Time Lord has the permission node
     */
    public boolean getPerm(String uuid, String node) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "blueprint WHERE uuid = ? AND permission = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            statement.setString(2, node);
            rs = statement.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            plugin.debug("ResultSet error for blueprint table! " + e.getMessage());
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
                plugin.debug("Error closing blueprint table! " + e.getMessage());
            }
        }
    }

    /**
     * Attempts to see whether the supplied player uuid has the required permission record in the blueprint table. This
     * method builds an SQL query string from the parameters supplied and then executes the query.
     *
     * @param uuid the Time Lord uuid to check
     * @param node the permission node to check
     * @return true or false depending on whether the Time Lord has the permission node
     */
    public int getRecordId(String uuid, String node) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT bp_id FROM " + prefix + "blueprint WHERE uuid = ? AND permission = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            statement.setString(2, node);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                return rs.getInt("bp_id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for blueprint table! " + e.getMessage());
            return -1;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing blueprint table! " + e.getMessage());
            }
        }
    }
}
