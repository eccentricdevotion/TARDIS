/*
 * Copyright (C) 2023 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.data.ConfiguredSonic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... a Time Lord's preferred sonic
 * screwdriver specifications.
 *
 * @author eccentric_nz
 */
public class ResultSetConfiguredSonic {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final UUID where;
    private final String prefix;
    private ConfiguredSonic configuredSonic;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     * @param where  a HashMap&lt;String, Object&gt; of table fields and values to refine the search.
     */
    public ResultSetConfiguredSonic(TARDIS plugin, UUID where) {
        this.plugin = plugin;
        this.where = where;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the sonic table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "sonic WHERE sonic_uuid = '" + where.toString() + "'";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                configuredSonic = new ConfiguredSonic(rs.getInt("sonic_id"), UUID.fromString(rs.getString("uuid")), rs.getInt("bio"), rs.getInt("diamond"), rs.getInt("emerald"), rs.getInt("redstone"), rs.getInt("painter"), rs.getInt("ignite"), rs.getInt("arrow"), rs.getInt("knockback"), UUID.fromString(rs.getString("sonic_uuid")));
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for sonic config table! " + e.getMessage());
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
                plugin.debug("Error closing sonic config table! " + e.getMessage());
            }
        }
        return true;
    }

    public ConfiguredSonic getConfiguredSonic() {
        return configuredSonic;
    }
}
