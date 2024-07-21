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
import me.eccentric_nz.TARDIS.database.data.Throticle;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the personal preferences of the Time
 * lords themselves.
 *
 * @author eccentric_nz
 */
public class ResultSetThrottle {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the player_prefs table.
     *
     * @param plugin an instance of the main class.
     */
    public ResultSetThrottle(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves the Space Time Throttle setting from the player_prefs table. This method builds an SQL query string
     * from the parameters supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @param uuid the unique id of the player to get the setting for
     * @return the Space Time Throttle setting
     */
    public Throticle getSpeedAndParticles(String uuid) {
        SpaceTimeThrottle throttle = SpaceTimeThrottle.NORMAL;
        boolean particles = false;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String throttleQuery = "SELECT throttle FROM " + prefix + "player_prefs WHERE uuid = ?";
        String particleQuery = "SELECT particles_on FROM " + prefix + "particle_prefs WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(throttleQuery);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.next()) {
                throttle = SpaceTimeThrottle.getByDelay().get(rs.getInt("throttle"));
            }
            statement = connection.prepareStatement(particleQuery);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.next()) {
                particles = rs.getBoolean("particles_on");
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for throttle table! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing throttle table! " + e.getMessage());
            }
        }
        return new Throticle(throttle, particles);
    }
}
