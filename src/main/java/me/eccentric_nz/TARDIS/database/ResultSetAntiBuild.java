/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Many facts, figures, and formulas are contained within the Matrix,
 * including... the location of the TARDIS Police Box blocks.
 *
 * @author eccentric_nz
 */
public class ResultSetAntiBuild {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String player;
    private Integer tardis_id;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the tardis & travellers table.
     *
     * @param plugin an instance of the main class.
     * @param player the player who is trying to build.
     */
    public ResultSetAntiBuild(TARDIS plugin, String player) {
        this.plugin = plugin;
        this.player = player;
    }

    /**
     * Retrieves an SQL ResultSet from the tardis & travellers table. This
     * method builds an SQL query string from the parameters supplied and then
     * executes the query. Use the getters to retrieve the results.
     *
     * @return true or false depending on whether any data matches the query
     */
    public boolean resultSet() {
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis.tardis_id FROM tardis, travellers, player_prefs WHERE travellers.player = ? AND tardis.owner != ? AND player_prefs.build_on = 0 AND tardis.tardis_id = travellers.tardis_id AND tardis.owner = player_prefs.player";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, player);
            statement.setString(2, player);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    this.tardis_id = Integer.valueOf(rs.getInt("tardis_id"));
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for antibuild tables! " + e.getMessage());
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
                plugin.debug("Error closing antibuild tables! " + e.getMessage());
            }
        }
        return true;
    }

    public Integer getTardis_id() {
        return tardis_id;
    }
}
