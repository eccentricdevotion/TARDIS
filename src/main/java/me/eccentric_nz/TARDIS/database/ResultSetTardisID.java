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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS vaults.
 *
 * @author eccentric_nz
 */
public class ResultSetTardisID {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private int tardis_id;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetTardisID(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Attempts to see whether the supplied TARDIS id is in the tardis table. This method builds an SQL query string
     * from the parameters supplied and then executes the query.
     *
     * @param uuid the Time Lord uuid to check
     * @return true or false depending on whether the TARDIS id exists in the table
     */
    public boolean fromUUID(String uuid) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id FROM " + prefix + "tardis WHERE uuid = ? AND abandoned = 0";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                tardis_id = rs.getInt("tardis_id");
                return true;
            }
            return false;
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis [tardis_id fromUUID] table! " + e.getMessage());
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
                plugin.debug("Error closing tardis [tardis_id fromUUID] table! " + e.getMessage());
            }
        }
    }

    public int getTardis_id() {
        return tardis_id;
    }
}
