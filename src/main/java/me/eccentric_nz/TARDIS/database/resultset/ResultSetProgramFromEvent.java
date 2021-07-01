/*
 * Copyright (C) 2021 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.data.Program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Many facts, figures, and formulas are contained within the Matrix, including... the locations of the TARDIS vaults.
 * <p>
 * Control types: 0 = handbrake 1 = random button 2 = x-repeater 3 = z-repeater 4 = multiplier-repeater 5 =
 * environment-repeater 6 = artron button
 *
 * @author eccentric_nz
 */
public class ResultSetProgramFromEvent {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String uuid;
    private final String event;
    private final String prefix;
    private Program program;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet from the vaults table.
     *
     * @param plugin an instance of the main class.
     * @param uuid   a player UUID to refine the search.
     * @param event  an event string to refine the search.
     */
    public ResultSetProgramFromEvent(TARDIS plugin, String uuid, String event) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.event = event;
        prefix = this.plugin.getPrefix();
    }

    /**
     * Retrieves an SQL ResultSet from the programs table. This method builds an SQL query string from the parameters
     * supplied and then executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT * FROM " + prefix + "programs WHERE uuid = ? AND parsed = ?";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            statement.setString(2, event);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                program = new Program(
                        rs.getInt("program_id"),
                        rs.getString("uuid"),
                        rs.getString("name"),
                        rs.getString("inventory"),
                        rs.getString("parsed"),
                        rs.getBoolean("checked")
                );
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for programs table! " + e.getMessage());
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
                plugin.debug("Error closing programs table! " + e.getMessage());
            }
        }
        return true;
    }

    public Program getProgram() {
        return program;
    }
}
