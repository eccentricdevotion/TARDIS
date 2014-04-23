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
package me.eccentric_nz.TARDIS.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.Location;

/**
 *
 * @author eccentric_nz
 */
public class TARDII {

    private HashMap<String, Integer> timelords;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();

    /**
     * Fetches a list of TARDIS owners.
     *
     * @return a map of TARDIS owner names and TARDIS ids
     */
    public HashMap<String, Integer> getTimelords() {
        timelords = new HashMap<String, Integer>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id, owner FROM tardis";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    timelords.put(rs.getString("owner"), rs.getInt("tardis_id"));
                }
            }
        } catch (SQLException e) {
            TARDIS.plugin.debug("ResultSet error for tardis table! " + e.getMessage());

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                TARDIS.plugin.debug("Error closing tardis table! " + e.getMessage());
            }
        }
        return timelords;
    }

    /**
     * Retrieves a TARDIS's current location.
     *
     * @param id the TARDIS id to retrieve the location for
     * @return the current TARDIS location or null if not found
     */
    public Location getLocation(int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rs = new ResultSetCurrentLocation(TARDIS.plugin, where);
        if (rs.resultSet()) {
            return new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ());
        }
        return null;
    }
}
