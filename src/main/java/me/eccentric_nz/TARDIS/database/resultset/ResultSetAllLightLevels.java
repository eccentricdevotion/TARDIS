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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS
 * controls.
 *
 * @author eccentric_nz
 */
public class ResultSetAllLightLevels {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final int id;
    private int interiorLevel;
    private int exteriorLevel;
    private int consoleLevel;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the controls table.
     *
     * @param plugin an instance of the main class.
     * @param id     the TARDIS id of the control
     */
    public ResultSetAllLightLevels(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the controls table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT `type`, secondary FROM " + prefix + "controls WHERE `type` IN (49, 50, 56) AND `tardis_id` = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while(rs.next()) {
                    switch (rs.getInt("type")) {
                        case 49 -> exteriorLevel = rs.getInt("secondary");
                        case 50 -> interiorLevel = rs.getInt("secondary");
                        default -> consoleLevel = rs.getInt("secondary");
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for ALL light level controls from id! " + e.getMessage());
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
                plugin.debug("Error closing ALL light level controls from id! " + e.getMessage());
            }
        }
        return true;
    }

    public int getInteriorLevel() {
        return interiorLevel;
    }

    public int getExteriorLevel() {
        return exteriorLevel;
    }

    public int getConsoleLevel() {
        return consoleLevel;
    }
}
