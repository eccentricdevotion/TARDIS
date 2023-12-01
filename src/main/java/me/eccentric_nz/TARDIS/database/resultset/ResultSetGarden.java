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
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetGarden {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private int garden_id;
    private int tardis_id;
    private int minX;
    private int maxX;
    private int minY;
    private int minZ;
    private int maxZ;
    private String world;
    private final Location location;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the transmats table.
     *
     * @param plugin   an instance of the main class.
     * @param location the location to check.
     */
    public ResultSetGarden(TARDIS plugin, Location location) {
        this.plugin = plugin;
        this.location = location;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the gardens table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "gardens WHERE ? BETWEEN minx AND maxx AND y = ? AND ? BETWEEN minz AND maxz";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, location.getBlockX());
            statement.setInt(2, location.getBlockY());
            statement.setInt(3, location.getBlockZ());
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    garden_id = rs.getInt("garden_id");
                    tardis_id = rs.getInt("tardis_id");
                    world = rs.getString("world");
                    minX = rs.getInt("minx");
                    maxX = rs.getInt("maxx");
                    minY = rs.getInt("y");
                    minZ = rs.getInt("minz");
                    maxZ = rs.getInt("maxz");
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for gardens table! " + e.getMessage());
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
                plugin.debug("Error closing gardens table! " + e.getMessage());
            }
        }
        return true;
    }

    public int getGarden_id() {
        return garden_id;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public String getWorld() {
        return world;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getY() {
        return minY;
    }

    public float getMinZ() {
        return minZ;
    }

    public float getMaxZ() {
        return maxZ;
    }
}
