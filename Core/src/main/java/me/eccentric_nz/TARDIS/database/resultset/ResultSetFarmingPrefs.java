/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.data.FarmPrefs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... everything about the construction of
 * the TARDIS itself.
 *
 * @author eccentric_nz
 */
public class ResultSetFarmingPrefs {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String uuid;
    private final String prefix;
    private FarmPrefs data;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the farming_prefs table.
     *
     * @param plugin    an instance of the main class.
     * @param uuid the unique id of the player to get the farming preferences for.
     */
    public ResultSetFarmingPrefs(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the farming table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;

        String query = "SELECT * FROM " + prefix + "farming_prefs WHERE uuid = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                data = new FarmPrefs(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getBoolean("allay"),
                        rs.getBoolean("apiary"),
                        rs.getBoolean("aquarium"),
                        rs.getBoolean("bamboo"),
                        rs.getBoolean("birdcage"),
                        rs.getBoolean("farm"),
                        rs.getBoolean("geode"),
                        rs.getBoolean("hutch"),
                        rs.getBoolean("igloo"),
                        rs.getBoolean("iistubil"),
                        rs.getBoolean("lava"),
                        rs.getBoolean("mangrove"),
                        rs.getBoolean("pen"),
                        rs.getBoolean("stable"),
                        rs.getBoolean("stall"),
                        rs.getBoolean("village")
                );
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for farming_prefs table! " + e.getMessage());
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
                plugin.debug("Error closing farming_prefs table! " + e.getMessage());
            }
        }
        return true;
    }

    public FarmPrefs getData() {
        return data;
    }
}
