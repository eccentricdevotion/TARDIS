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
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;

/**
 * Gets a list of TARDIS ids whose power is on.
 *
 * @author eccentric_nz
 */
public class ResultSetStandby {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;

    public ResultSetStandby(TARDIS plugin) {
        this.plugin = plugin;
    }

    public HashMap<Integer, Integer> onStandby() {
        HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>();
        PreparedStatement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id, artron_level FROM tardis WHERE powered_on = 1";
        try {
            service.testConnection(connection);
            statement = connection.prepareStatement(query);
            rs = statement.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    ids.put(rs.getInt("tardis_id"), rs.getInt("artron_level"));
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for standby! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing standby! " + e.getMessage());
            }
        }
        return ids;
    }
}
