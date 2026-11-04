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
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the location of special areas the
 * TARDIS can travel to.
 *
 * @author eccentric_nz
 */
public class ResultSetLocator {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the structures table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetLocator(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the areas table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean fromWorld(World world) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "structures WHERE world = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, world.getKey().asString());
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Location location = new Location(
                            world,
                            rs.getInt("origin_chunk_x") << 4,
                            rs.getInt("y") + 4,
                            rs.getInt("origin_chunk_z") << 4);
                    locations.add(location);
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for structures table! " + e.getMessage());
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
                plugin.debug("Error closing structures table! " + e.getMessage());
            }
        }
        return true;
    }

    private final List<Location> locations = new ArrayList<>();

    public List<Location> getLocations() {
        Collections.shuffle(locations);
        return locations;
    }

    public Location getClosestStructureLocation(World world, int playerChunkX, int playerChunkZ) {
        String sql = """
        SELECT chunkX, chunkZ, ((chunkX - ?) * (chunkX - ?) + (chunkZ - ?) * (chunkZ - ?)) AS distance
        FROM coords
        WHERE world = ?
        AND claimed = 0
        ORDER BY distance ASC
        LIMIT 1
        """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, playerChunkX);
            statement.setInt(2, playerChunkX);
            statement.setInt(3, playerChunkZ);
            statement.setInt(4, playerChunkZ);
            statement.setString(5, world.getKey().asString());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Location(
                            world,
                            rs.getInt("origin_chunk_x") << 4,
                            rs.getInt("y") + 4,
                            rs.getInt("origin_chunk_z") << 4);
                }
            }
        } catch (SQLException ignored) {
        }
        return null;
    }
}
