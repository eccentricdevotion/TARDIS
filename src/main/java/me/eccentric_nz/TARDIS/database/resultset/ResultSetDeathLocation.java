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
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.World;

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
public class ResultSetDeathLocation {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final String uuid;
    private World world;
    private int x;
    private int y;
    private int z;
    private COMPASS direction;
    private boolean submarine;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the next locations table.
     *
     * @param plugin an instance of the main class.
     * @param uuid   a player's UUID to refine the search.
     */
    public ResultSetDeathLocation(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
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
        String query = "SELECT * FROM " + prefix + "deaths WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                world = TARDISAliasResolver.getWorldFromAlias(rs.getString("world"));
                if (world == null) {
                    return false;
                }
                x = rs.getInt("x");
                y = rs.getInt("y");
                z = rs.getInt("z");
                direction = COMPASS.valueOf(rs.getString("direction"));
                submarine = rs.getBoolean("submarine");
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for deaths table! " + e.getMessage());
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
                plugin.debug("Error closing deaths table! " + e.getMessage());
            }
        }
        return world != null;
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public COMPASS getDirection() {
        return direction;
    }

    public boolean isSubmarine() {
        return submarine;
    }
}
