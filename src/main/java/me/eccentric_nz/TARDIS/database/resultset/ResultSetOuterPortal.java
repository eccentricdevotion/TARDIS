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
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the location of the TARDIS doors in
 * their different dimensions.
 *
 * @author eccentric_nz
 */
public class ResultSetOuterPortal {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final int id;
    private final String prefix;
    private Location location;
    private COMPASS direction;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the doors table.
     *
     * @param plugin an instance of the main class.
     * @param id     the TARDIS id to get the doors for.
     */
    public ResultSetOuterPortal(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the doors table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "doors WHERE tardis_id = " + id + " AND door_type = 0";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                location = TARDISStaticLocationGetters.getLocationFromDB(rs.getString("door_location"));
                direction = COMPASS.valueOf(rs.getString("door_direction"));
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for [exterior portal] doors table! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing [exterior portal] doors table! " + e.getMessage());
            }
        }
        return true;
    }

    public Location getLocation() {
        return location;
    }

    public COMPASS getDirection() {
        return direction;
    }
}
