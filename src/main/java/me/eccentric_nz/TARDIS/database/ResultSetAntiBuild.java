/*
 * Copyright (C) 2016 eccentric_nz
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
import java.util.UUID;
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
    private final String uuid;
    private Integer tardis_id;
    private final String prefix;

    /**
     * Creates a class instance that can be used to retrieve an SQL ResultSet
     * from the tardis & travellers table.
     *
     * @param plugin an instance of the main class.
     * @param uuid the uuid who is trying to build.
     */
    public ResultSetAntiBuild(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid.toString();
        this.prefix = this.plugin.getPrefix();
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
        String query = "SELECT " + prefix + "tardis.tardis_id FROM " + prefix + "tardis, " + prefix + "travellers, " + prefix + "player_prefs WHERE " + prefix + "travellers.uuid = ? AND " + prefix + "tardis.uuid != ? AND " + prefix + "player_prefs.build_on = 0 AND " + prefix + "tardis.tardis_id = " + prefix + "travellers.tardis_id AND " + prefix + "tardis.uuid = " + prefix + "player_prefs.uuid";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            statement.setString(2, uuid);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    this.tardis_id = rs.getInt("tardis_id");
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
