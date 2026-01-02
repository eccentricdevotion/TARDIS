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
package me.eccentric_nz.TARDIS.database.resultset;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... a list of locations the TARDIS can
 * travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetChunkContainsTARDIS {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String world;
    private final int minX;
    private final int minZ;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the current locations table.
     *
     * @param plugin an instance of the main class.
     * @param world  the world to search in.
     * @param minX   the minimum blockX coordinate of the chunk.
     * @param minZ   the minimum blockZ coordinate of the chunk.
     */
    public ResultSetChunkContainsTARDIS(TARDIS plugin, String world, int minX, int minZ) {
        this.plugin = plugin;
        this.world = world;
        this.minX = minX;
        this.minZ = minZ;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the destinations table. This method builds an SQL query string from the
     * parameters supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id FROM " + prefix + "current WHERE world = ?" +
                "AND x >= ? AND x < ? AND z >= ? AND z < ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, world);
            statement.setInt(2, minX);
            statement.setInt(3, minZ);
            statement.setInt(4, minX + 16);
            statement.setInt(5, minZ + 16);
            rs = statement.executeQuery();
            return rs.isBeforeFirst();
        } catch (SQLException e) {
            plugin.debug("ResultSet error for [ChunkContains] current table! " + e.getMessage());
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
                plugin.debug("Error closing [ChunkContains] current table! " + e.getMessage());
            }
        }
    }
}
